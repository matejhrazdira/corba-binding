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
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;

public class CorbaEnumRenderer extends AbsCorbaRenderer {

	private static final String CACHE_ENUM_VALUES = "_values_";
	private static final String CACHE_ENUM_ORDINAL = "_ordinal_";

	private final JniSignatureTypeRenderer mJniSignatureTypeRenderer = new JniSignatureTypeRenderer();

	public CorbaEnumRenderer(final JavaProjectionProvider projectionProvider, final CorbaOutput output, final OutputListener outputListener) {
		super(projectionProvider, outputListener, output);
	}

	@Override
	protected void renderConversionToJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		writer.writeln(
				"return ",
				jniCall(
						"GetStaticObjectField",
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, CACHE_CLASS)),
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, CACHE_ENUM_VALUES)) + "[(int) " + CONVERSION_IN_ARG + "]"
				),
				";"
		);
	}

	@Override
	protected void renderConversionFromJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		writer.writeln(
				CONVERSION_OUT_ARG, " = ", "(", mCorbaScopedRenderer.render(projection.symbol.name), ")", " ",
				jniCall(
						"CallIntMethod",
						CONVERSION_IN_ARG,
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, CACHE_ENUM_ORDINAL))
				),
				";"
		);
	}

	@Override
	protected void writeJniCacheMembersDeclarationImpl(final JavaProjection projection) throws IOException {
		JniCacheHeaderWriter writer = mOutput.jniCacheHeader;
		writer.writeln("std::vector<jfieldID>", " ", CACHE_ENUM_VALUES, ";");
		writer.writeln("jmethodID", " ", CACHE_ENUM_ORDINAL, ";");
	}

	@Override
	protected void writeJniCacheMemersInitializationImpl(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniCacheImpl;
		EnumType element = (EnumType) projection.symbol.element;

		String enumValues = mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(projection.name, CACHE_ENUM_VALUES));
		writer.writeln(enumValues,	".reserve(", Integer.toString(element.values.size()), ");");

		for (final EnumType.EnumValue value : element.values) {
			String valueName = value.name;
			writer.writeln(
					enumValues, ".push_back(",
					jniCall(
							"GetStaticFieldID",
							LOCAL_CLASS, quoted(valueName), quoted(mJniSignatureTypeRenderer.render(projection.name))),
					");"
			);
		}
		writer.writeln();

		writer.writeln(
				mJniCacheRenderer.renderQualifiedMember(ScopedName.nameInScope(projection.name, CACHE_ENUM_ORDINAL)), " = ",
				jniCall("GetMethodID",LOCAL_CLASS, quoted("ordinal"), quoted("()I")), ";"
		);
	}

}
