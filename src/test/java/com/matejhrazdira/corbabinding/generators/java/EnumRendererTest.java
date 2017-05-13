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

package com.matejhrazdira.corbabinding.generators.java;

import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EnumRendererTest {

	@Test
	public void renderEnumTest() throws IOException {
		EnumRenderer renderer = new EnumRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				null);
		StringWriter stringWriter = new StringWriter();
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(
								Arrays.asList("myidl", "TestEnum"),
								true
						),
						new EnumType(
								"TestEnum",
								Arrays.asList(
										"VALUE_1",
										"VALUE_2",
										"VALUE_3"
								)
						)
				)
		);
		String expected ="package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public enum TestEnum {\n" +
				"\n" +
				"\tVALUE_1,\n" +
				"\tVALUE_2,\n" +
				"\tVALUE_3,\n" +
				"\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}
}