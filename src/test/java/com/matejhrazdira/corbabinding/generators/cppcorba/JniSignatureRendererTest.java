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

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JniSignatureRendererTest {

	@Test
	public void renderMethodSignatureReturnVoid() throws Exception {
		String actual = new JniSignatureRenderer(new JniSignatureTypeRenderer()).renderMethodSignature(
				null,
				new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
				new StringType(),
				new SequenceType(new StringType(), null),
				new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "Struct"), true)
		);
		String expected = "(ILjava/lang/String;Ljava/util/List;Lcom/matejhrazdira/generated/Struct;)V";
		assertEquals(expected, actual);
	}

	@Test
	public void renderMethodSignatureReturnLong() throws Exception {
		String actual = new JniSignatureRenderer(new JniSignatureTypeRenderer()).renderMethodSignature(
				new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_LONG_INT),
				new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
				new StringType(),
				new SequenceType(new StringType(), null),
				new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "Struct"), true)
		);
		String expected = "(ILjava/lang/String;Ljava/util/List;Lcom/matejhrazdira/generated/Struct;)J";
		assertEquals(expected, actual);
	}
}