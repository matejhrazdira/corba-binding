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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.StructType;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CorbaStructRendererTest extends CorbaRendererTest {

	private void renderStruct() throws IOException {
		Symbol symbol = new Symbol(
				new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true),
				new StructType(
						"SimpleStruct",
						Arrays.asList(
								new Member(new Declarator("stringMember"), new StringType()),
								new Member(new Declarator("intMember"), new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT))
						)
				)
		);
		CorbaStructRenderer renderer = new CorbaStructRenderer(new JavaStructProjectionProvider() {
			@Override
			public JavaStructProjection getProjection(final Symbol symbol) {
				return new JavaStructProjection(
						symbol,
						new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "SimpleIdl", "SimpleStruct"), true),
						Arrays.asList(
								new Member(new Declarator("stringMember"), new StringType()),
								new Member(new Declarator("intMember"), new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT))
						)
				);
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
		renderStruct();
		String actual = mJniCacheHeader.toString();
		String expected =
				"struct {\n" +
				"\tstruct {\n" +
				"\t\tstruct {\n" +
				"\t\t\tstruct {\n" +
				"\t\t\t\tstruct {\n" +
				"\t\t\t\t\tjclass _cls_;\n" +
				"\t\t\t\t\tjmethodID _ctor_;\n" +
				"\t\t\t\t\tjfieldID stringMember;\n" +
				"\t\t\t\t\tjfieldID intMember;\n" +
				"\t\t\t\t} SimpleStruct;\n" +
				"\t\t\t} SimpleIdl;\n" +
				"\t\t} generated;\n" +
				"\t} matejhrazdira;\n" +
				"} com;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderJniCacheImpl() throws IOException {
		renderStruct();
		String actual = mJniCacheImpl.toString();
		String expected =
				"{\n" +
				"\tjclass _cls_ = _env_->FindClass(\"com/matejhrazdira/generated/SimpleIdl/SimpleStruct\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SimpleStruct._cls_ = (jclass) _env_->NewGlobalRef(_cls_);\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SimpleStruct._ctor_ = _env_->GetMethodID(_cls_, \"<init>\", \"(Ljava/lang/String;I)V\");\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SimpleStruct.stringMember = _env_->GetFieldID(_cls_, \"stringMember\", \"Ljava/lang/String;\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SimpleStruct.intMember = _env_->GetFieldID(_cls_, \"intMember\", \"I\");\n" +
				"\n" +
				"\t_env_->DeleteLocalRef(_cls_);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionHeader() throws IOException {
		renderStruct();
		String actual = mConversionHeader.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SimpleStruct & _in_);\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SimpleStruct & _out_);\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionImpl() throws IOException {
		renderStruct();
		String actual = mConversionImpl.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SimpleStruct & _in_) {\n" +
				"\tjobject stringMember = convert(_env_, _in_.stringMember);\n" +
				"\tjint intMember = convert(_env_, _in_.intMember);\n" +
				"\n" +
				"\tjobject _result_ = _env_->NewObject(_jni_->com.matejhrazdira.generated.SimpleIdl.SimpleStruct._cls_, _jni_->com.matejhrazdira.generated.SimpleIdl.SimpleStruct._ctor_, stringMember, intMember);\n" +
				"\n" +
				"\t_env_->DeleteLocalRef(stringMember);\n" +
				"\n" +
				"\treturn _result_;\n" +
				"}\n" +
				"\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SimpleStruct & _out_) {\n" +
				"\tjobject stringMember = _env_->GetObjectField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SimpleStruct.stringMember);\n" +
				"\tconvert(_env_, stringMember, _out_.stringMember);\n" +
				"\t_env_->DeleteLocalRef(stringMember);\n" +
				"\n" +
				"\tjint intMember = _env_->GetIntField(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SimpleStruct.intMember);\n" +
				"\tconvert(_env_, intMember, _out_.intMember);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

}