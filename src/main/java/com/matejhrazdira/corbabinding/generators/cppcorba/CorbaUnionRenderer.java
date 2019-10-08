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

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.SwitchData;
import com.matejhrazdira.corbabinding.generators.SwitchData.SwitchDataBuilder;
import com.matejhrazdira.corbabinding.generators.SwitchRenderingUtility;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Data;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CorbaUnionRenderer extends AbsCorbaStructRenderer {

	private final SwitchRenderingUtility mSwitchRenderingUtility;
	private final CorbaTypeRenderer mCorbaTypeRenderer = new CorbaTypeRenderer();

	public CorbaUnionRenderer(final JavaUnionProjectionProvider projectionProvider, final CorbaOutput output, final OutputListener outputListener) {
		super(projectionProvider, outputListener, output);
		mSwitchRenderingUtility = new SwitchRenderingUtility(new ExpressionRenderer(mCorbaScopedRenderer)) {
			@Override
			protected String renderEnumConstant(final ScopedName name) {
				List<String> components = name.name;
				if (components.size() > 1) {
					ArrayList<String> stripped = new ArrayList<>();
					stripped.addAll(components.subList(0, components.size() - 2));
					stripped.add(components.get(components.size() - 1));
					return mCorbaScopedRenderer.render(new ScopedName(stripped, true));
				} else {
					throw new CorbabindingException("Cannot strip enum name from fully qualified name " + mCorbaScopedRenderer.render(name) + ".");
				}
			}
		};
	}

	@Override
	protected ArrayList<String> writeConversionToJavaMembersInitialization(final LineWriter writer, final JavaStructProjection structProjection) throws IOException {
		ArrayList<String> localRefs = new ArrayList<>();
		JavaUnionProjection projection = (JavaUnionProjection) structProjection;

		for (final Member member : projection.members) {
			String name = member.declarator.name;
			if (!(member.type instanceof PrimitiveType)) {
				localRefs.add(name);
			}
			writer.writeln(mJniJavaTypeRenderer.render(member.type), " ", name, " = ", mJniJavaTypeRenderer.getEmptyValue(member.type), ";");
		}
		writer.writeln();

		writer.writeln(projection.typeMember.declarator.name, " = ", JniConfig.CONVERSION_FUNCTION, "(", JniConfig.ARG_JNI_ENV, ", ", JniConfig.CONVERSION_IN_ARG, "._d()", ")", ";");

		final boolean enumBased = !(projection.typeMember.type instanceof PrimitiveType);
		final SwitchData<Member> switchData = new SwitchDataBuilder<Member>()
				.withWriter(writer)
				.withEnumBased(enumBased)
				.withSwitchExpr(JniConfig.CONVERSION_IN_ARG + "._d()")
				.withFields(createDataForSwitch(projection))
				.withCaseRenderer(this::renderConversionToJavaCaseBody)
				.build();
		mSwitchRenderingUtility.writeUnionSwitch(switchData);

		return localRefs;
	}

	private void renderConversionToJavaCaseBody(final SwitchData<Member> data, final Data<UnionField, Member> field) throws IOException {
		LineWriter writer = data.writer;
		String name = field.extra.declarator.name;
		writer.writeln(
				name,
				" = ",
				JniConfig.CONVERSION_FUNCTION, "(" ,
				JniConfig.ARG_JNI_ENV, ", ",
				JniConfig.CONVERSION_IN_ARG, ".", name, "()",
				");");
		writer.writeln("break;");
	}

	@Override
	protected void renderConversionFromJava(final JavaProjection javaProjection) throws IOException {
		JavaUnionProjection projection = (JavaUnionProjection) javaProjection;
		LineWriter writer = mOutput.conversionImpl;
		Member typeMember = projection.typeMember;
		String typeMembername = typeMember.declarator.name;
		String jtypeMemberName = jvalueName(typeMembername);
		writeJavaFieldAccess(writer, javaProjection, typeMember.type, typeMembername, jtypeMemberName);
		writeConversionOfMemberFromJava(writer, projection, typeMember.type, typeMembername, jtypeMemberName);
		if (!(typeMember.type instanceof PrimitiveType)) {
			writeDeleteLocalRef(writer, jtypeMemberName);
		}

		writer.writeln();

		final boolean enumBased = !(projection.typeMember.type instanceof PrimitiveType);
		final SwitchData<Member> switchData = new SwitchDataBuilder<Member>()
				.withWriter(writer)
				.withEnumBased(enumBased)
				.withHasCasesInBlocks(true)
				.withSwitchExpr(typeMembername)
				.withFields(createDataForSwitch(projection))
				.withCaseRenderer(this::renderConversionFromJavaCaseBody)
				.withExtra(projection)
				.build();
		mSwitchRenderingUtility.writeUnionSwitch(switchData);

		writer.writeln();

		writer.writeln(JniConfig.CONVERSION_OUT_ARG, "._d(", typeMembername, ")", ";");
	}

	private void renderConversionFromJavaCaseBody(final SwitchData<Member> data, final Data<UnionField, Member> field) throws IOException {
		LineWriter writer = data.writer;
		JavaUnionProjection projection = (JavaUnionProjection) data.extra;
		String name = field.extra.declarator.name;
		String jname = jvalueName(name);
		Type type = field.extra.type;
		writeJavaFieldAccess(writer, projection, type, name, jname);
		if (type instanceof StringType) { // Needs special handling because of string being allocated dynamically. Since char * is not const, target should take ownership.
			writer.writeln("char * ", name, " = ", JniConfig.CONVERSION_FUNCTION, "<char>", "(", JniConfig.ARG_JNI_ENV, ", ", jname, ")", ";");
		} else {
			writeConversionOfMemberFromJava(writer, projection, field.data.type, name, jname); // use raw type -> takes care of typedefed sequences
		}
		if (!(type instanceof PrimitiveType)) {
			writeDeleteLocalRef(writer, jname);
		}
		writer.writeln(JniConfig.CONVERSION_OUT_ARG, ".", name, "(", name, ")", ";");
		writer.writeln("break;");
	}

	private void writeJavaFieldAccess(final LineWriter writer, final JavaProjection projection, final Type type, final String name, final String jname) throws IOException {
		writer.writeln(
				mJniJavaTypeRenderer.render(type), " ", jname,
				" = ",
				jniCall(
						mJniFieldAccessRenderer.getMethod(type),
						JniConfig.CONVERSION_IN_ARG,
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, name))
				),
				";");
	}

	private void writeConversionOfMemberFromJava(final LineWriter writer, JavaUnionProjection projection, final Type type, final String name, final String jname) throws IOException {
		if (type instanceof SequenceType) { // Needs special handling for anonymous types.
			final ScopedName anonymousType = ScopedName.nameInScope(projection.symbol.name,"_" + name + "_seq");
			writer.writeln(mCorbaScopedRenderer.render(anonymousType), " ", name, ";");
			logw("Handled anonymous sequence type " + type + ".");
		} else {
			writer.writeln(mCorbaTypeRenderer.render(type), " ", name, ";");
		}
		writer.writeln(
				JniConfig.CONVERSION_FUNCTION, "(", JniConfig.ARG_JNI_ENV, ", ", jname, ", ", name, ")", ";"
		);
	}

	private String jvalueName(final String name) {
		return "_j_" + name + "_";
	}

	private List<Data<UnionField, Member>> createDataForSwitch(final JavaUnionProjection union) {
		List<Data<UnionField, Member>> result = new ArrayList<>();
		for (final Member field : union.members) {
			UnionField unionField = union.fieldMap.get(field);
			if (unionField != null) {
				result.add(new Data<>(unionField, field));
			}
		}
		return result;
	}

}
