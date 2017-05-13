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

package com.matejhrazdira.corbabinding.generators.java;

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.generators.SwitchData;
import com.matejhrazdira.corbabinding.generators.SwitchData.SwitchDataBuilder;
import com.matejhrazdira.corbabinding.generators.SwitchRenderingUtility;
import com.matejhrazdira.corbabinding.generators.SwitchRenderingUtility.CaseRenderer;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.UnionType;
import com.matejhrazdira.corbabinding.idl.definitions.members.ArrayDeclarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.CaseLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.DefaultLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.ExpressionLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Data;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionRenderer extends JavaClassRenderer implements JavaUnionProjectionProvider {

	private static final String TYPE_FIELD_NAME = "_type_";
	private static final String DEFAULT_TYPE_VALUE = "_default_type_";

	private final SwitchRenderingUtility mUnionUtility;

	public UnionRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener) {
		super(scopedRenderer, resolver, outputListener);
		mUnionUtility = new SwitchRenderingUtility(mExpressionRenderer);
	}

	@Override
	public JavaUnionProjection getProjection(final Symbol symbol) {
		JavaProjection projection = super.getProjection(symbol);
		UnionMembersInfo memberInfo = getMemberInfo(symbol);
		ArrayList<Member> members = new ArrayList<>();
		members.add(memberInfo.typeMember);
		members.addAll(memberInfo.valueMembers);
		return new JavaUnionProjection.JavaUnionProjectionBuilder()
				.withSymbol(projection.symbol)
				.withName(projection.name)
				.withMembers(members)
				.withTypeMember(memberInfo.typeMember)
				.withFieldMap(memberInfo.fieldMap)
				.build();
	}

	private UnionMembersInfo getMemberInfo(final Symbol symbol) {
		final UnionType union = (UnionType) symbol.element;

		Type typeMemberJavaType = getJavaType(union.type);
		final Member typeMember = new Member(
				new Declarator(TYPE_FIELD_NAME),
				typeMemberJavaType
		);

		final List<Member> valueMembers = new ArrayList<>();
		final Map<Member, UnionField> fieldMap = new HashMap<>();
		for (final UnionField field : union.fields) {
			if (field.declarator instanceof ArrayDeclarator) {
				throw new CorbabindingException(
						"Can't generate field " + field.declarator.name +
								" in " + symbol.name.getQualifiedName() +
								" : Array declarators are not supported");
			}
			Type javaType = getJavaType(field.type);
			Member member = new Member(field.declarator, javaType);
			valueMembers.add(member);
			fieldMap.put(member, field);
		}

		return new UnionMembersInfo(typeMember, valueMembers, fieldMap);
	}

	@Override
	protected RenderingData createRenderingData(final LineWriter writer, final Symbol symbol, final ScopedName name) {
		UnionMembersInfo memberInfo = getMemberInfo(symbol);

		final List<Field> fields = new ArrayList<>();

		Field typeField = new Field(
				mTypeRenderer.render(memberInfo.typeMember.type),
				memberInfo.typeMember.declarator.name,
				memberInfo.typeMember);

		fields.add(typeField);

		final Map<Field, UnionField> fieldMap = new HashMap<>();
		for (final Member member : memberInfo.valueMembers) {
			Field f = new Field(
					mTypeRenderer.render(member.type),
					member.declarator.name,
					member
			);
			fields.add(f);
			fieldMap.put(f, memberInfo.fieldMap.get(member));
		}

		return new UnionRenderingData(writer, (UnionType) symbol.element, name, memberInfo.typeMember.type, fields, fieldMap);
	}

	@Override
	protected void writeStaticData(final RenderingData data) throws IOException {
		UnionRenderingData unionData = (UnionRenderingData) data;
		if (hasDefaultOnlyField(unionData.union)) {
			LineWriter writer = data.writer;
			writeDefaultTypeValue(writer, unionData);
			writeStaticInitBlock(writer, unionData);
		}
	}

	private boolean hasDefaultOnlyField(final UnionType union) {
		for (final UnionField field : union.fields) {
			if (field.labels.get(0) instanceof DefaultLabel) {
				return true;
			}
		}
		return false;
	}

	private void writeDefaultTypeValue(final LineWriter writer, final UnionRenderingData data) throws IOException {
		writer.writeln("private static final ", mTypeRenderer.render(data.unionType), " ", DEFAULT_TYPE_VALUE, ";");
		writer.writeln();
	}

	private void writeStaticInitBlock(final LineWriter writer, final UnionRenderingData data) throws IOException {
		writer.writeln("static {");
		writer.increaseLevel();
		writeDefaultInitializer(writer, data);
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeDefaultInitializer(final LineWriter writer, final UnionRenderingData data) throws IOException {
		String typeStr = mTypeRenderer.render(data.unionType);
		String typeVar = "type";
		writer.writeln(typeStr, " ", typeVar, " = ", mTypeRenderer.getEmptyValue(data.unionType), ";");
		String loopVar = "value";
		if (data.isEnumBased) {
			writeEnumDefaultInitializerLoop(writer, typeStr, loopVar);
		} else {
			writePrimitiveDefaultInitializerLoop(writer, (PrimitiveType) data.unionType, typeStr, loopVar);
		}
		writer.increaseLevel();
		writeUnionSwitch(writer, loopVar, data, this::writeDefaultInitializerCaseBody);
		writer.writeln(typeVar, " = ", loopVar, ";");
		writer.writeln("break;");
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln(DEFAULT_TYPE_VALUE, " = ", typeVar, ";");
	}

	private void writeEnumDefaultInitializerLoop(final LineWriter writer, final String typeStr, final String loopVar) throws IOException {
		writer.writeln("for (", typeStr, " ", loopVar, " : ", typeStr, ".values()) {");
	}

	private void writePrimitiveDefaultInitializerLoop(final LineWriter writer, final PrimitiveType type, final String typeStr, final String loopVar) throws IOException {
		writer.writeln("for (",
				typeStr, " ", loopVar, " = ", mTypeRenderer.getMinValue(type), "; ",
				loopVar, " < ", mTypeRenderer.getMaxValue(type), "; ",
				loopVar, "++", ") {");
	}

	private void writeDefaultInitializerCaseBody(final SwitchData<Field> data, Data<UnionField, Field> fieldData) throws IOException {
		LineWriter writer = data.writer;
		if (fieldData.data.labels.get(0) instanceof DefaultLabel) {
			writer.writeln("break;");
		} else {
			writer.writeln("continue;");
		}
	}

	@Override
	protected void writeFieldInitializations(final LineWriter writer, final List<Field> fields, final SequenceAssignment sequenceAssignment) throws IOException {
		// TODO: super implementation might not be correct for all cases, because ALL fields (including __type__) will be default initialized.
		super.writeFieldInitializations(writer, fields, sequenceAssignment);
	}

	@Override
	protected void writeBuilderSetterAssignment(final RenderingData data, final Field f) throws IOException {
		UnionRenderingData unionData = (UnionRenderingData) data;
		UnionField unionField = unionData.fieldMap.get(f);
		final LineWriter writer = data.writer;
		if (unionField != null) {
			writer.writeln("reset();");
			super.writeBuilderSetterAssignment(data, f);
			writer.write("this.", TYPE_FIELD_NAME, " = ");
			CaseLabel label = unionField.labels.get(0);
			if (label instanceof ExpressionLabel) {
				writer.write(mExpressionRenderer.render(((ExpressionLabel) label).expression));
			} else {
				writer.write(DEFAULT_TYPE_VALUE);
			}
			writer.write(";").endl();
		} else {
			// TODO: unlike ACE+TAO reset and keep only field that matches the type
			super.writeBuilderSetterAssignment(data, f);
		}
	}

	@Override
	protected void writeBuilderMethods(final RenderingData data) throws IOException {
		super.writeBuilderMethods(data);
		data.writer.writeln();
		writeResetMethod(data);
	}

	private void writeResetMethod(final RenderingData data) throws IOException {
		final LineWriter writer = data.writer;
		UnionRenderingData union = (UnionRenderingData) data;

		writer.writeln("private void reset() {");
		writer.increaseLevel();

		if (union.isEnumBased) {
			writeTypeInitializedCheck(writer);
		}

		writeUnionSwitch(writer, TYPE_FIELD_NAME, union, this::writeResetCaseBody);

		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeUnionSwitch(final LineWriter writer, final String typeFieldName, final UnionRenderingData union, final CaseRenderer<Field> writeResetCaseBody) throws IOException {
		SwitchData<Field> data = new SwitchDataBuilder<Field>()
				.withWriter(writer)
				.withEnumBased(union.isEnumBased)
				.withSwitchExpr(typeFieldName)
				.withFields(createDataForSwitch(union))
				.withCaseRenderer(writeResetCaseBody)
				.build();
		mUnionUtility.writeUnionSwitch(data);
	}

	private List<Data<UnionField, Field>> createDataForSwitch(final UnionRenderingData union) {
		List<Data<UnionField, Field>> result = new ArrayList<>();
		for (final Field field : union.fields) {
			UnionField unionField = union.fieldMap.get(field);
			if (unionField != null) {
				result.add(new Data<>(unionField, field));
			}
		}
		return result;
	}

	private void writeResetCaseBody(final SwitchData<Field> data, Data<UnionField, Field> fieldData) throws IOException {
		LineWriter writer = data.writer;
		writer.writeln(fieldData.extra.name, " = ", mTypeRenderer.getEmptyValue(fieldData.extra.member.type), ";");
		writer.writeln("break;");
	}


	private void writeTypeInitializedCheck(final LineWriter writer) throws IOException {
		writer.writeln("if (", TYPE_FIELD_NAME, " == null) {");
		writer.increaseLevel().writeln("return;").decreaseLevel();
		writer.writeln("}");
	}

	private static class UnionRenderingData extends RenderingData {

		public final UnionType union;
		public final Type unionType;
		public final boolean isEnumBased;
		public final Map<Field, UnionField> fieldMap;

		public UnionRenderingData(final LineWriter writer, final UnionType union, final ScopedName name, final Type unionType, final List<Field> fields, final Map<Field, UnionField> unionFieldMap) {
			super(writer, name, fields);
			this.union = union;
			this.unionType = unionType;
			this.isEnumBased = unionType instanceof ScopedName;
			this.fieldMap = unionFieldMap;
		}
	}

	private static class UnionMembersInfo {

		public final Member typeMember;
		public final List<Member> valueMembers;
		public final Map<Member, UnionField> fieldMap;

		public UnionMembersInfo(final Member typeMember, final List<Member> valueMembers, final Map<Member, UnionField> fieldMap) {
			this.typeMember = typeMember;
			this.valueMembers = valueMembers;
			this.fieldMap = fieldMap;
		}
	}

}
