#include "corbabinding.h"

#include <iconv.h>

#define CB_CORBA_USES_UTF_8

namespace corbabinding {

JniCache * _jni_;

void init(JNIEnv * _env_) {
	if (!_jni_) {
		_jni_ = new JniCache(_env_);
	}
}

jobject convert(JNIEnv * _env_, const ::IncludedIdl::IncludedStruct & _in_) {
	jobject someMember = convert(_env_, _in_.someMember);

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.IncludedIdl.IncludedStruct._cls_, _jni_->com.matejhrazdira.pojos.IncludedIdl.IncludedStruct._ctor_, someMember);

	_env_->DeleteLocalRef(someMember);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::IncludedIdl::IncludedStruct & _out_) {
	jobject someMember = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.IncludedIdl.IncludedStruct.someMember);
	convert(_env_, someMember, _out_.someMember);
	_env_->DeleteLocalRef(someMember);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::SimpleStruct & _in_) {
	jobject stringMember = convert(_env_, _in_.stringMember);
	jint intMember = convert(_env_, _in_.intMember);
	jlong typedefedMember = convert(_env_, _in_.typedefedMember);
	jlong uintMember = convert(_env_, _in_.uintMember);
	jlong longMember = convert(_env_, _in_.longMember);
	jlong ulongMember = convert(_env_, _in_.ulongMember);
	jobject includedMember = convert(_env_, _in_.includedMember);
	jobject fullyScopedIncludedMember = convert(_env_, _in_.fullyScopedIncludedMember);
	jobject duplicate1 = convert(_env_, _in_.duplicate1);
	jobject duplicate2 = convert(_env_, _in_.duplicate2);

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct._ctor_, stringMember, intMember, typedefedMember, uintMember, longMember, ulongMember, includedMember, fullyScopedIncludedMember, duplicate1, duplicate2);

	_env_->DeleteLocalRef(stringMember);
	_env_->DeleteLocalRef(includedMember);
	_env_->DeleteLocalRef(fullyScopedIncludedMember);
	_env_->DeleteLocalRef(duplicate1);
	_env_->DeleteLocalRef(duplicate2);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SimpleStruct & _out_) {
	jobject stringMember = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.stringMember);
	convert(_env_, stringMember, _out_.stringMember);
	_env_->DeleteLocalRef(stringMember);

	jint intMember = _env_->GetIntField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.intMember);
	convert(_env_, intMember, _out_.intMember);

	jlong typedefedMember = _env_->GetLongField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.typedefedMember);
	convert(_env_, typedefedMember, _out_.typedefedMember);

	jlong uintMember = _env_->GetLongField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.uintMember);
	convert(_env_, uintMember, _out_.uintMember);

	jlong longMember = _env_->GetLongField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.longMember);
	convert(_env_, longMember, _out_.longMember);

	jlong ulongMember = _env_->GetLongField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.ulongMember);
	convert(_env_, ulongMember, _out_.ulongMember);

	jobject includedMember = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.includedMember);
	convert(_env_, includedMember, _out_.includedMember);
	_env_->DeleteLocalRef(includedMember);

	jobject fullyScopedIncludedMember = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.fullyScopedIncludedMember);
	convert(_env_, fullyScopedIncludedMember, _out_.fullyScopedIncludedMember);
	_env_->DeleteLocalRef(fullyScopedIncludedMember);

	jobject duplicate1 = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.duplicate1);
	convert(_env_, duplicate1, _out_.duplicate1);
	_env_->DeleteLocalRef(duplicate1);

	jobject duplicate2 = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleStruct.duplicate2);
	convert(_env_, duplicate2, _out_.duplicate2);
	_env_->DeleteLocalRef(duplicate2);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::StructWithArrays & _in_) {
	jobject typedefedArray = convert(_env_, _in_.typedefedArray);
	jobject directArray = convert(_env_, _in_.directArray);

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.SimpleIdl.StructWithArrays._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.StructWithArrays._ctor_, typedefedArray, directArray);

	_env_->DeleteLocalRef(typedefedArray);
	_env_->DeleteLocalRef(directArray);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::StructWithArrays & _out_) {
	jobject typedefedArray = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.StructWithArrays.typedefedArray);
	convert(_env_, typedefedArray, _out_.typedefedArray);
	_env_->DeleteLocalRef(typedefedArray);

	jobject directArray = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.StructWithArrays.directArray);
	convert(_env_, directArray, _out_.directArray);
	_env_->DeleteLocalRef(directArray);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::SimpleEnum & _in_) {
	return _env_->GetStaticObjectField(_jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._values_[(int) _in_]);
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SimpleEnum & _out_) {
	_out_ = (::SimpleIdl::SimpleEnum) _env_->CallIntMethod(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleEnum._ordinal_);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::SimpleUnion & _in_) {
	jobject _type_ = nullptr;
	jobject strInUnion = nullptr;
	jlong ullInUnion = 0;
	jobject simpleStructsInUnion = nullptr;

	_type_ = convert(_env_, _in_._d());
	switch(_in_._d()) {
		case ::SimpleIdl::EN_VAL_A:
			strInUnion = convert(_env_, _in_.strInUnion());
			break;
		case ::SimpleIdl::EN_VAL_B:
			ullInUnion = convert(_env_, _in_.ullInUnion());
			break;
		case ::SimpleIdl::EN_VAL_C:
			simpleStructsInUnion = convert(_env_, _in_.simpleStructsInUnion());
			break;
	}

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._ctor_, _type_, strInUnion, ullInUnion, simpleStructsInUnion);

	_env_->DeleteLocalRef(_type_);
	_env_->DeleteLocalRef(strInUnion);
	_env_->DeleteLocalRef(simpleStructsInUnion);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SimpleUnion & _out_) {
	jobject _j__type__ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion._type_);
	::SimpleIdl::SimpleEnum _type_;
	convert(_env_, _j__type__, _type_);
	_env_->DeleteLocalRef(_j__type__);

	switch(_type_) {
		case ::SimpleIdl::EN_VAL_A:
		{
			jobject _j_strInUnion_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.strInUnion);
			char * strInUnion = convert<char>(_env_, _j_strInUnion_);
			_env_->DeleteLocalRef(_j_strInUnion_);
			_out_.strInUnion(strInUnion);
			break;
		}
		case ::SimpleIdl::EN_VAL_B:
		{
			jlong _j_ullInUnion_ = _env_->GetLongField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.ullInUnion);
			::CORBA::ULongLong ullInUnion;
			convert(_env_, _j_ullInUnion_, ullInUnion);
			_out_.ullInUnion(ullInUnion);
			break;
		}
		case ::SimpleIdl::EN_VAL_C:
		{
			jobject _j_simpleStructsInUnion_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleUnion.simpleStructsInUnion);
			::SimpleIdl::structs simpleStructsInUnion;
			convert(_env_, _j_simpleStructsInUnion_, simpleStructsInUnion);
			_env_->DeleteLocalRef(_j_simpleStructsInUnion_);
			_out_.simpleStructsInUnion(simpleStructsInUnion);
			break;
		}
	}

	_out_._d(_type_);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::WeirdUnion & _in_) {
	jint _type_ = 0;
	jobject foo = nullptr;
	jobject bar = nullptr;

	_type_ = convert(_env_, _in_._d());
	switch(_in_._d()) {
		case 1 + 3:
		case 1 + 2 * 5:
		case 2 + 6 * (1 - 3) * ( -5 / 2) - 5:
			foo = convert(_env_, _in_.foo());
			break;
		default:
			bar = convert(_env_, _in_.bar());
			break;
	}

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._ctor_, _type_, foo, bar);

	_env_->DeleteLocalRef(foo);
	_env_->DeleteLocalRef(bar);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::WeirdUnion & _out_) {
	jint _j__type__ = _env_->GetIntField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.WeirdUnion._type_);
	::CORBA::Long _type_;
	convert(_env_, _j__type__, _type_);

	switch(_type_) {
		case 1 + 3:
		case 1 + 2 * 5:
		case 2 + 6 * (1 - 3) * ( -5 / 2) - 5:
		{
			jobject _j_foo_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.WeirdUnion.foo);
			char * foo = convert<char>(_env_, _j_foo_);
			_env_->DeleteLocalRef(_j_foo_);
			_out_.foo(foo);
			break;
		}
		default:
		{
			jobject _j_bar_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.WeirdUnion.bar);
			char * bar = convert<char>(_env_, _j_bar_);
			_env_->DeleteLocalRef(_j_bar_);
			_out_.bar(bar);
			break;
		}
	}

	_out_._d(_type_);
}

