#include "corbabinding.h"

namespace corbabinding {

JniCache * _jni_;

void init(JNIEnv * _env_) {
	if (!_jni_) {
		_jni_ = new JniCache(_env_);
	}
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
		return _env_->NewStringUTF(_in_);
	} else {
		return 0x0;
	}
}

template<> char * convert<char>(JNIEnv * _env_, const jobject _in_) {
	jstring _j_string_ = (jstring) _in_;
	if (_j_string_) {
		const char * _j_string_chars_ = _env_->GetStringUTFChars(_j_string_, nullptr);
		CORBA::String_var _result_(_j_string_chars_);
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
