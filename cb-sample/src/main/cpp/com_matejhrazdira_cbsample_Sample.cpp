#include "com_matejhrazdira_cbsample_Sample.h"

#include "../cpp-cb/corbabinding.h"

using namespace corbabinding;

JNIEXPORT void JNICALL Java_com_matejhrazdira_cbsample_Sample_initCache
  (JNIEnv * env, jclass cls) {
	init(env);
}

JNIEXPORT jobject JNICALL Java_com_matejhrazdira_cbsample_Sample_copyUnion
  (JNIEnv * env, jclass cls, jobject input) {
	SimpleIdl::SimpleUnion tmp;
	convert(env, input, tmp);
	return convert(env, tmp);
}
