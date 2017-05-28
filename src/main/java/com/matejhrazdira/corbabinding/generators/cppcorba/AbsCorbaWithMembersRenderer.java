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

import com.matejhrazdira.corbabinding.generators.java.JavaScopedRenderer;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.stream.Collectors;

public abstract class AbsCorbaWithMembersRenderer extends AbsCorbaRenderer {
	protected static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
	protected static final String[] EMPTY_STRING_ARRAY = new String[0];
	protected final JniJavaTypeRenderer mJniJavaTypeRenderer = new JniJavaTypeRenderer();
	protected final JniFieldAccessRenderer mJniFieldAccessRenderer = new JniFieldAccessRenderer();
	protected final JniSignatureTypeRenderer mJniSignatureTypeRenderer;
	protected final JniSignatureRenderer mSignatureRenderer;
	protected final JavaScopedRenderer mJavaScopedRenderer = new JavaScopedRenderer(true);

	public AbsCorbaWithMembersRenderer(final JavaProjectionProvider projectionProvider, final OutputListener outputListener, final CorbaOutput output) {
		super(projectionProvider, outputListener, output);
		mJniSignatureTypeRenderer = new JniSignatureTypeRenderer(projectionProvider.getProjectionPrefix());
		mSignatureRenderer = new JniSignatureRenderer(mJniSignatureTypeRenderer);
	}

	@Override
	protected void writeJniCacheMembersDeclarationImpl(final JavaProjection projection) throws IOException {
		JniCacheHeaderWriter writer = mOutput.jniCacheHeader;
		JavaStructProjection structProjection = (JavaStructProjection) projection;
		writer.writeln("jmethodID ", JniConfig.JNI_CACHE_CTOR, ";");
		for (final Member member : structProjection.members) {
			writer.writeln("jfieldID ", member.declarator.name, ";");
		}
	}

	@Override
	protected void writeJniCacheMemersInitializationImpl(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		JavaStructProjection structProjection = (JavaStructProjection) projection;
		writeCacheCtorInitialization(writer, structProjection);
		writer.writeln();
		writeCacheFieldsInitialization(writer, structProjection);
	}

	private void writeCacheCtorInitialization(final LineWriter writer, final JavaStructProjection structProjection) throws IOException {
		writer.writeln(
				mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(structProjection.name, JniConfig.JNI_CACHE_CTOR)),
				" = ",
				jniCall(
						"GetMethodID",
						JniConfig.JNI_CACHE_CLASS,
						quoted(JniConfig.JNI_CTOR_NAME),
						quoted(mSignatureRenderer.renderMethodSignature(
								null,
								structProjection.members.stream()
										.map(member -> member.type)
										.collect(Collectors.toList())
										.toArray(EMPTY_TYPE_ARRAY)
								)
						)
				),
				";"
		);
	}

	private void writeCacheFieldsInitialization(final LineWriter writer, final JavaStructProjection structProjection) throws IOException {
		for (final Member member : structProjection.members) {
			writer.writeln(
					mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(structProjection.name, member.declarator.name)),
					" = ",
					jniCall(
							"GetFieldID",
							JniConfig.JNI_CACHE_CLASS,
							quoted(member.declarator.name),
							quoted(mJniSignatureTypeRenderer.render(member.type))
					),
					";"
			);
		}
	}
}
