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
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.definitions.Definition;
import com.matejhrazdira.corbabinding.idl.definitions.ExceptDcl;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Declaration;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.interfaces.Param;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceDeclRenderer extends JavaWithMembersRenderer {

	protected final ExceptionRenderer mExceptionRenderer;
	protected final String mVarType;
	protected final String mCorbaExceptionType;
	protected final String mDisposableType;

	public InterfaceDeclRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener, final ExceptionRenderer exceptionRenderer, final String varType, final String corbaExceptionType, String disposableType) {
		super(scopedRenderer, resolver, outputListener);
		mExceptionRenderer = exceptionRenderer;
		mVarType = varType;
		mCorbaExceptionType = corbaExceptionType;
		mDisposableType = disposableType;
	}

	@Override
	protected String getElementType() {
		return "interface";
	}

	@Override
	protected String getInheritanceSpec(Symbol symbol) {
		Declaration declaration = (Declaration) symbol.element;
		String suffix = "extends " + mDisposableType;
		if (declaration.inheritance.size() > 0) {
			return declaration.inheritance.stream()
					.map(n -> mScopedRenderer.render(n))
					.collect(Collectors.joining(", ", "extends ", ", " + mDisposableType));
		} else {
			return suffix;
		}
	}

	@Override
	protected void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException {
		Declaration declaration = (Declaration) symbol.element;
		writeFields(writer, symbol);
		writeCtor(writer, name, declaration);
		writeOperations(writer, declaration);
	}

	protected void writeFields(final LineWriter writer, final Symbol symbol) throws IOException {
		writeInnerConstants(writer, symbol);
	}

	private void writeInnerConstants(final LineWriter writer, final Symbol symbol) throws IOException {
		Declaration declaration = (Declaration) symbol.element;
		List<ConstDcl> constants = filterDeclarations(declaration, ConstDcl.class);
		for (final ConstDcl c : constants) {
			Type javaType = getJavaType(c.type);
			writer.writeln("public static final ", mTypeRenderer.render(javaType), " ", c.name, " = ", mExpressionRenderer.render(c.value), ";");
		}
		if (constants.size() > 0) {
			writer.writeln();
		}
		List<ExceptDcl> exceptions = filterDeclarations(declaration, ExceptDcl.class);
		for (final ExceptDcl e : exceptions) {
			Symbol innerSymbol = new Symbol(ScopedName.nameInScope(symbol.name, e.name), e).asInner();
			mExceptionRenderer.renderStatic(writer, innerSymbol);
			writer.writeln();
		}
	}

	private <T extends Definition> List<T> filterDeclarations(final Declaration declaration, Class<T> cls) {
		return declaration.innerDefinitions.stream()
				.filter(cls::isInstance)
				.map(def -> (T) def)
				.collect(Collectors.toList());
	}

	protected void writeCtor(final LineWriter writer, final ScopedName name, final Declaration declaration) throws IOException {}

	private void writeOperations(final LineWriter writer, final Declaration declaration) throws IOException {
		List<Operation> allOperations = getAllOperations(declaration);
		for (final Operation operation : allOperations) {
			writeOperation(writer, operation);
			writer.writeln();
		}
	}

	protected List<Operation> getAllOperations(final Declaration declaration) {
		List<Operation> allOperations = new ArrayList<>();
		allOperations.addAll(getJavaOperations(declaration));
		return allOperations;
	}

	protected List<Operation> getJavaOperations(final Declaration declaration) {
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

	protected void writeOperation(final LineWriter writer, final Operation operation) throws IOException {
		writer.write(getOperationSpec(), mTypeRenderer.render(operation.returnType), " ", operation.name, "(");
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

	protected String getOperationSpec() {
		return "public ";
	}
}
