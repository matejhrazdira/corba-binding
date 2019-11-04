#ifndef CORBABINDING_H_
#define CORBABINDING_H_



#include "JniCache.h"

namespace corbabinding {

extern JniCache * _jni_;

void init(JNIEnv * _env_);

template<typename T> jobject convert(JNIEnv * _env_, T * _in_);
template<typename T> T * convert(JNIEnv * _env_, const jobject _in_);


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

jobject convert(JNIEnv * _env_, const ::CORBA::String_var & _in_);
void convert(JNIEnv * _env_, const jobject _in_, ::CORBA::String_var & _out_);

jthrowable convert(JNIEnv * _env_, const ::CORBA::Exception & _in_);

template<typename T> jobject convert(JNIEnv * _env_, const T & _in_);
template<typename T> void convert(JNIEnv * _env_, const jobject _in_, T & _out_);

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

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, T(& _in_)[SIZE], jobjectArray & _out_) {
	if (SIZE > 0) {
		jobject _element_ = convert(_env_, _in_[0]);

		jclass _cls_ = _env_->GetObjectClass(_element_);
		_out_ = _env_->NewObjectArray(SIZE, _cls_, nullptr);
		_env_->DeleteLocalRef(_cls_);

		_env_->SetObjectArrayElement(_out_, 0, _element_);

		for (size_t i = 1; i < SIZE; i++) {
			_element_ = convert(_env_, _in_[i]);
			_env_->SetObjectArrayElement(_out_, i, _element_);
			_env_->DeleteLocalRef(_element_);
		}
	} else {
		T dummy{};
		jobject jdummy = convert(_env_, dummy);
		jclass _cls_ = _env_->GetObjectClass(jdummy);
		_env_->NewObjectArray(SIZE, _cls_, nullptr);
		_env_->DeleteLocalRef(_cls_);
		_env_->DeleteLocalRef(jdummy);
	}
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jobjectArray _in_, T(& _out_)[SIZE]) {
	for (size_t i = 0; i < SIZE; i++) {
		jobject _element_ = _env_->GetObjectArrayElement(_in_, i);
		if (_element_) {
			convert(_env_, _element_, _out_[i]);
		} else {
			_out_[i] = {};
		}
		_env_->DeleteLocalRef(_element_);
	}
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jbooleanArray & _out_) {
	_out_ = _env_->NewBooleanArray(SIZE);
	jboolean * _element_s = _env_->GetBooleanArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jboolean) _in_[i];
	}
	_env_->ReleaseBooleanArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jbooleanArray _in_, T(& _out_)[SIZE]) {
	jboolean * _element_s = _env_->GetBooleanArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseBooleanArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jbyteArray & _out_) {
	_out_ = _env_->NewByteArray(SIZE);
	jbyte * _element_s = _env_->GetByteArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jbyte) _in_[i];
	}
	_env_->ReleaseByteArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jbyteArray _in_, T(& _out_)[SIZE]) {
	jbyte * _element_s = _env_->GetByteArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseByteArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jcharArray & _out_) {
	_out_ = _env_->NewCharArray(SIZE);
	jchar * _element_s = _env_->GetCharArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jchar) _in_[i];
	}
	_env_->ReleaseCharArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jcharArray _in_, T(& _out_)[SIZE]) {
	jchar * _element_s = _env_->GetCharArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseCharArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jintArray & _out_) {
	_out_ = _env_->NewIntArray(SIZE);
	jint * _element_s = _env_->GetIntArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jint) _in_[i];
	}
	_env_->ReleaseIntArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jintArray _in_, T(& _out_)[SIZE]) {
	jint * _element_s = _env_->GetIntArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseIntArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jlongArray & _out_) {
	_out_ = _env_->NewLongArray(SIZE);
	jlong * _element_s = _env_->GetLongArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jlong) _in_[i];
	}
	_env_->ReleaseLongArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jlongArray _in_, T(& _out_)[SIZE]) {
	jlong * _element_s = _env_->GetLongArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseLongArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jfloatArray & _out_) {
	_out_ = _env_->NewFloatArray(SIZE);
	jfloat * _element_s = _env_->GetFloatArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jfloat) _in_[i];
	}
	_env_->ReleaseFloatArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jfloatArray _in_, T(& _out_)[SIZE]) {
	jfloat * _element_s = _env_->GetFloatArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseFloatArrayElements(_in_, _element_s, JNI_ABORT);
}

template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const T(& _in_)[SIZE], jdoubleArray & _out_) {
	_out_ = _env_->NewDoubleArray(SIZE);
	jdouble * _element_s = _env_->GetDoubleArrayElements(_out_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_element_s[i] = (jdouble) _in_[i];
	}
	_env_->ReleaseDoubleArrayElements(_out_, _element_s, 0);
}
template<typename T, std::size_t SIZE> void convertArray(JNIEnv * _env_, const jdoubleArray _in_, T(& _out_)[SIZE]) {
	jdouble * _element_s = _env_->GetDoubleArrayElements(_in_, nullptr);
	for (size_t i = 0; i < SIZE; i++) {
		_out_[i] = (T) _element_s[i];
	}
	_env_->ReleaseDoubleArrayElements(_in_, _element_s, JNI_ABORT);
}

jobject getVarObject(JNIEnv * _env_, const jobject _var_);
void setVarObject(JNIEnv * _env_, jobject _var_, jobject _value_);

/** @Deprecated */
template<typename T> T * getVarPtr(JNIEnv * _env_, const jobject _var_) {
	jobject _value_ = getVarObject(_env_, _var_);
	T * _result_ = convert<T>(_env_, _value_);
	_env_->DeleteLocalRef(_value_);
	return _result_;
}

/** @Deprecated */
template<typename T> T getVarValue(JNIEnv * _env_, const jobject _var_) {
	jobject _value_ = getVarObject(_env_, _var_);
	T _result_;
	convertElement(_env_, _value_, _result_);
	_env_->DeleteLocalRef(_value_);
	return _result_;
}

template<typename T> void convertVar(JNIEnv * _env_, const jobject _var_, T & _out_) {
	jobject _value_ = getVarObject(_env_, _var_);
	convertElement(_env_, _value_, _out_);
	_env_->DeleteLocalRef(_value_);
}

template<typename T> void setVar(JNIEnv * _env_, jobject _var_, const T & _in_) {
	jobject _tmp_ = convertElement(_env_, _in_);
	setVarObject(_env_, _var_, _tmp_);
	_env_->DeleteLocalRef(_tmp_);
}

} /* namespace corbabinding */

#endif /* CORBABINDING_H_ */
