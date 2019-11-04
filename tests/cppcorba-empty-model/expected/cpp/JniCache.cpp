#include "JniCache.h"

namespace corbabinding {

JniCache::JniCache(JNIEnv * _env_) {


	{
		jclass _cls_ = _env_->FindClass("java/lang/Boolean");
		java.lang.Boolean._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Boolean._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(Z)Ljava/lang/Boolean;");
		java.lang.Boolean.booleanValue = _env_->GetMethodID(_cls_, "booleanValue", "()Z");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Byte");
		java.lang.Byte._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Byte._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(B)Ljava/lang/Byte;");
		java.lang.Byte.byteValue = _env_->GetMethodID(_cls_, "byteValue", "()B");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Integer");
		java.lang.Integer._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Integer._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(I)Ljava/lang/Integer;");
		java.lang.Integer.intValue = _env_->GetMethodID(_cls_, "intValue", "()I");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Long");
		java.lang.Long._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Long._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(J)Ljava/lang/Long;");
		java.lang.Long.longValue = _env_->GetMethodID(_cls_, "longValue", "()J");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Float");
		java.lang.Float._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Float._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(F)Ljava/lang/Float;");
		java.lang.Float.floatValue = _env_->GetMethodID(_cls_, "floatValue", "()F");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Double");
		java.lang.Double._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Double._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(D)Ljava/lang/Double;");
		java.lang.Double.doubleValue = _env_->GetMethodID(_cls_, "doubleValue", "()D");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Character");
		java.lang.Character._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Character._ctor_ = _env_->GetStaticMethodID(_cls_, "valueOf", "(C)Ljava/lang/Character;");
		java.lang.Character.charValue = _env_->GetMethodID(_cls_, "charValue", "()C");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/util/ArrayList");
		java.util.ArrayList._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.util.ArrayList._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(I)V");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/util/List");
		java.util.List.add = _env_->GetMethodID(_cls_, "add", "(Ljava/lang/Object;)Z");
		java.util.List.get = _env_->GetMethodID(_cls_, "get", "(I)Ljava/lang/Object;");
		java.util.List.size = _env_->GetMethodID(_cls_, "size", "()I");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("Var");
		_impl_._var_._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		_impl_._var_._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/lang/Object;)V");
		_impl_._var_._get_ = _env_->GetMethodID(_cls_, "get", "()Ljava/lang/Object;");
		_impl_._var_._set_ = _env_->GetMethodID(_cls_, "set", "(Ljava/lang/Object;)V");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("CorbaException");
		_impl_._corba_exception_._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		_impl_._corba_exception_._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("AlreadyDisposedException");
		_impl_._already_disposed_exception_._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		_impl_._already_disposed_exception_._ctor_ = _env_->GetMethodID(_cls_, "<init>", "()V");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("EventConsumer");
		_impl_._event_consumer_._callback_ = _env_->GetMethodID(_cls_, "onEvent", "(Ljava/lang/Object;)V");
		_env_->DeleteLocalRef(_cls_);
	}
}

JniCache::~JniCache() {}

} /* namespace corbabinding */
