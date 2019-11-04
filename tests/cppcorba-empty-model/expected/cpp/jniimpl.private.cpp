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
	typedef void (* convertToAnyImpl)(JNIEnv * env, const jobject in, CORBA::Any & out);
	TypeCache();
	virtual ~TypeCache();
	jobject convert(JNIEnv * env, const char * name, CORBA::Object_ptr in);
	convertAnyImpl getAnyConversion(const char * name);
	convertToAnyImpl getToAnyConversion(const char * name);

	static jobject convertAnyNoOp(JNIEnv * env, const CORBA::Any & any) {
		return nullptr;
	}

	static void convertToAnyNoOp(JNIEnv * env, const jobject in, CORBA::Any & out) {
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

	std::map<std::string, convertToAnyImpl> mToAnyTable;

	template<typename T> static void convertToAny(JNIEnv * env, const jobject in, CORBA::Any & out) {
		T value;
		::corbabinding::convert(env, in, value);
		out <<= value;
	}
};

TypeCache::TypeCache() {

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

TypeCache::convertToAnyImpl TypeCache::getToAnyConversion(const char * name) {
	auto item = mToAnyTable.find(name);
	if (item != mToAnyTable.end()) {
		return item->second;
	} else {
		return convertToAnyNoOp;
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

class EventProducer : public AbsEventProducer<jobject> {
public:
	EventProducer(
			JNIEnv * env,
			TypeCache * typeCache,
			RtecEventComm::EventSourceID sourceId,
			RtecEventComm::EventType type,
			const char * typeName)
		: AbsEventProducer(sourceId, type) {
		mConversionImpl = typeCache->getToAnyConversion(typeName);
		env->GetJavaVM(&mJvm);
		mJniVersion = env->GetVersion();
	}

	virtual bool putData(CORBA::Any & event, const jobject & data) {
		JNIEnv * env = attach();
		if (!env) {
			return false;
		}
		mConversionImpl(env, data, event);
		return true;
	}

private:
	JavaVM * mJvm;
	jint mJniVersion;
	TypeCache::convertToAnyImpl mConversionImpl;

	JNIEnv * attach() {
		JNIEnv * env = nullptr;
		mJvm->GetEnv(reinterpret_cast<void**>(&env), mJniVersion);
		if (env) {
			return env;
		} else {
			JavaVMAttachArgs threadParams;
			threadParams.version = mJniVersion;
			threadParams.name = (char *) "EvnetProducer thread (native)";
			threadParams.group = 0x0;
			jint res = mJvm->AttachCurrentThread((void **) &env, &threadParams);
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

JNIEXPORT jlong JNICALL Java_CorbaProvider_init(JNIEnv * env, jobject thiz, jobjectArray jorbArgs) {

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
		result = new NativeWrapper(orbArgCount, orbArgs, cleanup);
	} catch (const CORBA::Exception & e) {
		pendingException = convert(env, e);
		delete cleanup;
	}

	for (int i = 0; i < orbArgCount; i++) {
		CORBA::string_free(orbArgs[i]);
	}

	if (pendingException != nullptr) {
		env->Throw(pendingException);
	}

	return (jlong) result;
}

JNIEXPORT jobject JNICALL Java_CorbaProvider_resolveImpl(JNIEnv * env, jobject thiz, jlong jnativeWrapper, jstring jclassName, jstring jcorbaStr) {
	try {
		CORBA::String_var className = getStringChars(env, jclassName);
		CORBA::String_var corbaStr = getStringChars(env, jcorbaStr);
		NativeWrapper * nativeWrapper = (NativeWrapper *) jnativeWrapper;
		CORBA::Object_var obj = nativeWrapper->resolve(corbaStr);
		return sTypeCache->convert(env, className, obj);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		return (jobject) 0x0;
	}
}

JNIEXPORT void JNICALL Java_CorbaProvider_disposeImpl(JNIEnv * env, jobject thiz, jlong nativeWrapper) {
	delete (NativeWrapper *) nativeWrapper;
}

JNIEXPORT jlong JNICALL Java_EventService_init(JNIEnv * env, jobject thiz, jlong jNativeWrapper, jstring jeventServiceStr) {
	NativeWrapper * nativeWrapper = (NativeWrapper *) jNativeWrapper;
	CORBA::String_var eventServiceStr = getStringChars(env, jeventServiceStr);
	return (jlong) new EventServiceWrapper(nativeWrapper, eventServiceStr);
}

JNIEXPORT void JNICALL Java_EventService_connectImpl(JNIEnv * env, jobject thiz, jlong jwrapper) {
	try {
		EventServiceWrapper *wrapper = (EventServiceWrapper *) jwrapper;
		wrapper->connect();
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
	}
}

JNIEXPORT jboolean JNICALL Java_EventService_isAliveImpl(JNIEnv * env, jobject thiz, jlong jwrapper) {
	try {
		EventServiceWrapper *wrapper = (EventServiceWrapper *) jwrapper;
		return wrapper->isAlive();
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		return false;
	}
}

JNIEXPORT void JNICALL Java_EventService_disposeImpl(JNIEnv * env, jobject thiz, jlong jwrapper) {
	delete (EventServiceWrapper *) jwrapper;
}

JNIEXPORT jlong JNICALL Java_EventConsumer_connectConsumer(JNIEnv * env, jobject thiz, jlong jwrapper, jint subscription, jstring eventClass) {
	EventServiceWrapper * wrapper = ((EventServiceWrapper *) jwrapper);
	CORBA::String_var className = getStringChars(env, eventClass);
	EventConsumer * eventConsumer = new EventConsumer(env, thiz, sTypeCache, subscription, className.in());
	try {
		eventConsumer->connect(wrapper);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		delete eventConsumer;
		eventConsumer = 0x0;
	}
	return (jlong) eventConsumer;
}

JNIEXPORT void JNICALL Java_EventConsumer_disposeImpl(JNIEnv * env, jobject thiz, jlong jconsumer) {
	EventConsumer * consumer = (EventConsumer *) jconsumer;
	try {
		consumer->disconnect();
	} catch (const CORBA::Exception & e) {
		// ignore
	}
}

JNIEXPORT jlong JNICALL Java_EventProducer_connectProducer(JNIEnv * env, jobject thiz, jlong jwrapper, jint source, jint subscription, jstring eventClass) {
	EventServiceWrapper * eventServiceWrapper = ((EventServiceWrapper *) jwrapper);
	CORBA::String_var className = getStringChars(env, eventClass);
	EventProducer * eventProducer = new EventProducer(env, sTypeCache, source, subscription, className.in());
	try {
		eventProducer->connect(eventServiceWrapper);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
		delete eventProducer;
		eventProducer = 0x0;
	}
	return (jlong) eventProducer;
}

JNIEXPORT void JNICALL Java_EventProducer_pushEventImpl(JNIEnv * env, jobject thiz, jlong jproducer, jobject eventData) {
	EventProducer * producer = (EventProducer *) jproducer;
	try {
		producer->push(eventData);
	} catch (const CORBA::Exception & e) {
		env->Throw(convert(env, e));
	}
}

JNIEXPORT void JNICALL Java_EventProducer_disposeImpl(JNIEnv * env, jobject thiz, jlong jproducer) {
	EventProducer * producer = (EventProducer *) jproducer;
	try {
		producer->disconnect();
	} catch (const CORBA::Exception & e) {
		// ignore
	}
}
