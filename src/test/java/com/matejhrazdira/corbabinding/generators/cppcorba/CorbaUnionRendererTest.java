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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.UnionType;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.ExpressionLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CorbaUnionRendererTest extends CorbaRendererTest {

	private void renderUnion() throws IOException {
		UnionField stringField = new UnionField(
				new StringType(),
				new Declarator("stringMember"),
				Arrays.asList(new ExpressionLabel(new ConstExp(Arrays.asList(
						new ScopedName(
								Arrays.asList("SimpleIdl", "SomeEnum", "VALUE_A"),
								true
						))))
				)
		);
		UnionField intField = new UnionField(
				new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
				new Declarator("intMember"),
				Arrays.asList(new ExpressionLabel(new ConstExp(Arrays.asList(
						new ScopedName(
								Arrays.asList("SimpleIdl", "SomeEnum", "VALUE_B"),
								true
						))))
				)
		);
		UnionField structField = new UnionField(
				new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true),
				new Declarator("structMember"),
				Arrays.asList(new ExpressionLabel(new ConstExp(Arrays.asList(
						new ScopedName(
								Arrays.asList("SimpleIdl", "SomeEnum", "VALUE_C"),
								true
						))))
				)
		);
		Symbol symbol = new Symbol(
				new ScopedName(Arrays.asList("SimpleIdl", "SomeUnion"), true),
				new UnionType(
						"SomeUnion",
						new ScopedName(Arrays.asList("SimpleIdl", "SomeEnum"), true),
						Arrays.asList(stringField, intField, structField)
				)
		);
		CorbaUnionRenderer renderer = new CorbaUnionRenderer(new JavaUnionProjectionProvider() {
			@Override
			public JavaUnionProjection getProjection(final Symbol symbol) {
				Member typeMember = new Member(new Declarator("_type_"), new ScopedName(Arrays.asList("SimpleIdl", "SomeEnum"), true));
				Member stringMember = new Member(new Declarator("stringMember"), new StringType());
				Member intMember = new Member(new Declarator("intMember"), new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT));
				Member structMember = new Member(new Declarator("structMember"), new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true));
				HashMap<Member, UnionField> fieldMap = new HashMap<>();
				fieldMap.put(stringMember, stringField);
				fieldMap.put(intMember, intField);
				fieldMap.put(structMember, structField);
				return new JavaUnionProjection.JavaUnionProjectionBuilder()
						.withSymbol(symbol)
						.withName(new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "SimpleIdl", "SomeUnion"), true))
						.withMembers(Arrays.asList(typeMember, stringMember, intMember, structMember))
						.withTypeMember(typeMember)
						.withFieldMap(fieldMap)
						.build();
			}

			@Override
			public ScopedName getProjectionPrefix() {
				return new ScopedName(Arrays.asList("com", "matejhrazdira", "generated"), true);
			}
		}, mOutput, null);
		renderer.render(symbol);
		mOutput.jniCacheHeader.exitScope();
	}

	@Test
	public void renderJniCacheHeader() throws IOException {
		renderUnion();
		String actual = mJniCacheHeader.toString();
		String expected =
				"struct {\n" +
				"\tstruct {\n" +
				"\t\tstruct {\n" +
				"\t\t\tstruct {\n" +
				"\t\t\t\tstruct {\n" +
				"\t\t\t\t\tjclass _cls_;\n" +
				"\t\t\t\t\tjmethodID _ctor_;\n" +
				"\t\t\t\t\tjfieldID _type_;\n" +
				"\t\t\t\t\tjfieldID stringMember;\n" +
				"\t\t\t\t\tjfieldID intMember;\n" +
				"\t\t\t\t\tjfieldID structMember;\n" +
				"\t\t\t\t} SomeUnion;\n" +
				"\t\t\t} SimpleIdl;\n" +
				"\t\t} generated;\n" +
				"\t} matejhrazdira;\n" +
				"} com;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderJniCacheImpl() throws IOException {
		renderUnion();
		String actual = mJniCacheImpl.toString();
		String expected =
				"{\n" +
				"\tjclass _cls_ = _env_->FindClass(\"com/matejhrazdira/generated/SimpleIdl/SomeUnion\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion._cls_ = (jclass) _env_->NewGlobalRef(_cls_);\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion._ctor_ = _env_->GetMethodID(_cls_, \"<init>\", \"(Lcom/matejhrazdira/generated/SimpleIdl/SomeEnum;Ljava/lang/String;ILcom/matejhrazdira/generated/SimpleIdl/SimpleStruct;)V\");\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion._type_ = _env_->GetFieldID(_cls_, \"_type_\", \"Lcom/matejhrazdira/generated/SimpleIdl/SomeEnum;\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion.stringMember = _env_->GetFieldID(_cls_, \"stringMember\", \"Ljava/lang/String;\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion.intMember = _env_->GetFieldID(_cls_, \"intMember\", \"I\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeUnion.structMember = _env_->GetFieldID(_cls_, \"structMember\", \"Lcom/matejhrazdira/generated/SimpleIdl/SimpleStruct;\");\n" +
				"\n" +
				"\t_env_->DeleteLocalRef(_cls_);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionHeader() throws IOException {
		renderUnion();
		String actual = mConversionHeader.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SomeUnion & _in_);\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SomeUnion & _out_);\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionImpl() throws IOException {
		renderUnion();
		String actual = mConversionImpl.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SomeUnion & _in_) {\n" +
				"\tjobject _type_ = nullptr;\n" +
				"\tjobject stringMember = nullptr;\n" +
				"\tjint intMember = 0;\n" +
				"\tjobject structMember = nullptr;\n" +
				"\n" +
				"\t_type_ = convert(_env_, _in_._d());\n" +
				"\tswitch(_in_._d()) {\n" +
				"\t\tcase ::SimpleIdl::VALUE_A:\n" +
				"\t\t\tstringMember = convert(_env_, _in_.stringMember());\n" +
				"\t\t\tbreak;\n" +
				"\t\tcase ::SimpleIdl::VALUE_B:\n" +
				"\t\t\tintMember = convert(_env_, _in_.intMember());\n" +
				"\t\t\tbreak;\n" +
				"\t\tcase ::SimpleIdl::VALUE_C:\n" +
				"\t\t\tstructMember = convert(_env_, _in_.structMember());\n" +
				"\t\t\tbreak;\n" +
				"\t}\n" +
				"\n" +
				"\tjobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion._cls_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion._ctor_, _type_, stringMember, intMember, structMember);\n" +
				"\n" +
				"\t_env_->DeleteLocalRef(_type_);\n" +
				"\t_env_->DeleteLocalRef(stringMember);\n" +
				"\t_env_->DeleteLocalRef(structMember);\n" +
				"\n" +
				"\treturn _result_;\n" +
				"}\n" +
				"\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SomeUnion & _out_) {\n" +
				"\tjobject _j__type__ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion._type_);\n" +
				"\t::SimpleIdl::SomeEnum _type_;\n" +
				"\tconvert(_env_, _j__type__, _type_);\n" +
				"\t_env_->DeleteLocalRef(_j__type__);\n" +
				"\n" +
				"\tswitch(_type_) {\n" +
				"\t\tcase ::SimpleIdl::VALUE_A:\n" +
				"\t\t{\n" +
				"\t\t\tjobject _j_stringMember_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion.stringMember);\n" +
				"\t\t\tchar * stringMember = convert<char>(_env_, _j_stringMember_);\n" +
				"\t\t\t_env_->DeleteLocalRef(_j_stringMember_);\n" +
				"\t\t\t_out_.stringMember(stringMember);\n" +
				"\t\t\tbreak;\n" +
				"\t\t}\n" +
				"\t\tcase ::SimpleIdl::VALUE_B:\n" +
				"\t\t{\n" +
				"\t\t\tjint _j_intMember_ = _env_->GetIntField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion.intMember);\n" +
				"\t\t\t::CORBA::Long intMember;\n" +
				"\t\t\tconvert(_env_, _j_intMember_, intMember);\n" +
				"\t\t\t_out_.intMember(intMember);\n" +
				"\t\t\tbreak;\n" +
				"\t\t}\n" +
				"\t\tcase ::SimpleIdl::VALUE_C:\n" +
				"\t\t{\n" +
				"\t\t\tjobject _j_structMember_ = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeUnion.structMember);\n" +
				"\t\t\t::SimpleIdl::SimpleStruct structMember;\n" +
				"\t\t\tconvert(_env_, _j_structMember_, structMember);\n" +
				"\t\t\t_env_->DeleteLocalRef(_j_structMember_);\n" +
				"\t\t\t_out_.structMember(structMember);\n" +
				"\t\t\tbreak;\n" +
				"\t\t}\n" +
				"\t}\n" +
				"\n" +
				"\t_out_._d(_type_);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

}