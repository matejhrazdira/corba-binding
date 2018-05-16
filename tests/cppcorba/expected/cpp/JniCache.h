#ifndef JNICACHE_H_
#define JNICACHE_H_

#include <jni.h>
#include <vector>

namespace corbabinding {

struct JniCache {
	
	struct {
		struct {
			struct {
				struct {
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID someMember;
					} IncludedStruct;
				} IncludedIdl;
				struct {
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID stringMember;
						jfieldID intMember;
						jfieldID typedefedMember;
						jfieldID uintMember;
						jfieldID longMember;
						jfieldID ulongMember;
						jfieldID includedMember;
						jfieldID fullyScopedIncludedMember;
						jfieldID duplicate1;
						jfieldID duplicate2;
					} SimpleStruct;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID typedefedArray;
						jfieldID directArray;
						jfieldID typedefTypedefedArray;
					} StructWithArrays;
					struct {
						jclass _cls_;
						std::vector<jfieldID> _values_;
						jmethodID _ordinal_;
					} SimpleEnum;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID _type_;
						jfieldID strInUnion;
						jfieldID ullInUnion;
						jfieldID simpleStructsInUnion;
					} SimpleUnion;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID _type_;
						jfieldID foo;
						jfieldID bar;
					} WeirdUnion;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID _type_;
						jfieldID foo;
						jfieldID bar;
					} DefaultUnionFromEnum;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
					} NoMemberException;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID someStringMember;
					} ExceptionWithMembers;
					struct {
						jclass _cls_;
						jmethodID _ctor_;
						jfieldID _native_address_;
						struct {
							jclass _cls_;
							jmethodID _ctor_;
						} NestedException;
					} SimpleIdlInterface;
				} SimpleIdl;
			} pojos;
		} matejhrazdira;
	} com;
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

	JniCache(JNIEnv * _env_);
	virtual ~JniCache();
};

} /* namespace corbabinding */

#endif /* JNICACHE_H_ */