jobject convert(JNIEnv * _env_, const ::SimpleIdl::DefaultUnionFromEnum & _in_) {
	jobject _type_ = nullptr;
	jobject foo = nullptr;
	jobject bar = nullptr;

	_type_ = convert(_env_, _in_._d());
	switch(_in_._d()) {
		case ::SimpleIdl::EN_VAL_A:
		case ::SimpleIdl::EN_VAL_B:
			foo = convert(_env_, _in_.foo());
			break;
		default:
			bar = convert(_env_, _in_.bar());
			break;
	}

	jobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._cls_, _jni_->com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._ctor_, _type_, foo, bar);

	_env_->DeleteLocalRef(_type_);
	_env_->DeleteLocalRef(foo);
	_env_->DeleteLocalRef(bar);

	return _result_;
}

void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::DefaultUnionFromEnum & _out_) {
	jobject _j__type__ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum._type_);
	::SimpleIdl::SimpleEnum _type_;
	convert(_env_, _j__type__, _type_);
	_env_->DeleteLocalRef(_j__type__);

	switch(_type_) {
		case ::SimpleIdl::EN_VAL_A:
		case ::SimpleIdl::EN_VAL_B:
		{
			jobject _j_foo_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum.foo);
			char * foo = convert<char>(_env_, _j_foo_);
			_env_->DeleteLocalRef(_j_foo_);
			_out_.foo(foo);
			break;
		}
		default:
		{
			jobject _j_bar_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.pojos.SimpleIdl.DefaultUnionFromEnum.bar);
			char * bar = convert<char>(_env_, _j_bar_);
			_env_->DeleteLocalRef(_j_bar_);
			_out_.bar(bar);
			break;
		}
	}

	_out_._d(_type_);
}

