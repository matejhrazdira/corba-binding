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
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.definitions.Module;
import com.matejhrazdira.corbabinding.idl.definitions.StructType;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class StructRendererTest {

	@Test
	public void renderNoMemberTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
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
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList()
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder();\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct();\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderOneMemberTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
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
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList(
										new Member(
												new Declarator("intMember"),
												new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT)
										)
								)
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic final int intMember;\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t\tthis.intMember = 0;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestStruct(int intMember) {\n" +
				"\t\tthis.intMember = intMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(intMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate int intMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(int intMember) {\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withIntMember(int intMember) {\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct(intMember);\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderStringMemberTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
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
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList(
										new Member(
												new Declarator("stringMember"),
												new StringType()
										)
								)
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic final String stringMember;\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t\tthis.stringMember = \"\";\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestStruct(String stringMember) {\n" +
				"\t\tthis.stringMember = stringMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(stringMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate String stringMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(String stringMember) {\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStringMember(String stringMember) {\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct(stringMember);\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderEnumMemberTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				new SymbolResolver(
						Arrays.asList(
								new Symbol(
										new ScopedName(Arrays.asList("myidl"), true),
										new Module("myidl", Collections.emptyList())
								),
								new Symbol(
										new ScopedName(Arrays.asList("myidl", "TestEnum"), true),
										new EnumType("TestEnum", Collections.emptyList())
								)
						)
				),
				null);
		StringWriter stringWriter = new StringWriter();
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList(
										new Member(
												new Declarator("enumMember"),
												new ScopedName(Arrays.asList("myidl", "TestEnum"), true)
										)
								)
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic final com.matejhrazdira.generated.myidl.TestEnum enumMember;\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t\tthis.enumMember = com.matejhrazdira.generated.myidl.TestEnum.values()[0];\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestStruct(com.matejhrazdira.generated.myidl.TestEnum enumMember) {\n" +
				"\t\tthis.enumMember = enumMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(enumMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate com.matejhrazdira.generated.myidl.TestEnum enumMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(com.matejhrazdira.generated.myidl.TestEnum enumMember) {\n" +
				"\t\t\tthis.enumMember = enumMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withEnumMember(com.matejhrazdira.generated.myidl.TestEnum enumMember) {\n" +
				"\t\t\tthis.enumMember = enumMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct(enumMember);\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderTwoMemberTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
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
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList(
										new Member(
												new Declarator("intMember"),
												new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT)
										),
										new Member(
												new Declarator("stringMember"),
												new StringType()
										)
								)
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic final int intMember;\n" +
				"\tpublic final String stringMember;\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t\tthis.intMember = 0;\n" +
				"\t\tthis.stringMember = \"\";\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestStruct(int intMember, String stringMember) {\n" +
				"\t\tthis.intMember = intMember;\n" +
				"\t\tthis.stringMember = stringMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(intMember, stringMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate int intMember;\n" +
				"\t\tprivate String stringMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(int intMember, String stringMember) {\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withIntMember(int intMember) {\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStringMember(String stringMember) {\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct(intMember, stringMember);\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void renderStructWithListTest() throws IOException {
		StructRenderer renderer = new StructRenderer(
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
						new ScopedName(Arrays.asList("myidl", "TestStruct"), true),
						new StructType(
								"TestStruct",
								Arrays.asList(
										new Member(
												new Declarator("listMember"),
												new SequenceType(new StringType(), null)
										)
								)
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestStruct {\n" +
				"\n" +
				"\tpublic final java.util.List<String> listMember;\n" +
				"\n" +
				"\tpublic TestStruct() {\n" +
				"\t\tthis.listMember = java.util.Collections.unmodifiableList(new java.util.ArrayList<String>());\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestStruct(java.util.List<String> listMember) {\n" +
				"\t\tthis.listMember = listMember != null ? java.util.Collections.unmodifiableList(new java.util.ArrayList<>(listMember)) : null;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(listMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate java.util.List<String> listMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(java.util.List<String> listMember) {\n" +
				"\t\t\tthis.listMember = listMember != null ? new java.util.ArrayList<>(listMember) : null;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withListMember(java.util.List<String> listMember) {\n" +
				"\t\t\tthis.listMember = listMember;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestStruct build() {\n" +
				"\t\t\treturn new TestStruct(listMember);\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}
}