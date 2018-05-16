#include "jniimpl.h"

#include "corbabinding.h"

using namespace corbabinding;

JNIEXPORT void JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface__1dispose_1(JNIEnv * _env_, jobject _this_) {
	::SimpleIdl::SimpleIdlInterface_var tmp = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	_env_->SetLongField(_this_, _jni_->com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface._native_address_, 0x0);
}

JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someStringMethod(JNIEnv * _env_, jobject _this_) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return (jobject) 0x0;
	}

	try {
		::CORBA::String_var _result_ = _c_this_->someStringMethod();

		return convert(_env_, _result_);

	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return (jobject) 0x0;
}

JNIEXPORT jlong JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_methodWithTypedefedValues(JNIEnv * _env_, jobject _this_, jlong timeArg, jobject structArg) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return (jlong) 0x0;
	}

	try {
		::SimpleIdl::time _c_timeArg_;
		convert(_env_, timeArg, _c_timeArg_);

		::SimpleIdl::typedefedStruct_var  _c_structArg_;

		::SimpleIdl::time _result_ = _c_this_->methodWithTypedefedValues(_c_timeArg_, _c_structArg_);

		setVar(_env_, structArg, _c_structArg_.ptr());

		return convert(_env_, _result_);

	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return (jlong) 0x0;
}

JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someMethodWithArgs(JNIEnv * _env_, jobject _this_, jobject strArg, jlong ullArg, jobject structArg) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return (jobject) 0x0;
	}

	try {
		::CORBA::String_var _c_strArg_;
		convert(_env_, strArg, _c_strArg_);

		::CORBA::ULongLong _c_ullArg_;
		convert(_env_, ullArg, _c_ullArg_);

		::SimpleIdl::SimpleStruct_var  _c_structArg_;

		::SimpleIdl::SimpleEnum _result_ = _c_this_->someMethodWithArgs(_c_strArg_, _c_ullArg_, _c_structArg_);

		setVar(_env_, structArg, _c_structArg_.ptr());

		return convert(_env_, _result_);

	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return (jobject) 0x0;
}

JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someMethodWithArgsThatThrows(JNIEnv * _env_, jobject _this_, jobject strArg, jlong ullArg, jobject structArg) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return (jobject) 0x0;
	}

	try {
		::CORBA::String_var _c_strArg_;
		convert(_env_, strArg, _c_strArg_);

		::CORBA::ULongLong _c_ullArg_;
		convert(_env_, ullArg, _c_ullArg_);

		::SimpleIdl::SimpleStruct _c_structArg_;
		convertVar(_env_, structArg, _c_structArg_);

		::SimpleIdl::SimpleStruct_var  _result_ = _c_this_->someMethodWithArgsThatThrows(_c_strArg_, _c_ullArg_, _c_structArg_);

		setVar(_env_, structArg, _c_structArg_);

		return convert(_env_, _result_.ptr());

	} catch (const ::SimpleIdl::ExceptionWithMembers & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return (jobject) 0x0;
}

JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_getTimeSequence(JNIEnv * _env_, jobject _this_) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return (jobject) 0x0;
	}

	try {
		::SimpleIdl::TimeSequence_var  _result_ = _c_this_->getTimeSequence();

		return convert(_env_, _result_.ptr());

	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return (jobject) 0x0;
}

JNIEXPORT void JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_throwsNestedException(JNIEnv * _env_, jobject _this_) {
	::SimpleIdl::SimpleIdlInterface_ptr _c_this_ = convert<::SimpleIdl::SimpleIdlInterface>(_env_, _this_);
	if (!_c_this_) {
		_env_->Throw((jthrowable) _env_->NewObject(_jni_->_impl_._already_disposed_exception_._cls_, _jni_->_impl_._already_disposed_exception_._ctor_));
		return;
	}

	try {
		_c_this_->throwsNestedException();

	} catch (const ::SimpleIdl::SimpleIdlInterface::NestedException & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	} catch (const ::CORBA::Exception & e) {
		_env_->Throw((jthrowable) convert(_env_, e));
	}
	return;
}

