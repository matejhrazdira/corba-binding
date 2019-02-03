#include <jni.h>

#ifndef JNIIMPL_PRIVATE_H_
#define JNIIMPL_PRIVATE_H_

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_CorbaProvider_init(JNIEnv *, jobject, jobjectArray);

JNIEXPORT void JNICALL Java_CorbaProvider_connectEventServiceImpl(JNIEnv * env, jobject thiz, jlong jnativeWrapper, jstring jeventServiceStr);

JNIEXPORT jboolean JNICALL Java_CorbaProvider_eventServiceAliveImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jobject JNICALL Java_CorbaProvider_resolveImpl(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT void JNICALL Java_CorbaProvider_disposeImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jlong JNICALL Java_EventConsumer_connectConsumer(JNIEnv *, jobject, jlong, jint, jstring);

JNIEXPORT void JNICALL Java_EventConsumer_disposeImpl(JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif

#endif /* JNIIMPL_PRIVATE_H_ */
