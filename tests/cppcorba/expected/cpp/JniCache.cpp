#include "JniCache.h"

namespace corbabinding {

JniCache::JniCache(JNIEnv * _env_) {

	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/IncludedIdl/IncludedStruct");
		com.matejhrazdira.pojos.IncludedIdl.IncludedStruct._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.IncludedIdl.IncludedStruct._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/lang/String;)V");

		com.matejhrazdira.pojos.IncludedIdl.IncludedStruct.someMember = _env_->GetFieldID(_cls_, "someMember", "Ljava/lang/String;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/SimpleStruct");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/lang/String;IJJJJLcom/matejhrazdira/pojos/IncludedIdl/IncludedStruct;Lcom/matejhrazdira/pojos/IncludedIdl/IncludedStruct;Ljava/lang/String;Ljava/lang/String;)V");

		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.stringMember = _env_->GetFieldID(_cls_, "stringMember", "Ljava/lang/String;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.intMember = _env_->GetFieldID(_cls_, "intMember", "I");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.typedefedMember = _env_->GetFieldID(_cls_, "typedefedMember", "J");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.uintMember = _env_->GetFieldID(_cls_, "uintMember", "J");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.longMember = _env_->GetFieldID(_cls_, "longMember", "J");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.ulongMember = _env_->GetFieldID(_cls_, "ulongMember", "J");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.includedMember = _env_->GetFieldID(_cls_, "includedMember", "Lcom/matejhrazdira/pojos/IncludedIdl/IncludedStruct;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.fullyScopedIncludedMember = _env_->GetFieldID(_cls_, "fullyScopedIncludedMember", "Lcom/matejhrazdira/pojos/IncludedIdl/IncludedStruct;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.duplicate1 = _env_->GetFieldID(_cls_, "duplicate1", "Ljava/lang/String;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.duplicate2 = _env_->GetFieldID(_cls_, "duplicate2", "Ljava/lang/String;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/StructWithArrays");
		com.matejhrazdira.pojos.SimpleIdl.StructWithArrays._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.StructWithArrays._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;)V");

		com.matejhrazdira.pojos.SimpleIdl.StructWithArrays.typedefedArray = _env_->GetFieldID(_cls_, "typedefedArray", "Ljava/util/List;");
		com.matejhrazdira.pojos.SimpleIdl.StructWithArrays.directArray = _env_->GetFieldID(_cls_, "directArray", "Ljava/util/List;");
		com.matejhrazdira.pojos.SimpleIdl.StructWithArrays.typedefTypedefedArray = _env_->GetFieldID(_cls_, "typedefTypedefedArray", "Ljava/util/List;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/SimpleEnum");
		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._values_.reserve(3);
		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, "EN_VAL_A", "Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;"));
		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, "EN_VAL_B", "Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;"));
		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, "EN_VAL_C", "Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;"));

		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._ordinal_ = _env_->GetMethodID(_cls_, "ordinal", "()I");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/SimpleUnion");
		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;Ljava/lang/String;JLjava/util/List;)V");

		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._type_ = _env_->GetFieldID(_cls_, "_type_", "Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.strInUnion = _env_->GetFieldID(_cls_, "strInUnion", "Ljava/lang/String;");
		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.ullInUnion = _env_->GetFieldID(_cls_, "ullInUnion", "J");
		com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.simpleStructsInUnion = _env_->GetFieldID(_cls_, "simpleStructsInUnion", "Ljava/util/List;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/WeirdUnion");
		com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(ILjava/lang/String;Ljava/lang/String;)V");

		com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._type_ = _env_->GetFieldID(_cls_, "_type_", "I");
		com.matejhrazdira.pojos.SimpleIdl.WeirdUnion.foo = _env_->GetFieldID(_cls_, "foo", "Ljava/lang/String;");
		com.matejhrazdira.pojos.SimpleIdl.WeirdUnion.bar = _env_->GetFieldID(_cls_, "bar", "Ljava/lang/String;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/DefaultUnionFromEnum");
		com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;Ljava/lang/String;Ljava/lang/String;)V");

		com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._type_ = _env_->GetFieldID(_cls_, "_type_", "Lcom/matejhrazdira/pojos/SimpleIdl/SimpleEnum;");
		com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum.foo = _env_->GetFieldID(_cls_, "foo", "Ljava/lang/String;");
		com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum.bar = _env_->GetFieldID(_cls_, "bar", "Ljava/lang/String;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/NoMemberException");
		com.matejhrazdira.pojos.SimpleIdl.NoMemberException._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.NoMemberException._ctor_ = _env_->GetMethodID(_cls_, "<init>", "()V");


		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/ExceptionWithMembers");
		com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(Ljava/lang/String;)V");

		com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers.someStringMember = _env_->GetFieldID(_cls_, "someStringMember", "Ljava/lang/String;");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/SimpleIdlInterface");
		com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface._ctor_ = _env_->GetMethodID(_cls_, "<init>", "(J)V");

		com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface._native_address_ = _env_->GetFieldID(_cls_, "_native_address_", "J");

		_env_->DeleteLocalRef(_cls_);
	}
	{
		jclass _cls_ = _env_->FindClass("com/matejhrazdira/pojos/SimpleIdl/SimpleIdlInterface$NestedException");
		com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException._cls_ = (jclass) _env_->NewGlobalRef(_cls_);

		com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException._ctor_ = _env_->GetMethodID(_cls_, "<init>", "()V");


		_env_->DeleteLocalRef(_cls_);
	}
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
