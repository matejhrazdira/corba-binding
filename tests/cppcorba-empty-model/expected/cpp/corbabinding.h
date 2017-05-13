#ifndef CORBABINDING_H_
#define CORBABINDING_H_


#include "JniCache.h"

namespace corbabinding {

extern JniCache * _jni_;

void init(JNIEnv * _env_);

jboolean convert(JNIEnv * _env_, const ::CORBA::Boolean & _in_);
void convert(JNIEnv * _env_, const jboolean _in_, ::CORBA::Boolean & _out_);

jbyte convert(JNIEnv * _env_, const ::CORBA::Octet & _in_);
void convert(JNIEnv * _env_, const jbyte _in_, ::CORBA::Octet& _out_);

jint convert(JNIEnv * _env_, const ::CORBA::Short & _in_);
void convert(JNIEnv * _env_, const jint _in_, ::CORBA::Short & _out_);

jint convert(JNIEnv * _env_, const ::CORBA::UShort & _in_);
void convert(JNIEnv * _env_, const jint _in_, ::CORBA::UShort & _out_);

jint convert(JNIEnv * _env_, const ::CORBA::Long & _in_);
void convert(JNIEnv * _env_, const jint _in_, ::CORBA::Long & _out_);

jlong convert(JNIEnv * _env_, const ::CORBA::ULong & _in_);
void convert(JNIEnv * _env_, const jlong _in_, ::CORBA::ULong & _out_);

jlong convert(JNIEnv * _env_, const ::CORBA::LongLong & _in_);
void convert(JNIEnv * _env_, const jint _in_, ::CORBA::LongLong & _out_);

jlong convert(JNIEnv * _env_, const ::CORBA::ULongLong & _in_);
void convert(JNIEnv * _env_, const jlong _in_, ::CORBA::ULongLong & _out_);

jfloat convert(JNIEnv * _env_, const ::CORBA::Float & _in_);
void convert(JNIEnv * _env_, const jfloat _in_, ::CORBA::Float & _out_);

jdouble convert(JNIEnv * _env_, const ::CORBA::Double & _in_);
void convert(JNIEnv * _env_, const jdouble _in_, ::CORBA::Double & _out_);

jchar convert(JNIEnv * _env_, const ::CORBA::Char & _in_);
void convert(JNIEnv * _env_, const jchar _in_, ::CORBA::Char & _out_);

jobject convert(JNIEnv * _env_, const ::TAO::String_Manager & _in_);
void convert(JNIEnv * _env_, const jobject _in_, ::TAO::String_Manager & _out_);

template<typename T> jobject convert(JNIEnv * _env_, T * _in_) {
	if (_in_) {
		return convert(_env_, *_in_);
	} else {
		return 0x0;
	}
}
template<typename T> T * convert(JNIEnv * _env_, const jobject _in_) {
	if (_in_) {
		typename T::_var_type _result_ = new T();
		convert(_env_, _in_, _result_.inout());
		return _result_._retn();
	} else {
		return 0x0;
	}
}

template<> jobject convert<const char>(JNIEnv * _env_, const char * _in_);
template<> char * convert<char>(JNIEnv * _env_, const jobject _in_);

template<typename T> jobject convertElement(JNIEnv * _env_, const T & _in_) {
	return convert(_env_, _in_);
}
template<typename T> void convertElement(JNIEnv * _env_, const jobject _in_, T & _out_) {
	convert(_env_, _in_, _out_);
}

template<typename T> jobject convertElement(JNIEnv * _env_, const TAO::details::string_const_sequence_element<T> & _in_) {
	return convert(_env_, _in_.in());
}
template<typename T> void convertElement(JNIEnv * _env_, const jobject _in_, TAO::details::string_sequence_element<T> _out_) {
	_out_ = convert<char>(_env_, _in_);
}

template<> jobject convertElement<::CORBA::Boolean>(JNIEnv * _env_, const ::CORBA::Boolean & _in_);
template<> void convertElement<::CORBA::Boolean>(JNIEnv * _env_, const jobject _in_, ::CORBA::Boolean & _out_);

template<> jobject convertElement<::CORBA::Octet>(JNIEnv * _env_, const ::CORBA::Octet & _in_);
template<> void convertElement<::CORBA::Octet>(JNIEnv * _env_, const jobject _in_, ::CORBA::Octet& _out_);

template<> jobject convertElement<::CORBA::Short>(JNIEnv * _env_, const ::CORBA::Short & _in_);
template<> void convertElement<::CORBA::Short>(JNIEnv * _env_, const jobject _in_, ::CORBA::Short & _out_);

template<> jobject convertElement<::CORBA::UShort>(JNIEnv * _env_, const ::CORBA::UShort & _in_);
template<> void convertElement<::CORBA::UShort>(JNIEnv * _env_, const jobject _in_, ::CORBA::UShort & _out_);

template<> jobject convertElement<::CORBA::Long>(JNIEnv * _env_, const ::CORBA::Long & _in_);
template<> void convertElement<::CORBA::Long>(JNIEnv * _env_, const jobject _in_, ::CORBA::Long & _out_);

template<> jobject convertElement<::CORBA::ULong>(JNIEnv * _env_, const ::CORBA::ULong & _in_);
template<> void convertElement<::CORBA::ULong>(JNIEnv * _env_, const jobject _in_, ::CORBA::ULong & _out_);

template<> jobject convertElement<::CORBA::LongLong>(JNIEnv * _env_, const ::CORBA::LongLong & _in_);
template<> void convertElement<::CORBA::LongLong>(JNIEnv * _env_, const jobject _in_, ::CORBA::LongLong & _out_);

template<> jobject convertElement<::CORBA::ULongLong>(JNIEnv * _env_, const ::CORBA::ULongLong & _in_);
template<> void convertElement<::CORBA::ULongLong>(JNIEnv * _env_, const jobject _in_, ::CORBA::ULongLong & _out_);

template<> jobject convertElement<::CORBA::Float>(JNIEnv * _env_, const ::CORBA::Float & _in_);
template<> void convertElement<::CORBA::Float>(JNIEnv * _env_, const jobject _in_, ::CORBA::Float & _out_);

template<> jobject convertElement<::CORBA::Double>(JNIEnv * _env_, const ::CORBA::Double & _in_);
template<> void convertElement<::CORBA::Double>(JNIEnv * _env_, const jobject _in_, ::CORBA::Double & _out_);

template<> jobject convertElement<::CORBA::Char>(JNIEnv * _env_, const ::CORBA::Char & _in_);
template<> void convertElement<::CORBA::Char>(JNIEnv * _env_, const jobject _in_, ::CORBA::Char & _out_);

template<typename T> jobject convert(JNIEnv * _env_, const T & _in_) {
	int length = _in_.length();
	jobject _result_ = _env_->NewObject(_jni_->java.util.ArrayList._cls_, _jni_->java.util.ArrayList._ctor_, length);
	for (int i = 0; i < length; i++) {
		jobject _element_ = convertElement(_env_, _in_[i]);
		_env_->CallBooleanMethod(_result_, _jni_->java.util.List.add, _element_);
		_env_->DeleteLocalRef(_element_);
	}
	return _result_;
}
template<typename T> void convert(JNIEnv * _env_, const jobject _in_, T & _out_) {
	int length = _env_->CallIntMethod(_in_, _jni_->java.util.List.size);
	_out_.length(length);
	for (int i = 0; i < length; i++) {
		jobject _element_ = _env_->CallObjectMethod(_in_, _jni_->java.util.List.get, i);
		convertElement(_env_, _element_, _out_[i]);
		_env_->DeleteLocalRef(_element_);
	}
}

} /* namespace corbabinding */

#endif /* CORBABINDING_H_ */
