#include "JniCache.h"

namespace corbabinding {

JniCache::JniCache(JNIEnv * _env_) {

	{
		jclass _cls_ = _env_->FindClass("java/lang/Boolean");
		java.lang.Boolean._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Boolean._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Z)V");
		java.lang.Boolean.booleanValue = _env_->GetMethodID(_cls_, "booleanValue", "()Z");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Byte");
		java.lang.Byte._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Byte._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(B)V");
		java.lang.Byte.byteValue = _env_->GetMethodID(_cls_, "byteValue", "()B");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Integer");
		java.lang.Integer._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Integer._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(I)V");
		java.lang.Integer.intValue = _env_->GetMethodID(_cls_, "intValue", "()I");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Long");
		java.lang.Long._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Long._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(J)V");
		java.lang.Long.longValue = _env_->GetMethodID(_cls_, "longValue", "()J");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Float");
		java.lang.Float._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Float._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(F)V");
		java.lang.Float.floatValue = _env_->GetMethodID(_cls_, "floatValue", "()F");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Double");
		java.lang.Double._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Double._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(D)V");
		java.lang.Double.doubleValue = _env_->GetMethodID(_cls_, "doubleValue", "()D");
		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("java/lang/Character");
		java.lang.Character._cls_ = (jclass) _env_->NewGlobalRef(_cls_);
		java.lang.Character._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(C)V");
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
}

JniCache::~JniCache() {}

} /* namespace corbabinding */
