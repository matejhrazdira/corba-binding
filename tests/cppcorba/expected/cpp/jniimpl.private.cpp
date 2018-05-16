#include "jniimpl.private.h"

#include "corbabinding.h"
#include "corbabindinglib.h"

#include <map>
#include <string>

using namespace corbabinding;

namespace corbabinding {
class TypeCache {
public:
	typedef jobject (* convertAnyImpl)(JNIEnv * env, const CORBA::Any & in);
	TypeCache();
	virtual ~TypeCache();
	jobject convert(JNIEnv * env, const char * name, CORBA::Object_ptr in);
	convertAnyImpl getAnyConversion(const char * name);

	static jobject convertAnyNoOp(JNIEnv * env, const CORBA::Any & any) {
		return nullptr;
	}

private:
	typedef jobject (* convertImpl)(JNIEnv * env, CORBA::Object_ptr in);
	std::map<std::string, convertImpl> mInterfaceTable;

	template<typename T> static jobject convertObject(JNIEnv * env, CORBA::Object_ptr obj) {
		if (obj) {
			return ::corbabinding::convert(env, T::_narrow(obj));
		} else {
			return nullptr;
		}
	}

	std::map<std::string, convertAnyImpl> mAnyTable;

	template<typename T> static jobject convertAny(JNIEnv * env, const CORBA::Any & any) {
		const T * ptr;
		if ((any >>= ptr)) {
			return ::corbabinding::convert(env, ptr);
		} else {
			return nullptr;
		}
	}
};

TypeCache::TypeCache() {
	mAnyTable["com.matejhrazdira.pojos.IncludedIdl.IncludedStruct"] = convertAny<::IncludedIdl::IncludedStruct>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.SimpleStruct"] = convertAny<::SimpleIdl::SimpleStruct>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.StructWithArrays"] = convertAny<::SimpleIdl::StructWithArrays>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.SimpleUnion"] = convertAny<::SimpleIdl::SimpleUnion>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.WeirdUnion"] = convertAny<::SimpleIdl::WeirdUnion>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum"] = convertAny<::SimpleIdl::DefaultUnionFromEnum>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.NoMemberException"] = convertAny<::SimpleIdl::NoMemberException>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers"] = convertAny<::SimpleIdl::ExceptionWithMembers>;
	mInterfaceTable["com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface"] = convertObject<::SimpleIdl::SimpleIdlInterface>;
	mAnyTable["com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException"] = convertAny<::SimpleIdl::SimpleIdlInterface::NestedException>;
}

TypeCache::~TypeCache() {

}

jobject TypeCache::convert(JNIEnv* env, const char * name, CORBA::Object_ptr in) {
	auto item = mInterfaceTable.find(name);
	if (item != mInterfaceTable.end()) {
		return item->second(env, in);
	} else {
		return nullptr;
	}
}

TypeCache::convertAnyImpl TypeCache::getAnyConversion(const char * name) {
	auto item = mAnyTable.find(name);
	if (item != mAnyTable.end()) {
		return item->second;
	} else {
		return convertAnyNoOp;
	}
}

class EventConsumer : public AnyEventConsumer {
public:
	EventConsumer(
			JNIEnv * env,
			jobject jconsumer,
			TypeCache * typeCache,
			CORBA::ULong type,
			const char * typeName)
		: AnyEventConsumer(type) {
		mConversionImpl = typeCache->getAnyConversion(typeName);
		mConsumer = env->NewGlobalRef(jconsumer);
		env->GetJavaVM(&mJvm);
		mJniVersion = env->GetVersion();
	}

	void onAnyEvent(const CORBA::Any & any) {
		JNIEnv * env = attach();
		if (!env) {
			return;
		}
		jobject callbackData = mConversionImpl(env, any);
		env->CallVoidMethod(mConsumer, _jni_->_impl_._event_consumer_._callback_, callbackData);
		env->DeleteLocalRef(callbackData);
	}

	virtual ~EventConsumer() {
		JNIEnv * env = attach();
		env->DeleteGlobalRef(mConsumer);
	}

private:
	JavaVM * mJvm;
	jint mJniVersion;
	TypeCache::convertAnyImpl mConversionImpl;
	jobject mConsumer;

	JNIEnv * attach() {
		JNIEnv * env = nullptr;
		mJvm->GetEnv(reinterpret_cast<void**>(&env), mJniVersion);
		if (env) {
			return env;
		} else {
			JavaVMAttachArgs threadParams;
			threadParams.version = mJniVersion;
			threadParams.name = (char *) "EvnetConsumer thread (native)";
			threadParams.group = 0x0;
#ifndef ANDROID
			jint res = mJvm->AttachCurrentThread((void **) &env, &threadParams);
#else /* ANDROID */
			jint res = mJvm->AttachCurrentThread(&env, &threadParams);
#endif /* ANDROID */
			return env;
		}
	}
};

} /* namespace corbabinding */

