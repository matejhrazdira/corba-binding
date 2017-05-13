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
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.Literal;
import com.matejhrazdira.corbabinding.idl.expressions.PrimitiveElement;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ConstantRendererTest {

	@Test
	public void renderIntegerConstantTest() throws IOException {
		ConstantRenderer renderer = new ConstantRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				new SymbolResolver(Collections.emptyList()),
				null);
		StringWriter stringWriter = new StringWriter();
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(
								Arrays.asList("myidl", "TEST_CONSTANT"),
								true
						),
						new ConstDcl(
								"TEST_CONSTANT",
								new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
								new ConstExp(
										Arrays.asList(
												new Literal("5"),
												new PrimitiveElement(PrimitiveElement.Type.ADD),
												new Literal("6")
												)
								)
						)
				)
		);
		String expected ="package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public interface TEST_CONSTANT {\n" +
				"\n" +
				"\tpublic static final int value = 5 + 6;\n" +
				"\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderStringConstantTest() throws IOException {
		ConstantRenderer renderer = new ConstantRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				new SymbolResolver(Collections.emptyList()),
				null);
		StringWriter stringWriter = new StringWriter();
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(
								Arrays.asList("myidl", "TEST_STRING_CONSTANT"),
								true
						),
						new ConstDcl(
								"TEST_STRING_CONSTANT",
								new StringType(),
								new ConstExp(
										Arrays.asList(
												new Literal("\"This is some constant string!\"")
										)
								)
						)
				)
		);
		String expected ="package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public interface TEST_STRING_CONSTANT {\n" +
				"\n" +
				"\tpublic static final String value = \"This is some constant string!\";\n" +
				"\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}
}