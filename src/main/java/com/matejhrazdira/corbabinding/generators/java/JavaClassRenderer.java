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

import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.ArrayType;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.List;

public abstract class JavaClassRenderer extends JavaWithMembersRenderer {

	private static final String BUILDER_NAME = "Builder";

	protected static class Field {
		public final String type;
		public final String name;
		public final Member member;

		public Field(final String type, final String name, final Member member) {
			this.type = type;
			this.name = name;
			this.member = member;
		}
	}

	protected static class RenderingData {
		public final LineWriter writer;
		public final ScopedName name;
		public final List<Field> fields;

		public RenderingData(final LineWriter writer, final ScopedName name, final List<Field> fields) {
			this.writer = writer;
			this.name = name;
			this.fields = fields;
		}
	}

	@FunctionalInterface
	protected interface SequenceAssignment {
		void write(LineWriter writer, String name, String type) throws IOException;
	}

	public JavaClassRenderer(final ScopedRenderer scopedRenderer, SymbolResolver resolver, final OutputListener outputListener) {
		super(scopedRenderer, resolver, outputListener);
	}

	@Override
	protected void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException {
		RenderingData data = createRenderingData(writer, symbol, name);
		writeStaticData(data);
		writeClassFields(data);
		writeClassCtors(data);
		writeClassMethods(data);
		writeBuilder(data);
	}

	protected void writeStaticData(final RenderingData data) throws IOException {

	}

	protected String getElementType() {
		return "class";
	}

	protected abstract RenderingData createRenderingData(final LineWriter writer, final Symbol symbol, final ScopedName name);

	private void writeClassFields(final RenderingData data) throws IOException {
		writeFields(data.writer, "public final ", data.fields);
	}

	protected void writeFields(final LineWriter writer, final String fieldSpec, final List<Field> fields) throws IOException {
		for (final Field f : fields) {
			writer.writeln(fieldSpec, f.type, " ", f.name, ";");
		}
		if (fields.size() > 0) {
			writer.writeln();
		}
	}

	private void writeClassCtors(final RenderingData data) throws IOException {
		writeCtors(data.writer, data.name.getBaseName(), data.fields, this::immutableAssignment);
	}

	protected void writeCtors(final LineWriter writer, final String name, final List<Field> fields, SequenceAssignment sequenceAssignment) throws IOException {
		writeNoArgCtor(writer, name, "public", fields, sequenceAssignment);
		writeFullArgCtor(writer, name, "public", fields, sequenceAssignment);
	}

	private void writeNoArgCtor(final LineWriter writer, final String name, final String specifier, final List<Field> fields, final SequenceAssignment sequenceAssignment) throws IOException {
		writer.writeln(specifier, " ", name, "() {");
		writer.increaseLevel();
		writeFieldInitializations(writer, fields, sequenceAssignment);
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	protected void writeFieldInitializations(final LineWriter writer, final List<Field> fields, final SequenceAssignment sequenceAssignment) throws IOException {
		for (Field f : fields) {
			writeFieldInitialization(writer, f, sequenceAssignment);
		}
	}

	private void writeFieldInitialization(final LineWriter writer, final Field f, SequenceAssignment sequenceAssignment) throws IOException {
		writer.write("this.", f.name, " = ");
		if (f.member.type instanceof SequenceType) {
			sequenceAssignment.write(writer, "", f.type);
		} else if (f.member.type instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) f.member.type;
			writer.write(
					"new ", mTypeRenderer.render(arrayType.elementType),
					"[", mExpressionRenderer.render(arrayType.size), "]"
			);
			if (arrayType.elementType instanceof ScopedName) {
				writer.write(";").endl();
				writer.write("java.util.Arrays.fill(", f.name, ", ");
				if (isEnum((ScopedName) arrayType.elementType)) {
					writer.write(mTypeRenderer.render(arrayType.elementType), ".values()[0]");
				} else {
					writer.write("new ", mTypeRenderer.render(arrayType.elementType), "()");
				}
				writer.write(")");
			}
		} else if (f.member.type instanceof PrimitiveType) {
			PrimitiveType primitiveType = (PrimitiveType) f.member.type;
			switch (primitiveType.type) {
				case BOOL:
					writer.write("false");
					break;
				default:
					writer.write("0");
					break;

			}
		} else if (f.member.type instanceof StringType) {
			writer.write("\"\"");
		} else {
			if (f.member.type instanceof ScopedName && isEnum((ScopedName) f.member.type)) {
				writer.write(mTypeRenderer.render(f.member.type), ".values()[0]");
			} else {
				writer.write("new ", mTypeRenderer.render(f.member.type), "()");
			}
		}
		writer.write(";").endl();
	}

	private boolean isEnum(ScopedName name) {
		Symbol symbol = mResolver.findSymbol(name);
		return symbol.element instanceof EnumType;
	}