class JvmCleanup: public corbabinding::ThreadCleanup {
public:
	JvmCleanup(JNIEnv * env) : ThreadCleanup() {
		env->GetJavaVM(&mJvm);
		mJniVersion = env->GetVersion();
	}

	virtual void cleanup() {
		JNIEnv * env = attach();
		if (env) {
			mJvm->DetachCurrentThread();
		}
	}

private:
	JavaVM * mJvm;
	jint mJniVersion;

	JNIEnv * attach() {
		JNIEnv * env = nullptr;
		mJvm->GetEnv(reinterpret_cast<void**>(&env), mJniVersion);
		if (env) {
			return env;
		} else {
			JavaVMAttachArgs threadParams;
			threadParams.version = mJniVersion;
			threadParams.name = (char *) "ORB::run() thread (native)";
			threadParams.group = 0x0;
#ifndef ANDROID
			jint res = mJvm->AttachCurrentThread((void **) &env, &threadParams);
#else /* ANDROID */
			jint res = mJvm->AttachCurrentThread(&env, &threadParams);
#endif /* ANDROID */
			return env;
		}
	}
};

static TypeCache * sTypeCache = nullptr;

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
	JNIEnv* env;
	if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}

	init(env);
	sTypeCache = new TypeCache();

	return JNI_VERSION_1_6;
}

static char * getStringChars(JNIEnv * env, jstring jstr) {
	const char * str = env->GetStringUTFChars(jstr, nullptr);
	char * result = CORBA::string_dup(str);
	env->ReleaseStringUTFChars(jstr, str);
	return result;
}

JNIEXPORT jlong JNICALL Java_CorbaProvider_init(JNIEnv * env, jobject thiz, jobjectArray jorbArgs, jstring jeventServiceName) {

	char * eventServiceName = getStringChars(env, jeventServiceName);

	jsize jorbArgCount = env->GetArrayLength(jorbArgs);
	int orbArgCount = jorbArgCount + 2;

	char ** orbArgs = new char *[orbArgCount];

	orbArgs[0] = CORBA::string_dup("UNUSED");
	for (int i = 0; i < jorbArgCount; i++) {
		jstring jarg = (jstring) env->GetObjectArrayElement(jorbArgs, i);
		orbArgs[i + 1] = getStringChars(env, jarg);
		env->DeleteLocalRef(jarg);
	}
	orbArgs[orbArgCount - 1] = CORBA::string_dup("");

	jthrowable pendingException = nullptr;
	NativeWrapper * result = nullptr;
	JvmCleanup * cleanup = new JvmCleanup(env);
	try {
		result = new NativeWrapper(orbArgCount, orbArgs, eventServiceName, cleanup);
	} catch (const CORBA::Exception & e) {
		pendingException = convert(env, e);
		delete cleanup;
	}

	CORBA::string_free(eventServiceName);
	for (int i = 0; i < orbArgCount; i++) {
		CORBA::string_free(orbArgs[i]);
	}

	if (pendingException != nullptr) {
		env->Throw(pendingException);
	}

	return (jlong) result;
}

JNIEXPORT jobject JNICALL Java_CorbaProvider_resolveImpl(JNIEnv * env, jobject thiz, jlong jnativeWrapper, jstring jclassName, jstring jcorbaName) {
	try {
		CORBA::String_var className = getStringChars(env, jclassName);
		CORBA::String_var corbaName = getStringChars(env, jcorbaName);
		NativeWrapper * nativeWrapper = (NativeWrapper *) jnativeWrapper;
		CORBA::Object_var obj = nativeWrapper->resolveName(corbaName);
		return sTypeCache->convert(env, className, obj);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		return (jobject) 0x0;
	}
}

JNIEXPORT void JNICALL Java_CorbaProvider_disposeImpl(JNIEnv * env, jobject thiz, jlong nativeWrapper) {
	delete (NativeWrapper *) nativeWrapper;
}

JNIEXPORT jlong JNICALL Java_EventConsumer_connectConsumer(JNIEnv * env, jobject thiz, jlong jnativeWrapper, jint subscription, jstring eventClass) {
	NativeWrapper * nativeWrapper = ((NativeWrapper *) jnativeWrapper);
	CORBA::String_var className = getStringChars(env, eventClass);
	EventConsumer * eventConsumer = new EventConsumer(env, thiz, sTypeCache, subscription, className.in());
	try {
		eventConsumer->connect(nativeWrapper);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		delete eventConsumer;
		eventConsumer = 0x0;
	}
	return (jlong) eventConsumer;
}

JNIEXPORT void JNICALL Java_EventConsumer_disposeImpl(JNIEnv * env, jobject thiz, jlong nativeWrapper) {
	EventConsumer * consumer = (EventConsumer *) nativeWrapper;
	try {
		consumer->disconnect();
	} catch (const CORBA::Exception & e) {
		// ignore
	}
	delete consumer;
}
