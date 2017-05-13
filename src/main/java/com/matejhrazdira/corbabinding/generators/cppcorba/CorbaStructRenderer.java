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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CorbaStructRenderer extends AbsCorbaStructRenderer {

	public CorbaStructRenderer(final JavaStructProjectionProvider projectionProvider, final CorbaOutput output, final OutputListener outputListener) {
		super(projectionProvider, outputListener, output);
	}

	@Override
	protected ArrayList<String> writeConversionToJavaMembersInitialization(final LineWriter writer, final JavaStructProjection structProjection) throws IOException {
		ArrayList<String> localRefs = new ArrayList<>();
		for (final Member member : structProjection.members) {
			Type type = member.type;
			String name = member.declarator.name;
			if (!(type instanceof PrimitiveType)) {
				localRefs.add(name);
			}
			writer.writeln(
					mJniJavaTypeRenderer.render(type), " ", name, " = ", CONVERSION_FUNCTION,  "(",
					JniConfig.ARG_JNI_ENV, ", ", CONVERSION_IN_ARG, ".", name ,
					");"
			);
		}
		return localRefs;
	}

	@Override
	protected void renderConversionFromJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		JavaStructProjection structProjection = (JavaStructProjection) projection;
		final List<Member> members = structProjection.members;
		for (int i = 0; i < members.size(); i++) {
			final Member member = members.get(i);
			Type type = member.type;
			String name = member.declarator.name;
			writer.writeln(
					mJniJavaTypeRenderer.render(type), " ", name,
					" = ",
					jniCall(
							mJniFieldAccessRenderer.render(type),
							CONVERSION_IN_ARG,
							mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, name))
					),
					";"
			);
			writer.writeln(
					CONVERSION_FUNCTION, "(",
					JniConfig.ARG_JNI_ENV, ", ",
					name, ", ",
					CONVERSION_OUT_ARG, ".", name,
					");"
			);
			if (!(type instanceof PrimitiveType)) {
				writeDeleteLocalRef(writer, name);
			}
			if (i < members.size() - 1) {
				writer.writeln();
			}
		}
	}
}
