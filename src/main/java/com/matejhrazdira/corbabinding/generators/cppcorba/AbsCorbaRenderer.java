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
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.NoOpOutputListener;
import com.matejhrazdira.corbabinding.util.OutputListener;
import com.matejhrazdira.corbabinding.util.UnsafeMethod;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class AbsCorbaRenderer {

	public static final String CACHE_CLASS = "_cls_";
	public static final String LOCAL_CLASS = "_cls_";

	public static final String CONVERSION_FUNCTION = "convert";
	public static final String CONVERSION_IN_ARG = "_in_";
	public static final String CONVERSION_OUT_ARG = "_out_";

	protected final JavaProjectionProvider mJavaProjectionProvider;
	protected final CorbaOutput mOutput;
	protected final OutputListener mOutputListener;
	protected final JniCacheRenderer mJniCacheRenderer = new JniCacheRenderer();
	private final JniScopedRenderer mClassNameRenderer = new JniScopedRenderer();
	protected final CorbaScopedRenderer mCorbaScopedRenderer = new CorbaScopedRenderer();

	public AbsCorbaRenderer(final JavaProjectionProvider projectionProvider, final OutputListener outputListener, final CorbaOutput output) {
		mJavaProjectionProvider = projectionProvider;
		mOutputListener = outputListener != null ? outputListener : new NoOpOutputListener();
		mOutput = output;
	}

	public void render(Symbol symbol) throws IOException {
		JavaProjection projection = mJavaProjectionProvider.getProjection(symbol);
		renderJniCacheHeader(projection);
		renderJniCacheBody(projection);
		renderConversionHeader(projection);
		renderConversionBody(projection);
	}

	private void renderConversionBody(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		writeConversionToJavaDeclaration(writer, projection);
		renderConversionFunction(this::renderConversionToJava, projection);
		writer.writeln();
		writeConversionFromJavaDeclaration(writer, projection);
		renderConversionFunction(this::renderConversionFromJava, projection);
	}

	private void renderConversionFunction(UnsafeMethod<JavaProjection, IOException> impl, JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		writer.write(" {").endl();
		writer.increaseLevel();
		impl.call(projection);
		writer.decreaseLevel();
		writer.writeln("}");
	}

	protected abstract void renderConversionFromJava(final JavaProjection projection) throws IOException;

	protected abstract void renderConversionToJava(final JavaProjection projection) throws IOException;

	private void renderConversionHeader(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionHeader;
		writeConversionToJavaDeclaration(writer, projection);
		writer.write(";").endl();
		writeConversionFromJavaDeclaration(writer, projection);
		writer.write(";").endl();
	}

	private void writeConversionFromJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		writer.write("void ", CONVERSION_FUNCTION, "(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", const jobject ", CONVERSION_IN_ARG, ", ", mCorbaScopedRenderer.render(projection.symbol.name), " & ", CONVERSION_OUT_ARG, ")");
	}

	protected void writeConversionToJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		writer.write("jobject ", CONVERSION_FUNCTION, "(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", const ", mCorbaScopedRenderer.render(projection.symbol.name), " & ", CONVERSION_IN_ARG, ")");
	}

	private void renderJniCacheHeader(final JavaProjection projection) throws IOException {
		mOutput.jniCacheHeader.enterScope(projection.name);
		writeJniCacheMembersDeclaration(projection);
	}

	protected void writeJniCacheMembersDeclaration(final JavaProjection projection) throws IOException {
		JniCacheHeaderWriter writer = mOutput.jniCacheHeader;
		writer.writeln("jclass ", CACHE_CLASS, ";");
		writeJniCacheMembersDeclarationImpl(projection);
	}

	protected abstract void writeJniCacheMembersDeclarationImpl(final JavaProjection projection) throws IOException;

	private void renderJniCacheBody(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		writer.writeln("{");
		writer.increaseLevel();
		writeJniCacheMembersInitialization(projection);
		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeJniCacheMembersInitialization(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		writer.writeln(
				"jclass ", LOCAL_CLASS, " = ", jniCall("FindClass", quoted(mClassNameRenderer.render(projection.name))), ";"
		);
		writer.writeln(
				mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(projection.name, CACHE_CLASS)), " = ",
				"(jclass) ", jniCall("NewGlobalRef", LOCAL_CLASS), ";"
		);
		writer.writeln();

		writeJniCacheMemersInitializationImpl(projection);

		writer.writeln();
		writeDeleteLocalRef(writer, LOCAL_CLASS);
	}

	protected abstract void writeJniCacheMemersInitializationImpl(JavaProjection projection) throws IOException;

	protected String quoted(String arg) {
		return "\"" + arg + "\"";
	}

	protected static final String jniCall(String method, String... args) {
		final CharSequence prefix = JniConfig.ARG_JNI_ENV + "->" + method + "(";
		final CharSequence suffix = ")";
		return Arrays.stream(args).collect(Collectors.joining(", ", prefix, suffix));
	}

	protected void writeDeleteLocalRef(final LineWriter writer, final String localRefName) throws IOException {
		writer.writeln(jniCall("DeleteLocalRef", localRefName), ";");
	}

	protected void logi(String info) {
		mOutputListener.onInfo(info);
	}

	protected void logw(String warning) {
		mOutputListener.onWarning(warning);
	}

	protected void loge(String error) {
		mOutputListener.onError(error);
	}
}