	private void writeFullArgCtor(final LineWriter writer, final String name, final String specifier, final List<Field> fields, final SequenceAssignment sequenceAssignment) throws IOException {
		if (fields.isEmpty()) {
			return;
		}
		writer.write(specifier, " ", name, "(");
		writeCtorInputArgs(writer, fields);
		writer.write(") {").endl();
		writer.increaseLevel();
		writeCtorAssignments(writer, fields, sequenceAssignment);
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeCtorInputArgs(final LineWriter writer, final List<Field> fields) throws IOException {
		for (int i = 0; i < fields.size(); i++) {
			final Field f = fields.get(i);
			if (i > 0) {
				writer.write(", ");
			}
			writeCtorInputArg(writer, f);
		}
	}

	private void writeCtorInputArg(final LineWriter writer, final Field f) throws IOException {
		writer.write(f.type, " ", f.name);
	}

	private void writeCtorAssignments(final LineWriter writer, final List<Field> fields, SequenceAssignment sequenceAssignment) throws IOException {
		for (Field f : fields) {
			writeFieldAssignment(writer, f, sequenceAssignment);
		}
	}

	private void writeFieldAssignment(final LineWriter writer, final Field f, SequenceAssignment sequenceAssignment) throws IOException {
		if (f.member.type instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) f.member.type;
			writer.writeln(
					"if (", f.name, " == null || ", f.name, ".length != (", mExpressionRenderer.render(arrayType.size), ")) {"
			);
			writer.increaseLevel();
			writer.writeln(
					"throw new IllegalArgumentException(",
					f.name, " + \" must not be null and must have length \" + (", mExpressionRenderer.render(arrayType.size), ") + \". ",
					"Received \" + (", f.name, " != null ? ",
					"\"", mTypeRenderer.render(arrayType.elementType), "[\" + ", f.name, ".length + \"]\"",
					" : \"null\")", " + \".\"",
					");");
			writer.decreaseLevel();
			writer.writeln("}");
		}
		writer.write("this.", f.name, " = ");
		if (f.member.type instanceof SequenceType) {
			sequenceAssignment.write(writer, f.name, f.type);
		} else {
			writer.write(f.name);
		}
		writer.write(";").endl();
	}

	private void simpleAssignment(final LineWriter writer, final String name, final String type) throws IOException {
		writer.write(name);
	}

	private void immutableAssignment(final LineWriter writer, final String name, final String type) throws IOException {
		String elementTypeSpec = name.isEmpty() ? extractElementType(type) : "";
		String listCtor = "java.util.Collections.unmodifiableList(new java.util.ArrayList<" + elementTypeSpec + ">(";
		writeListAssignment(writer, name, listCtor, "))");
	}

	private String extractElementType(final String type) {
		int bgn = type.indexOf('<');
		int end = type.indexOf('>');
		if (bgn >= 0 && end > bgn) {
			return type.substring(bgn + 1, end);
		} else {
			logw("Failed to extract sequence element type from " + type + ", code might not compile on some platforms.");
			return "";
		}
	}

	private void mutableAssignment(final LineWriter writer, final String name, final String type) throws IOException {
		writeListAssignment(writer, name, "new java.util.ArrayList<>(", ")");
	}

	private void writeListAssignment(final LineWriter writer, final String name, final String listCtor, final String listCtorEnd) throws IOException {
		if (name.length() > 0) {
			writer.write(name, " != null ? ", listCtor, name, listCtorEnd, " : null");
		} else {
			writer.write(listCtor, listCtorEnd);
		}
	}

	private void writeClassMethods(final RenderingData data) throws IOException {
		LineWriter writer = data.writer;
		writer.writeln("public ", BUILDER_NAME, " ", "builder() {");
		writer.increaseLevel();
		writer.write("return ");
		writeCtorInvocation(writer, BUILDER_NAME, data.fields);
		writer.write(";").endl();
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeCtorInvocation(final LineWriter writer, final String name, final List<Field> fields) throws IOException {
		writer.write("new ", name, "(");
		for (int i = 0; i < fields.size(); i++) {
			final Field f = fields.get(i);
			if (i > 0) {
				writer.write(", ");
			}
			writer.write(f.name);
		}
		writer.write(")");
	}

	private void writeBuilder(final RenderingData data) throws IOException {
		final LineWriter writer = data.writer;
		final ScopedName name = data.name;
		final List<Field> fields = data.fields;
		writer.writeln("public static class ", BUILDER_NAME, " {");
		writer.increaseLevel();
		writer.writeln();
		writeFields(writer, "private ", fields);
		writeBuilderNoArgCtor(writer);
		if (fields.size() > 0) {
			writeFullArgCtor(writer, BUILDER_NAME, "private", fields, this::mutableAssignment); // TODO: maybe simple assignment could be sufficient as long as the builder doesn't provide getters
		}
		writeBuilderSetters(data);
		writeBuilderMethods(data);
		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeBuilderNoArgCtor(final LineWriter writer) throws IOException {
		writer.writeln("public ", BUILDER_NAME, "() {");
		writer.writeln("}");
		writer.writeln();
	}

	private void writeBuilderSetters(final RenderingData data) throws IOException {
		for (final Field f : data.fields) {
			writeBuilderSetter(data, f);
		}
	}

	private void writeBuilderSetter(final RenderingData data, final Field f) throws IOException {
		final LineWriter writer = data.writer;
		writer.write("public ", BUILDER_NAME, " ");
		writeBuilderSetterName(writer, f);
		writer.write("(", f.type, " ", f.name, ") {").endl();
		writer.increaseLevel();
		writeBuilderSetterAssignment(data, f);
		writer.writeln("return this;");
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeBuilderSetterName(final LineWriter writer, final Field f) throws IOException {
		writer.write("with", Character.toString(Character.toUpperCase(f.name.charAt(0))), f.name.substring(1, f.name.length()));
	}

	protected void writeBuilderSetterAssignment(final RenderingData data, final Field f) throws IOException {
		writeFieldAssignment(data.writer, f, this::simpleAssignment);
	}

	protected void writeBuilderMethods(final RenderingData data) throws IOException {
		writeBuilderBuildMethod(data.writer, data.name, data.fields);
	}

	private void writeBuilderBuildMethod(final LineWriter writer, final ScopedName name, final List<Field> fields) throws IOException {
		writer.writeln("public ", name.getBaseName(), " build() {");
		writer.increaseLevel();
		writer.write("return ");
		writeCtorInvocation(writer, name.getBaseName(), fields);
		writer.write(";").endl();
		writer.decreaseLevel();
		writer.writeln("}");
	}
}