jboolean convert(JNIEnv * _env_, const ::CORBA::Boolean & _in_) {
	return (jboolean) _in_;
}

void convert(JNIEnv * _env_, const jboolean _in_, ::CORBA::Boolean & _out_) {
	_out_ = (::CORBA::Boolean) _in_;
}

jbyte convert(JNIEnv * _env_, const ::CORBA::Octet & _in_) {
	return (jbyte) _in_;
}

void convert(JNIEnv * _env_, const jbyte _in_, ::CORBA::Octet& _out_) {
	_out_ = (::CORBA::Octet) _in_;
}

jint convert(JNIEnv * _env_, const ::CORBA::Short & _in_) {
	return (jint) _in_;
}

void convert(JNIEnv * _env_, const jint _in_, ::CORBA::Short & _out_) {
	_out_ = (::CORBA::Short) _in_;
}

jint convert(JNIEnv * _env_, const ::CORBA::UShort & _in_) {
	return (jint) _in_;
}

void convert(JNIEnv * _env_, const jint _in_, ::CORBA::UShort & _out_) {
	_out_ = (::CORBA::UShort) _in_;
}

jint convert(JNIEnv * _env_, const ::CORBA::Long & _in_) {
	return (jint) _in_;
}

void convert(JNIEnv * _env_, const jint _in_, ::CORBA::Long & _out_) {
	_out_ = (::CORBA::Long) _in_;
}

jlong convert(JNIEnv * _env_, const ::CORBA::ULong & _in_) {
	return (jlong) _in_;
}

void convert(JNIEnv * _env_, const jlong _in_, ::CORBA::ULong & _out_) {
	_out_ = (::CORBA::ULong) _in_;
}

jlong convert(JNIEnv * _env_, const ::CORBA::LongLong & _in_) {
	return (jlong) _in_;
}

void convert(JNIEnv * _env_, const jint _in_, ::CORBA::LongLong & _out_) {
	_out_ = (::CORBA::LongLong) _in_;
}

jlong convert(JNIEnv * _env_, const ::CORBA::ULongLong & _in_) {
	return (jlong) _in_;
}

void convert(JNIEnv * _env_, const jlong _in_, ::CORBA::ULongLong & _out_) {
	_out_ = (::CORBA::ULongLong) _in_;
}

jfloat convert(JNIEnv * _env_, const ::CORBA::Float & _in_) {
	return (jfloat) _in_;
}

void convert(JNIEnv * _env_, const jfloat _in_, ::CORBA::Float & _out_) {
	_out_ = (::CORBA::Float) _in_;
}

jdouble convert(JNIEnv * _env_, const ::CORBA::Double & _in_) {
	return (jdouble) _in_;
}

void convert(JNIEnv * _env_, const jdouble _in_, ::CORBA::Double & _out_) {
	_out_ = (::CORBA::Double) _in_;
}

jchar convert(JNIEnv * _env_, const ::CORBA::Char & _in_) {
	return (jchar) _in_;
}

void convert(JNIEnv * _env_, const jchar _in_, ::CORBA::Char & _out_) {
	_out_ = (::CORBA::Char) _in_;
}

