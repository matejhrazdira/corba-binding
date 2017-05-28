#ifndef JNIIMPL_H_
#define JNIIMPL_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface__1dispose_1(JNIEnv * _env_, jobject _this_);
JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someStringMethod(JNIEnv * _env_, jobject _this_);
JNIEXPORT jlong JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_methodWithTypedefedValues(JNIEnv * _env_, jobject _this_, jlong timeArg, jobject structArg);
JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someMethodWithArgs(JNIEnv * _env_, jobject _this_, jobject strArg, jlong ullArg, jobject structArg);
JNIEXPORT jobject JNICALL Java_com_matejhrazdira_pojos_SimpleIdl_SimpleIdlInterface_someMethodWithArgsThatThrows(JNIEnv * _env_, jobject _this_, jobject strArg, jlong ullArg, jobject structArg);

#ifdef __cplusplus
}
#endif

#endif /* JNIIMPL_H_ */
