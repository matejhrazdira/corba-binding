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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CorbaEnumRendererTest extends CorbaRendererTest {

	private void renderEnum() throws IOException {
		Symbol symbol = new Symbol(
				new ScopedName(Arrays.asList("SimpleIdl", "SomeEnum"), true),
				new EnumType(
						"UnusedEnumName",
						Arrays.asList("VALUE_A", "VALUE_B", "VALUE_C")
				)
		);
		CorbaEnumRenderer renderer = new CorbaEnumRenderer(new JavaProjectionProvider() {
			@Override
			public JavaProjection getProjection(final Symbol symbol) {
				return new JavaProjection(symbol, new ScopedName(Arrays.asList("com", "matejhrazdira", "generated", "SimpleIdl", "SomeEnum"), true));
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
		renderEnum();
		String actual = mJniCacheHeader.toString();
		String expected =
				"struct {\n" +
				"\tstruct {\n" +
				"\t\tstruct {\n" +
				"\t\t\tstruct {\n" +
				"\t\t\t\tstruct {\n" +
				"\t\t\t\t\tjclass _cls_;\n" +
				"\t\t\t\t\tstd::vector<jfieldID> _values_;\n" +
				"\t\t\t\t\tjmethodID _ordinal_;\n" +
				"\t\t\t\t} SomeEnum;\n" +
				"\t\t\t} SimpleIdl;\n" +
				"\t\t} generated;\n" +
				"\t} matejhrazdira;\n" +
				"} com;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderJniCacheImpl() throws IOException {
		renderEnum();
		String actual = mJniCacheImpl.toString();
		String expected =
				"{\n" +
				"\tjclass _cls_ = _env_->FindClass(\"com/matejhrazdira/generated/SimpleIdl/SomeEnum\");\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._cls_ = (jclass) _env_->NewGlobalRef(_cls_);\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._values_.reserve(3);\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, \"VALUE_A\", \"Lcom/matejhrazdira/generated/SimpleIdl/SomeEnum;\"));\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, \"VALUE_B\", \"Lcom/matejhrazdira/generated/SimpleIdl/SomeEnum;\"));\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._values_.push_back(_env_->GetStaticFieldID(_cls_, \"VALUE_C\", \"Lcom/matejhrazdira/generated/SimpleIdl/SomeEnum;\"));\n" +
				"\n" +
				"\tcom.matejhrazdira.generated.SimpleIdl.SomeEnum._ordinal_ = _env_->GetMethodID(_cls_, \"ordinal\", \"()I\");\n" +
				"\n" +
				"\t_env_->DeleteLocalRef(_cls_);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionHeader() throws IOException {
		renderEnum();
		String actual = mConversionHeader.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SomeEnum & _in_);\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SomeEnum & _out_);\n";
		assertEquals(expected, actual);
	}

	@Test
	public void renderConversionImpl() throws IOException {
		renderEnum();
		String actual = mConversionImpl.toString();
		String expected =
				"jobject convert(JNIEnv * _env_, const ::SimpleIdl::SomeEnum & _in_) {\n" +
				"\treturn _env_->GetStaticObjectField(_jni_->com.matejhrazdira.generated.SimpleIdl.SomeEnum._cls_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeEnum._values_[((int) _in_) < _jni_->com.matejhrazdira.generated.SimpleIdl.SomeEnum._values_.size() ? (int) _in_ :  0]);\n" +
				"}\n" +
				"\n" +
				"void convert(JNIEnv * _env_, const jobject _in_, ::SimpleIdl::SomeEnum & _out_) {\n" +
				"\t_out_ = (::SimpleIdl::SomeEnum) _env_->CallIntMethod(_in_, _jni_->com.matejhrazdira.generated.SimpleIdl.SomeEnum._ordinal_);\n" +
				"}\n";
		assertEquals(expected, actual);
	}

}