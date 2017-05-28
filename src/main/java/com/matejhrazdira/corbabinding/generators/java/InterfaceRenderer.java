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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.definitions.Definition;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Declaration;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.interfaces.Param;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.idl.types.VoidType;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceRenderer extends JavaWithMembersRenderer implements JavaInterfaceProjectionProvider {

	public static final String NATIVE_ADDRESS = "_native_address_";
	public static final Type NATIVE_ADDRESS_TYPE = new PrimitiveType(PrimitiveType.Type.UNSIGNED_LONG_LONG_INT);
	public static final String DISPOSE_NAME = "_dispose_";

	private final String mVarType;
	private final String mCorbaExceptionType;
	private final String mAddressType;

	public InterfaceRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener, final String varType, final String corbaExceptionType) {
		super(scopedRenderer, resolver, outputListener);
		mVarType = varType;
		mCorbaExceptionType = corbaExceptionType;
		mAddressType = mTypeRenderer.render(NATIVE_ADDRESS_TYPE);
	}

	@Override
	public JavaInterfaceProjection getProjection(final Symbol symbol) {
		JavaProjection projection = super.getProjection(symbol);
		List<Member> members = Arrays.asList(new Member(new Declarator(NATIVE_ADDRESS), NATIVE_ADDRESS_TYPE));
		return new JavaInterfaceProjection(projection.symbol, projection.name, members, getJavaOperations((Declaration) symbol.element), getDestructorOperation());
	}

	@Override
	protected String getElementType() {
		return "class";
	}

	@Override
	protected String getInheritanceSpec(Symbol symbol) {
		Declaration declaration = (Declaration) symbol.element;
		if (declaration.inheritance.size() > 0) {
			return declaration.inheritance.stream()
					.map(n -> mScopedRenderer.render(n))
					.collect(Collectors.joining(", ", "extends ", ""));
		} else {
			return "";
		}
	}

	@Override
	protected void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException {
		Declaration declaration = (Declaration) symbol.element;
		writeFields(writer, symbol);
		writeCtor(writer, name, declaration);
		writeOperations(writer, declaration);
	}

	private void writeFields(final LineWriter writer, final Symbol symbol) throws IOException {
		writeInnerConstants(writer, symbol);
		writer.writeln("private ", mAddressType, " ", NATIVE_ADDRESS, ";");
		writer.writeln();
	}

	private void writeInnerConstants(final LineWriter writer, final Symbol symbol) throws IOException {
		Declaration declaration = (Declaration) symbol.element;
		for (Definition definition : declaration.innerDefinitions) {
			if (definition instanceof ConstDcl) {
				ConstDcl c = (ConstDcl) definition;
				Type javaType = getJavaType(c.type);
				writer.writeln("public static final ", mTypeRenderer.render(javaType), " ", c.name, " = ", mExpressionRenderer.render(c.value), ";");
			}
		}
		if (declaration.innerDefinitions.size() > 0) {
			writer.writeln();
		}
	}

	private void writeCtor(final LineWriter writer, final ScopedName name, final Declaration declaration) throws IOException {
		writer.writeln("public ", name.getBaseName(), "(", mAddressType, " ", NATIVE_ADDRESS, ") {");
		writer.increaseLevel();
		if (declaration.inheritance.size() > 0) {
			writer.writeln("super(", NATIVE_ADDRESS, ");");
		}
		writer.writeln("this.", NATIVE_ADDRESS, " = ", NATIVE_ADDRESS, ";");
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeOperations(final LineWriter writer, final Declaration declaration) throws IOException {
		List<Operation> allOperations = new ArrayList<>();
		allOperations.add(getDestructorOperation());
		allOperations.addAll(getJavaOperations(declaration));
		for (final Operation operation : allOperations) {
			writeOperation(writer, operation);
			writer.writeln();
		}
	}

	private Operation getDestructorOperation() {
		return new Operation(DISPOSE_NAME, false, new VoidType(), Collections.emptyList(), Collections.emptyList());
	}

	private List<Operation> getJavaOperations(final Declaration declaration) {
		return declaration.operations.stream()
				.map(o -> new Operation(
								o.name,
								o.oneway,
								getJavaType(o.returnType),
								o.params.stream()
										.map(p -> new Param(p.name, p.direction, getJavaType(p.type)))
										.collect(Collectors.toList()),
								o.exceptions.stream()
										.map(e -> (ScopedName) getJavaType(e))
										.collect(Collectors.toList())
						)
				)
				.collect(Collectors.toList());
	}

	private void writeOperation(final LineWriter writer, final Operation operation) throws IOException {
		writer.write("public native ", mTypeRenderer.render(operation.returnType), " ", operation.name, "(");
		final List<Param> params = operation.params;
		for (int i = 0; i < params.size(); i++) {
			if (i > 0) {
				writer.write(", ");
			}
			final Param param = params.get(i);
			if (param.direction == Param.Direction.IN) {
				writer.write(mTypeRenderer.render(param.type));
			} else {
				writer.write(mVarType, "<", mTypeRenderer.renderGenericType(param.type), ">");
			}
			writer.write(" ", param.name);
		}
		writer.write(")");
		final List<ScopedName> exceptions = operation.exceptions;
		writer.write(" throws ", mCorbaExceptionType);
		for (ScopedName exception : exceptions) {
			writer.write(", ");
			writer.write(mScopedRenderer.render(exception));
		}
		writer.write(";");
		writer.endl();
	}
}