jobject convert(JNIEnv * _env_, const ::TAO::String_Manager & _in_) {
	return convert(_env_, _in_.in());
}

void convert(JNIEnv * _env_, const jobject _in_, ::TAO::String_Manager & _out_) {
	_out_ = convert<char>(_env_, _in_);
}

template<> jobject convert<const char>(JNIEnv * _env_, const char * _in_) {
	if (_in_) {
#ifdef CB_CORBA_USES_UTF_8
		return _env_->NewStringUTF(_in_);
#else // CB_CORBA_USES_UTF_8
		size_t _in_len_ = strlen(_in_);
		if (_in_len_ > 0) {
			// TODO: Better use UTF-16 (size of output buffer can be determined in advance)
			// but UTF-16 comes in LE and BE variants which could cause platform-dependent bugs.
			// Right now using UTF-8 with worst-case buffer size - for single byte input encoding.
			// For details see "modified UTF-8".
			size_t _out_len_ = _in_len_ * 3 + 2;
			char * _out_ = (char *) malloc(_out_len_);

			char * _in_buff_ = (char *) _in_;
			size_t _in_left_ = _in_len_;
			char * _out_buff_ = _out_;
			size_t _out_left_ = _out_len_;

			iconv_t _cd_ = iconv_open("UTF-8", "UTF-8");
			iconv(_cd_, &_in_buff_, &_in_left_, &_out_buff_, &_out_left_);
			_out_buff_[0] = 0x0;
			_out_buff_[1] = 0x0;
			iconv_close(_cd_);

			jobject _result_ = _env_->NewStringUTF(_out_);

			free(_out_);

			return _result_;
		} else {
			return _env_->NewStringUTF("");
		}
#endif // CB_CORBA_USES_UTF_8
	} else {
		return 0x0;
	}
}

template<> char * convert<char>(JNIEnv * _env_, const jobject _in_) {
	jstring _j_string_ = (jstring) _in_;
	if (_j_string_) {
		const char * _j_string_chars_ = _env_->GetStringUTFChars(_j_string_, nullptr);
#ifdef CB_CORBA_USES_UTF_8
		CORBA::String_var _result_(_j_string_chars_);
#else // CB_CORBA_USES_UTF_8
		// TODO: Better use UTF-16 and some improved estimation of output buffer size. Current approach
		// should work for single byte encoding that can be used as c strings.
		size_t _in_len_ = strlen(_j_string_chars_);
		size_t _out_len_ = _in_len_ + 1;
		char * _out_ = (char *) malloc(_out_len_);

		char * _in_buff_ = (char *) _j_string_chars_;
		size_t _in_left_ = _in_len_;
		char * _out_buff_ = _out_;
		size_t _out_left_ = _out_len_;

		iconv_t _cd_ = iconv_open("UTF-8", "UTF-8");
		iconv(_cd_, &_in_buff_, &_in_left_, &_out_buff_, &_out_left_);
		_out_buff_[0] = 0x0;
		iconv_close(_cd_);

		CORBA::String_var _result_((const char *) _out_);

		free(_out_);
#endif // CB_CORBA_USES_UTF_8
		_env_->ReleaseStringUTFChars(_j_string_, _j_string_chars_);
		return _result_._retn();
	} else {
		return 0x0;
	}
}

template<> jobject convertElement<::CORBA::Boolean>(JNIEnv * _env_, const ::CORBA::Boolean & _in_) {
	return _env_->NewObject(_jni_->java.lang.Boolean._cls_, _jni_->java.lang.Boolean._ctor_, _in_);
}

template<> void convertElement<::CORBA::Boolean>(JNIEnv * _env_, const jobject _in_, ::CORBA::Boolean & _out_) {
	_out_ = _env_->CallBooleanMethod(_in_, _jni_->java.lang.Boolean.booleanValue);
}

template<> jobject convertElement<::CORBA::Octet>(JNIEnv * _env_, const ::CORBA::Octet & _in_) {
	return _env_->NewObject(_jni_->java.lang.Byte._cls_, _jni_->java.lang.Byte._ctor_, _in_);
}

template<> void convertElement<::CORBA::Octet>(JNIEnv * _env_, const jobject _in_, ::CORBA::Octet& _out_) {
	_out_ = _env_->CallByteMethod(_in_, _jni_->java.lang.Byte.byteValue);
}

