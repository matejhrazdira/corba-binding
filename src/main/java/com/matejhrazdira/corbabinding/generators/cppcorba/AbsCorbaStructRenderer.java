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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbsCorbaStructRenderer extends AbsCorbaWithMembersRenderer {

	public AbsCorbaStructRenderer(final JavaProjectionProvider projectionProvider, final OutputListener outputListener, final CorbaOutput output) {
		super(projectionProvider, outputListener, output);
	}

	@Override
	protected void renderTypeCacheEntries(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniImplPrivateImpl;
		writer.writeln(
				JniConfig.TYPE_CACHE_ANY_TABLE,
				"[\"", mJavaScopedRenderer.render(projection.name) , "\"]",
				" = ",
				JniConfig.TYPE_CACHE_CONVERT_ANY, "<", mCorbaScopedRenderer.render(projection.symbol.name), ">",
				";"
		);
	}

	@Override
	protected void renderConversionToJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		JavaStructProjection structProjection = (JavaStructProjection) projection;

		ArrayList<String> localRefs = writeConversionToJavaMembersInitialization(writer, structProjection);
		writer.writeln();

		writeConversionToJavaResultCreation(writer, structProjection);
		writer.writeln();

		wrideDeleteLocalRef(writer, localRefs);
		writer.writeln();

		writeConversionToJavaReturn(writer);
	}

	protected abstract ArrayList<String> writeConversionToJavaMembersInitialization(LineWriter writer, JavaStructProjection structProjection) throws IOException;

	private void writeConversionToJavaResultCreation(final LineWriter writer, final JavaStructProjection projection) throws IOException {
		List<String> newObjectArgs = new ArrayList<>();
		newObjectArgs.add(mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, JniConfig.JNI_CACHE_CLASS)));
		newObjectArgs.add(mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, JniConfig.JNI_CACHE_CTOR)));
		newObjectArgs.addAll(
				projection.members.stream()
						.map(member -> member.declarator.name)
						.collect(Collectors.toList())
		);
		writer.writeln(
				"jobject", " ", JniConfig.RESULT_VAR,
				" = ",
				jniCall(
						"NewObject",
						newObjectArgs.toArray(EMPTY_STRING_ARRAY)
				),
				";"
		);
	}

	private void wrideDeleteLocalRef(final LineWriter writer, final ArrayList<String> localRefs) throws IOException {
		for (final String localRef : localRefs) {
			writeDeleteLocalRef(writer, localRef);
		}
	}

	private void writeConversionToJavaReturn(final LineWriter writer) throws IOException {
		writer.writeln("return ", JniConfig.RESULT_VAR, ";");
	}
}
