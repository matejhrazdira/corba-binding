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
import com.matejhrazdira.corbabinding.idl.definitions.UnionType;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.DefaultLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.ExpressionLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.Literal;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UnionRendererTest {

	@Test
	public void renderEnumBasedUnion() throws IOException {
		UnionRenderer renderer = new UnionRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				new SymbolResolver(
						Arrays.asList(
								new Symbol(
										new ScopedName(Arrays.asList("somescope"), true),
										new Module("somescope", Collections.emptyList())
								),
								new Symbol(
										new ScopedName(Arrays.asList("somescope", "SomeStruct"), true),
										new StructType("SomeStruct", Collections.emptyList())
								),
								new Symbol(
										new ScopedName(Arrays.asList("somescope", "TestEnum"), true),
										new EnumType("TestEnum", Collections.emptyList())
								)
						)
				),
				null);
		StringWriter stringWriter = new StringWriter();
		ScopedName unionType = new ScopedName(Arrays.asList("somescope", "TestEnum"), true);
		List<UnionField> unionFields = Arrays.asList(
				new UnionField(
						new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
						new Declarator("intMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new ScopedName(
																Arrays.asList("somescope", "TestEnum", "VALUE_A"),
																true
														)
												)
										)
								)
						)
				),
				new UnionField(
						new StringType(),
						new Declarator("stringMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new ScopedName(
																Arrays.asList("somescope", "TestEnum", "VALUE_B"),
																true
														)
												)
										)
								),
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new ScopedName(
																Arrays.asList("somescope", "TestEnum", "VALUE_C"),
																true
														)
												)
										)
								)
						)
				),
				new UnionField(
						new ScopedName(Arrays.asList("somescope", "SomeStruct"), true),
						new Declarator("structMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new ScopedName(
																Arrays.asList("somescope", "TestEnum", "VALUE_D"),
																true
														)
												)
										)
								),
								new DefaultLabel()
						)
				)
		);
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(Arrays.asList("myidl", "TestUnion"), true),
						new UnionType(
								"TestUnion",
								unionType,
								unionFields
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestUnion {\n" +
				"\n" +
				"\tpublic final com.matejhrazdira.generated.somescope.TestEnum _type_;\n" +
				"\tpublic final int intMember;\n" +
				"\tpublic final String stringMember;\n" +
				"\tpublic final com.matejhrazdira.generated.somescope.SomeStruct structMember;\n" +
				"\n" +
				"\tpublic TestUnion() {\n" +
				"\t\tthis._type_ = com.matejhrazdira.generated.somescope.TestEnum.values()[0];\n" +
				"\t\tthis.intMember = 0;\n" +
				"\t\tthis.stringMember = \"\";\n" +
				"\t\tthis.structMember = new com.matejhrazdira.generated.somescope.SomeStruct();\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestUnion(com.matejhrazdira.generated.somescope.TestEnum _type_, int intMember, String stringMember, com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\tthis._type_ = _type_;\n" +
				"\t\tthis.intMember = intMember;\n" +
				"\t\tthis.stringMember = stringMember;\n" +
				"\t\tthis.structMember = structMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(_type_, intMember, stringMember, structMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate com.matejhrazdira.generated.somescope.TestEnum _type_;\n" +
				"\t\tprivate int intMember;\n" +
				"\t\tprivate String stringMember;\n" +
				"\t\tprivate com.matejhrazdira.generated.somescope.SomeStruct structMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(com.matejhrazdira.generated.somescope.TestEnum _type_, int intMember, String stringMember, com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\t\tthis._type_ = _type_;\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\tthis.structMember = structMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder with_type_(com.matejhrazdira.generated.somescope.TestEnum _type_) {\n" +
				"\t\t\tthis._type_ = _type_;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withIntMember(int intMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\tthis._type_ = com.matejhrazdira.generated.somescope.TestEnum.VALUE_A;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStringMember(String stringMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\tthis._type_ = com.matejhrazdira.generated.somescope.TestEnum.VALUE_B;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStructMember(com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.structMember = structMember;\n" +
				"\t\t\tthis._type_ = com.matejhrazdira.generated.somescope.TestEnum.VALUE_D;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestUnion build() {\n" +
				"\t\t\treturn new TestUnion(_type_, intMember, stringMember, structMember);\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate void reset() {\n" +
				"\t\t\tif (_type_ == null) {\n" +
				"\t\t\t\treturn;\n" +
				"\t\t\t}\n" +
				"\t\t\tswitch(_type_) {\n" +
				"\t\t\t\tcase VALUE_A:\n" +
				"\t\t\t\t\tintMember = 0;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t\tcase VALUE_B:\n" +
				"\t\t\t\tcase VALUE_C:\n" +
				"\t\t\t\t\tstringMember = null;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t\tcase VALUE_D:\n" +
				"\t\t\t\tdefault:\n" +
				"\t\t\t\t\tstructMember = null;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}


	@Test
	public void renderIntBasedUnion() throws IOException {
		UnionRenderer renderer = new UnionRenderer(
				new JavaScopedRenderer(
						new ScopedName(
								Arrays.asList("com", "matejhrazdira", "generated")
								, true
						)
				),
				new SymbolResolver(
						Arrays.asList(
								new Symbol(
										new ScopedName(Arrays.asList("somescope"), true),
										new Module("somescope", Collections.emptyList())
								),
								new Symbol(
										new ScopedName(Arrays.asList("somescope", "SomeStruct"), true),
										new StructType("SomeStruct", Collections.emptyList())
								)
						)
				),
				null);
		StringWriter stringWriter = new StringWriter();
		Type unionType = new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT);
		List<UnionField> unionFields = Arrays.asList(
				new UnionField(
						new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
						new Declarator("intMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new Literal("0")
												)
										)
								)
						)
				),
				new UnionField(
						new StringType(),
						new Declarator("stringMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new Literal("1")
												)
										)
								),
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new Literal("3")
												)
										)
								)
						)
				),
				new UnionField(
						new ScopedName(Arrays.asList("somescope", "SomeStruct"), true),
						new Declarator("structMember"),
						Arrays.asList(
								new ExpressionLabel(
										new ConstExp(
												Arrays.asList(
														new Literal("5")
												)
										)
								),
								new DefaultLabel()
						)
				)
		);
		renderer.render(
				stringWriter,
				new Symbol(
						new ScopedName(Arrays.asList("myidl", "TestUnion"), true),
						new UnionType(
								"TestUnion",
								unionType,
								unionFields
						)
				)
		);
		String expected =
				"package com.matejhrazdira.generated.myidl;\n" +
				"\n" +
				"public class TestUnion {\n" +
				"\n" +
				"\tpublic final int _type_;\n" +
				"\tpublic final int intMember;\n" +
				"\tpublic final String stringMember;\n" +
				"\tpublic final com.matejhrazdira.generated.somescope.SomeStruct structMember;\n" +
				"\n" +
				"\tpublic TestUnion() {\n" +
				"\t\tthis._type_ = 0;\n" +
				"\t\tthis.intMember = 0;\n" +
				"\t\tthis.stringMember = \"\";\n" +
				"\t\tthis.structMember = new com.matejhrazdira.generated.somescope.SomeStruct();\n" +
				"\t}\n" +
				"\n" +
				"\tpublic TestUnion(int _type_, int intMember, String stringMember, com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\tthis._type_ = _type_;\n" +
				"\t\tthis.intMember = intMember;\n" +
				"\t\tthis.stringMember = stringMember;\n" +
				"\t\tthis.structMember = structMember;\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Builder builder() {\n" +
				"\t\treturn new Builder(_type_, intMember, stringMember, structMember);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic static class Builder {\n" +
				"\n" +
				"\t\tprivate int _type_;\n" +
				"\t\tprivate int intMember;\n" +
				"\t\tprivate String stringMember;\n" +
				"\t\tprivate com.matejhrazdira.generated.somescope.SomeStruct structMember;\n" +
				"\n" +
				"\t\tpublic Builder() {\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate Builder(int _type_, int intMember, String stringMember, com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\t\tthis._type_ = _type_;\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\tthis.structMember = structMember;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder with_type_(int _type_) {\n" +
				"\t\t\tthis._type_ = _type_;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withIntMember(int intMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.intMember = intMember;\n" +
				"\t\t\tthis._type_ = 0;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStringMember(String stringMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.stringMember = stringMember;\n" +
				"\t\t\tthis._type_ = 1;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic Builder withStructMember(com.matejhrazdira.generated.somescope.SomeStruct structMember) {\n" +
				"\t\t\treset();\n" +
				"\t\t\tthis.structMember = structMember;\n" +
				"\t\t\tthis._type_ = 5;\n" +
				"\t\t\treturn this;\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tpublic TestUnion build() {\n" +
				"\t\t\treturn new TestUnion(_type_, intMember, stringMember, structMember);\n" +
				"\t\t}\n" +
				"\n" +
				"\t\tprivate void reset() {\n" +
				"\t\t\tswitch(_type_) {\n" +
				"\t\t\t\tcase 0:\n" +
				"\t\t\t\t\tintMember = 0;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t\tcase 1:\n" +
				"\t\t\t\tcase 3:\n" +
				"\t\t\t\t\tstringMember = null;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t\tcase 5:\n" +
				"\t\t\t\tdefault:\n" +
				"\t\t\t\t\tstructMember = null;\n" +
				"\t\t\t\t\tbreak;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}\n";
		String actual = stringWriter.toString();
		assertEquals(expected, actual);
	}
}