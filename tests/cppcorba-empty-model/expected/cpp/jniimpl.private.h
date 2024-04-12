#include <jni.h>

#ifndef JNIIMPL_PRIVATE_H_
#define JNIIMPL_PRIVATE_H_

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_CorbaProvider_init(JNIEnv *, jobject, jobjectArray);

JNIEXPORT void JNICALL Java_CorbaProvider_setOrbRelativeRtTimeoutImpl(JNIEnv *, jobject, jlong, jlong);

JNIEXPORT jobject JNICALL Java_CorbaProvider_setObjectRelativeRtTimeoutImpl(JNIEnv *, jobject, jlong, jobject, jlong);

JNIEXPORT jobject JNICALL Java_CorbaProvider_resolveImpl(JNIEnv *, jobject, jlong, jstring, jstring);

JNIEXPORT void JNICALL Java_CorbaProvider_disposeImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jlong JNICALL Java_EventService_init(JNIEnv *, jobject, jlong, jstring);

JNIEXPORT void JNICALL Java_EventService_connectImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jboolean JNICALL Java_EventService_isAliveImpl(JNIEnv *, jobject, jlong);

JNIEXPORT void JNICALL Java_EventService_disposeImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jlong JNICALL Java_EventConsumer_connectConsumer(JNIEnv *, jobject, jlong, jint, jstring);

JNIEXPORT void JNICALL Java_EventConsumer_disposeImpl(JNIEnv *, jobject, jlong);

JNIEXPORT jlong JNICALL Java_EventProducer_connectProducer(JNIEnv *, jobject, jlong, jint, jint, jstring);

JNIEXPORT void JNICALL Java_EventProducer_pushEventImpl(JNIEnv *, jobject, jlong, jobject);

JNIEXPORT void JNICALL Java_EventProducer_disposeImpl(JNIEnv *, jobject, jlong);

#ifdef __cplusplus
}
#endif

#endif /* JNIIMPL_PRIVATE_H_ */
