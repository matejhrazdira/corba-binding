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

	protected final JavaProjectionProvider mJavaProjectionProvider;
	protected final CorbaOutput mOutput;
	protected final OutputListener mOutputListener;
	protected final JniCacheRenderer mJniCacheRenderer = new JniCacheRenderer();
	protected final JniScopedRenderer mClassNameRenderer = new JniScopedRenderer();
	protected final CorbaScopedRenderer mCorbaScopedRenderer = new CorbaScopedRenderer();

	public AbsCorbaRenderer(final JavaProjectionProvider projectionProvider, final OutputListener outputListener, final CorbaOutput output) {
		mJavaProjectionProvider = projectionProvider;
		mOutputListener = outputListener != null ? outputListener : new NoOpOutputListener();
		mOutput = output;
	}

	public void render(Symbol symbol) throws IOException {
		JavaProjection projection = mJavaProjectionProvider.getProjection(symbol);
		renderJniCacheHeader(projection);
		renderJniCacheHeaderClientImpl(projection);
		renderJniCacheBody(projection);
		renderConversionHeader(projection);
		renderConversionBody(projection);
		renderJniImplHeader(projection);
		renderJniImplBody(projection);
		renderTypeCacheEntries(projection);
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

	protected void writeConversionFromJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		writer.write("void ", JniConfig.CONVERSION_FUNCTION, "(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", const jobject ", JniConfig.CONVERSION_IN_ARG, ", ", mCorbaScopedRenderer.render(projection.symbol.name), " & ", JniConfig.CONVERSION_OUT_ARG, ")");
	}

	protected void writeConversionToJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		writer.write("jobject ", JniConfig.CONVERSION_FUNCTION, "(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", const ", mCorbaScopedRenderer.render(projection.symbol.name), " & ", JniConfig.CONVERSION_IN_ARG, ")");
	}

	private void renderJniCacheHeader(final JavaProjection projection) throws IOException {
		mOutput.jniCacheHeader.enterScope(projection.name);
		writeJniCacheMembersDeclaration(projection);
	}

	protected void writeJniCacheMembersDeclaration(final JavaProjection projection) throws IOException {
		JniCacheHeaderWriter writer = mOutput.jniCacheHeader;
		writer.writeln("jclass ", JniConfig.JNI_CACHE_CLASS, ";");
		writeJniCacheMembersDeclarationImpl(projection);
	}

	protected abstract void writeJniCacheMembersDeclarationImpl(final JavaProjection projection) throws IOException;

	protected void renderJniCacheHeaderClientImpl(final JavaProjection projection) throws IOException {

	}

	protected void renderJniCacheBody(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		writer.writeln("{");
		writer.increaseLevel();
		writeJniCacheMembersInitialization(projection);
		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeJniCacheMembersInitialization(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		final String className;
		if (projection.symbol.innerSymbol) {
			className = mClassNameRenderer.renderNested(projection.name);
		} else {
			className = mClassNameRenderer.render(projection.name);
		}
		writer.writeln(
				"jclass ", JniConfig.JNI_CACHE_CLASS, " = ", jniCall("FindClass", quoted(className)), ";"
		);
		writer.writeln(
				mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(projection.name, JniConfig.JNI_CACHE_CLASS)), " = ",
				"(jclass) ", jniCall("NewGlobalRef", JniConfig.JNI_CACHE_CLASS), ";"
		);
		writer.writeln();

		writeJniCacheMemersInitializationImpl(projection);

		writer.writeln();
		writeDeleteLocalRef(writer, JniConfig.JNI_CACHE_CLASS);
	}

	protected abstract void writeJniCacheMemersInitializationImpl(JavaProjection projection) throws IOException;

	protected void renderJniImplHeader(final JavaProjection projection) throws IOException {

	}

	protected void renderJniImplBody(final JavaProjection projection) throws IOException {

	}

	protected void renderTypeCacheEntries(final JavaProjection projection) throws IOException {

	}

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