template<> jobject convertElement<::CORBA::Short>(JNIEnv * _env_, const ::CORBA::Short & _in_) {
	return _env_->NewObject(_jni_->java.lang.Integer._cls_, _jni_->java.lang.Integer._ctor_, _in_);
}

template<> void convertElement<::CORBA::Short>(JNIEnv * _env_, const jobject _in_, ::CORBA::Short & _out_) {
	_out_ = _env_->CallIntMethod(_in_, _jni_->java.lang.Integer.intValue);
}

template<> jobject convertElement<::CORBA::UShort>(JNIEnv * _env_, const ::CORBA::UShort & _in_) {
	return _env_->NewObject(_jni_->java.lang.Integer._cls_, _jni_->java.lang.Integer._ctor_, _in_);
}

template<> void convertElement<::CORBA::UShort>(JNIEnv * _env_, const jobject _in_, ::CORBA::UShort & _out_) {
	_out_ = _env_->CallIntMethod(_in_, _jni_->java.lang.Integer.intValue);
}

template<> jobject convertElement<CORBA::Long>(JNIEnv * _env_, const CORBA::Long & _in_) {
	return _env_->NewObject(_jni_->java.lang.Integer._cls_, _jni_->java.lang.Integer._ctor_, _in_);
}

template<> void convertElement<CORBA::Long>(JNIEnv * _env_, const jobject _in_, CORBA::Long & _out_) {
	_out_ = _env_->CallIntMethod(_in_, _jni_->java.lang.Integer.intValue);
}

template<> jobject convertElement<::CORBA::ULong>(JNIEnv * _env_, const ::CORBA::ULong & _in_) {
	return _env_->NewObject(_jni_->java.lang.Long._cls_, _jni_->java.lang.Long._ctor_, _in_);
}

template<> void convertElement<::CORBA::ULong>(JNIEnv * _env_, const jobject _in_, ::CORBA::ULong & _out_) {
	_out_ = _env_->CallLongMethod(_in_, _jni_->java.lang.Long.longValue);
}

template<> jobject convertElement<::CORBA::LongLong>(JNIEnv * _env_, const ::CORBA::LongLong & _in_) {
	return _env_->NewObject(_jni_->java.lang.Long._cls_, _jni_->java.lang.Long._ctor_, _in_);
}

template<> void convertElement<::CORBA::LongLong>(JNIEnv * _env_, const jobject _in_, ::CORBA::LongLong & _out_) {
	_out_ = _env_->CallLongMethod(_in_, _jni_->java.lang.Long.longValue);
}

template<> jobject convertElement<::CORBA::ULongLong>(JNIEnv * _env_, const ::CORBA::ULongLong & _in_) {
	return _env_->NewObject(_jni_->java.lang.Long._cls_, _jni_->java.lang.Long._ctor_, _in_);
}

template<> void convertElement<::CORBA::ULongLong>(JNIEnv * _env_, const jobject _in_, ::CORBA::ULongLong & _out_) {
	_out_ = _env_->CallLongMethod(_in_, _jni_->java.lang.Long.longValue);
}

template<> jobject convertElement<::CORBA::Float>(JNIEnv * _env_, const ::CORBA::Float & _in_) {
	return _env_->NewObject(_jni_->java.lang.Float._cls_, _jni_->java.lang.Float._ctor_, _in_);
}

template<> void convertElement<::CORBA::Float>(JNIEnv * _env_, const jobject _in_, ::CORBA::Float & _out_) {
	_out_ = _env_->CallFloatMethod(_in_, _jni_->java.lang.Float.floatValue);
}

template<> jobject convertElement<::CORBA::Double>(JNIEnv * _env_, const ::CORBA::Double & _in_) {
	return _env_->NewObject(_jni_->java.lang.Double._cls_, _jni_->java.lang.Double._ctor_, _in_);
}

template<> void convertElement<::CORBA::Double>(JNIEnv * _env_, const jobject _in_, ::CORBA::Double & _out_) {
	_out_ = _env_->CallDoubleMethod(_in_, _jni_->java.lang.Double.doubleValue);
}

template<> jobject convertElement<::CORBA::Char>(JNIEnv * _env_, const ::CORBA::Char & _in_) {
	return _env_->NewObject(_jni_->java.lang.Character._cls_, _jni_->java.lang.Character._ctor_, _in_);
}

template<> void convertElement<::CORBA::Char>(JNIEnv * _env_, const jobject _in_, ::CORBA::Char & _out_) {
	_out_ = _env_->CallCharMethod(_in_, _jni_->java.lang.Character.charValue);
}

} /* namespace corbabinding */
