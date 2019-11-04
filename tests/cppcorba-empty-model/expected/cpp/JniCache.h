#ifndef JNICACHE_H_
#define JNICACHE_H_

#include <jni.h>
#include <vector>

namespace corbabinding {

struct JniCache {
	

	struct {
		struct {
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID booleanValue;
			} Boolean;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID byteValue;
			} Byte;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID intValue;
			} Integer;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID longValue;
			} Long;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID floatValue;
			} Float;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID doubleValue;
			} Double;
			struct {
				jclass _cls_;
				jmethodID _ctor_;
				jmethodID charValue;
			} Character;
		} lang;
		struct {
			struct {
				jclass _cls_;
				jmethodID _ctor_;
			} ArrayList;
			struct {
				jmethodID add;
				jmethodID get;
				jmethodID size;
			} List;
		} util;
	} java;
	struct {
		struct {
			jclass _cls_;
			jmethodID _ctor_;
			jmethodID _get_;
			jmethodID _set_;
		} _var_;
		struct {
			jclass _cls_;
			jmethodID _ctor_;
		} _corba_exception_;
		struct {
			jclass _cls_;
			jmethodID _ctor_;
		} _already_disposed_exception_;
		struct {
			jmethodID _callback_;
		} _event_consumer_;
	} _impl_;
	struct {

	} _client_;

	JniCache(JNIEnv * _env_);
	virtual ~JniCache();
};

} /* namespace corbabinding */

#endif /* JNICACHE_H_ */
