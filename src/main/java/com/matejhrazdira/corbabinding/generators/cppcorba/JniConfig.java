/*
 * Copyright (C) 2016 Matej Hrazdira.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.matejhrazdira.corbabinding.generators.cppcorba;

import com.matejhrazdira.corbabinding.generators.java.TemplateRenderer;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaTemplateProjection;

import java.util.HashMap;
import java.util.Map;

public class JniConfig {

	public static final String INDENTATION = "\t";

	public static final String CONVERSION_FUNCTION = "convert";
	public static final String ARRAY_CONVERSION_FUNCTION = "convertArray";

	public static final String CONVERSION_IN_ARG = "_in_";
	public static final String CONVERSION_OUT_ARG = "_out_";
	public static final String ARG_JNI_ENV = "_env_";
	public static final String JNI_CACHE_VAR = "_jni_";
	public static final String JNI_CACHE_CLASS = "_cls_";
	public static final String JNI_CACHE_CTOR = "_ctor_";
	public static final String RESULT_VAR = "_result_";
	public static final String JNI_CTOR_NAME = "<init>";

	public static final String JNI_CACHE_IMPL = "_impl_";
	public static final String JNI_CACHE_ITEM_VAR = "_var_";
	public static final String JNI_CACHE_VAR_GET = "_get_";
	public static final String JNI_CACHE_VAR_SET = "_set_";
	public static final String JNI_CACHE_ITEM_CORBA_EXCEPTION = "_corba_exception_";
	public static final String JNI_CACHE_ITEM_ALREADY_DISPOSED_EXCEPTION = "_already_disposed_exception_";
	public static final String JNI_CACHE_ITEM_EVENT_CONSUMER = "_event_consumer_";
	public static final String JNI_CACHE_EVENT_CONSUMER_CALLBACK = "_callback_";

	public static final String IDL_SUFFIX = ".idl";
	public static final String JNI_CACHE_H = "JniCache.h";
	public static final String JNI_CACHE_CPP = "JniCache.cpp";
	public static final String JNI_IMPL_H = "jniimpl.h";
	public static final String JNI_IMPL_CPP = "jniimpl.cpp";
	public static final String JNI_IMPL_PRIVATE_H = "jniimpl.private.h";
	public static final String JNI_IMPL_PRIVATE_CPP = "jniimpl.private.cpp";
	public static final String CB_LIB_H = "corbabindinglib.h";
	public static final String CB_LIB_CPP = "corbabindinglib.cpp";
	public static final String CORBABINDING_H = "corbabinding.h";
	public static final String CORBABINDING_CPP = "corbabinding.cpp";

	public static final String TYPE_CACHE_INTERFACE_TABLE = "mInterfaceTable";
	public static final String TYPE_CACHE_ANY_TABLE = "mAnyTable";
	public static final String TYPE_CACHE_TO_ANY_TABLE = "mToAnyTable";
	public static final String TYPE_CACHE_CONVERT_OBJ = "convertObject";
	public static final String TYPE_CACHE_CONVERT_ANY = "convertAny";
	public static final String TYPE_CACHE_CONVERT_TO_ANY = "convertToAny";

	public static final String UTF_8_DEFINE = "CB_CORBA_USES_UTF_8";

	public static Map<String, String> getStringStringMap(final String corbaEncoding, final JavaTemplateProjection projection) {
		JniScopedRenderer jniScopedRenderer = new JniScopedRenderer();
		JniImplMethodNameRenderer jniImplMethodNameRenderer = new JniImplMethodNameRenderer();
		final Map<String, String> res = new HashMap<>();
		res.put("$$$ENV$$$", ARG_JNI_ENV);
		res.put("$$$JNI$$$", JNI_CACHE_VAR);
		res.put("$$$CLS$$$", JNI_CACHE_CLASS);
		res.put("$$$CONVERT$$$", CONVERSION_FUNCTION);
		res.put("$$$CONVERT_ARRAY$$$", ARRAY_CONVERSION_FUNCTION);
		res.put("$$$IN_ARG$$$", CONVERSION_IN_ARG);
		res.put("$$$OUT_ARG$$$", CONVERSION_OUT_ARG);
		res.put("$$$CTOR$$$", JNI_CACHE_CTOR);
		res.put("$$$RESULT$$$", RESULT_VAR);
		res.put("$$$CACHE_IMPL$$$", JNI_CACHE_IMPL);
		res.put("$$$CACHE_VAR$$$", JNI_CACHE_ITEM_VAR);
		res.put("$$$VAR_TYPE_SIG$$$", jniScopedRenderer.render(projection.varClass));
		res.put("$$$VAR_GET$$$", JNI_CACHE_VAR_GET);
		res.put("$$$VAR_SET$$$", JNI_CACHE_VAR_SET);
		res.put("$$$VAR_GET_NAME$$$", TemplateRenderer.VAR_GET);
		res.put("$$$VAR_SET_NAME$$$", TemplateRenderer.VAR_SET);
		res.put("$$$CACHE_CORBA_EXCEPTION$$$", JNI_CACHE_ITEM_CORBA_EXCEPTION);
		res.put("$$$CACHE_ALREADY_DISPOSED_EXCEPTION$$$", JNI_CACHE_ITEM_ALREADY_DISPOSED_EXCEPTION);
		res.put("$$$CORBA_EXCEPTION_TYPE_SIG$$$", jniScopedRenderer.render(projection.corbaExceptionClass));
		res.put("$$$ALREADY_DISPOSED_EXCEPTION_TYPE_SIG$$$", jniScopedRenderer.render(projection.alreadyDisposedExceptionClass));
		res.put("$$$CACHE_EVENT_CONSUMER$$$", JNI_CACHE_ITEM_EVENT_CONSUMER);
		res.put("$$$EVENT_CONSUMER_CALLBACK$$$", JNI_CACHE_EVENT_CONSUMER_CALLBACK);
		res.put("$$$EVENT_CONSUMER_TYPE_SIG$$$", jniScopedRenderer.render(projection.eventConsumerClass));
		res.put("$$$EVENT_CONSUMER_CALLBACK_NAME$$$", TemplateRenderer.EVENT_CONSUMER_CALLBACK);
		res.put("$$$CORBA_PROVIDER_JNI_SIG$$$", jniImplMethodNameRenderer.render(projection.corbaProviderClass));
		res.put("$$$EVENT_SERVICE_JNI_SIG$$$", jniImplMethodNameRenderer.render(projection.eventServiceClass));
		res.put("$$$EVENT_CONSUMER_JNI_SIG$$$", jniImplMethodNameRenderer.render(projection.eventConsumerClass));
		res.put("$$$EVENT_PRODUCER_JNI_SIG$$$", jniImplMethodNameRenderer.render(projection.eventProducerClass));
		res.put("$$$CONVERT_ELEMENT$$$", CONVERSION_FUNCTION + "Element");
		res.put("$$$ELEMENT$$$", "_element_");
		res.put("$$$JSTRING_CHARS_VAR$$$", "_j_string_chars_");
		res.put("$$$JSTRING_VAR$$$", "_j_string_");
		res.put("$$$TYPE_CACHE_INTERFACE_TABLE$$$", TYPE_CACHE_INTERFACE_TABLE);
		res.put("$$$TYPE_CACHE_CONVERT_OBJ$$$", TYPE_CACHE_CONVERT_OBJ);
		res.put("$$$TYPE_CACHE_ANY_TABLE$$$", TYPE_CACHE_ANY_TABLE);
		res.put("$$$TYPE_CACHE_CONVERT_ANY$$$", TYPE_CACHE_CONVERT_ANY);
		res.put("$$$TYPE_CACHE_TO_ANY_TABLE$$$", TYPE_CACHE_TO_ANY_TABLE);
		res.put("$$$TYPE_CACHE_CONVERT_TO_ANY$$$", TYPE_CACHE_CONVERT_TO_ANY);
		res.put("$$$CORBA_ENCODING$$$", corbaEncoding);
		res.put("$$$UTF_8_DEFINE$$$", UTF_8_DEFINE);
		return res;
	}
}
