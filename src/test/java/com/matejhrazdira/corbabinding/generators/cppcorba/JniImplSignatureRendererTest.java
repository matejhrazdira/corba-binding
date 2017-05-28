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

import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.interfaces.Param;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class JniImplSignatureRendererTest {

	@Test
	public void renderSignature() throws IOException {
		StringWriter output = new StringWriter();
		new JniImplSignatureRenderer().render(
				new LineWriter("\t", output),
				new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "ClassName"), true),
				new Operation(
						"someMethod",
						false,
						new ScopedName(Arrays.asList("SomeEnum"), true),
						Arrays.asList(
								new Param("inInteger", Param.Direction.IN, new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT)),
								new Param("outInteger", Param.Direction.OUT, new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT)),
								new Param("inString", Param.Direction.IN, new StringType()),
								new Param("inOutString", Param.Direction.IN_OUT, new StringType()),
								new Param("inStruct", Param.Direction.IN, new ScopedName(Arrays.asList("SomeStruct"), true))
						),
						Collections.emptyList()
				)
		);
		String actual = new String(output.toString());
		String expected = "JNIEXPORT jobject JNICALL Java_com_matejhrazdira_generated_ClassName_someMethod(JNIEnv * _env_, jobject _this_, jint inInteger, jobject outInteger, jobject inString, jobject inOutString, jobject inStruct)";
		// NOTE: jstrings are rendered as jobjects. This has to be fixed but at least
		// OpenJDK Runtime Environment (build 1.8.0_131-8u131-b11-0ubuntu1.16.04.2-b11)
		// is perfectly fine with this...
		assertEquals(actual, expected);
	}


}