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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.File;
import java.io.IOException;

public class CppCorbaGeneratorBuilder {

	private File mOutput;
	private JavaProjectionProvider mEnumProjectionProvider;
	private JavaStructProjectionProvider mStructProjectionProvider;
	private JavaUnionProjectionProvider mUnionProjectionProvider;
	private OutputListener mOutputListener;
	private String mTaoIdlIncludePrefix;

	public CppCorbaGeneratorBuilder withOutput(final File output) {
		mOutput = output;
		return this;
	}

	public CppCorbaGeneratorBuilder withEnumProjectionProvider(final JavaProjectionProvider enumProjectionProvider) {
		mEnumProjectionProvider = enumProjectionProvider;
		return this;
	}

	public CppCorbaGeneratorBuilder withStructProjectionProvider(final JavaStructProjectionProvider structProjectionProvider) {
		mStructProjectionProvider = structProjectionProvider;
		return this;
	}

	public CppCorbaGeneratorBuilder withUnionProjectionProvider(final JavaUnionProjectionProvider unionProjectionProvider) {
		mUnionProjectionProvider = unionProjectionProvider;
		return this;
	}

	public CppCorbaGeneratorBuilder withOutputListener(final OutputListener outputListener) {
		mOutputListener = outputListener;
		return this;
	}

	public CppCorbaGeneratorBuilder withTaoIdlIncludePrefix(final String taoIdlIncludePrefix) {
		mTaoIdlIncludePrefix = taoIdlIncludePrefix;
		return this;
	}

	public CppCorbaGenerator createCppCorbaGenerator() throws IOException {
		return new CppCorbaGenerator(mOutput, mEnumProjectionProvider, mStructProjectionProvider, mUnionProjectionProvider, mOutputListener, mTaoIdlIncludePrefix);
	}
}
