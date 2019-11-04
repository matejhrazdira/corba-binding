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
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Declaration;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.idl.types.VoidType;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InterfaceImplRenderer extends InterfaceDeclRenderer implements JavaInterfaceProjectionProvider {

	public static final String NATIVE_ADDRESS = "_native_address_";
	public static final Type NATIVE_ADDRESS_TYPE = new PrimitiveType(PrimitiveType.Type.UNSIGNED_LONG_LONG_INT);

	private final String mAddressType;

	public InterfaceImplRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener, final ExceptionRenderer exceptionRenderer, final String varType, final String corbaExceptionType, String disposableType) {
		super(scopedRenderer, resolver, outputListener, exceptionRenderer, varType, corbaExceptionType, disposableType);
		mAddressType = mTypeRenderer.render(NATIVE_ADDRESS_TYPE);
	}

	@Override
	public JavaInterfaceProjection getProjection(final Symbol symbol) {
		JavaProjection projection = super.getProjection(symbol);
		List<Member> members = Arrays.asList(new Member(new Declarator(NATIVE_ADDRESS), NATIVE_ADDRESS_TYPE));
//		return new JavaInterfaceProjection(projection.symbol, projection.name, members, getJavaOperations((Declaration) symbol.element), getDestructorOperation());
		return new JavaInterfaceProjection(projection.symbol, super.getJavaSymbolName(symbol), members, getJavaOperations((Declaration) symbol.element), getDestructorOperation(), getJavaSymbolName(symbol));
	}

	@Override
	protected String getElementType() {
		return "class";
	}

	@Override
	protected ScopedName getJavaSymbolName(final Symbol symbol) {
		return mScopedRenderer.getNameInScope(getClientImplName(symbol.name));
	}

	public ScopedName getClientImplName(final ScopedName name) {
		return name.moveToScope(PojoGenerator.CB_CLIENT_IMPL_PACKAGE);
	}

	@Override
	protected String getInheritanceSpec(Symbol symbol) {
		Declaration declaration = (Declaration) symbol.element;
		String suffix = "implements " + mScopedRenderer.render(symbol.name);
		if (declaration.inheritance.size() > 0) {
			return declaration.inheritance.stream()
					.map(n -> mScopedRenderer.render(getClientImplName(n)))
					.collect(Collectors.joining(", ", "extends ", " " + suffix));
		} else {
			return suffix;
		}
	}

	@Override
	protected void writeFields(final LineWriter writer, final Symbol symbol) throws IOException {
		writer.writeln("private ", mAddressType, " ", NATIVE_ADDRESS, ";");
		writer.writeln();
	}

	@Override
	protected void writeCtor(final LineWriter writer, final ScopedName name, final Declaration declaration) throws IOException {
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

	@Override
	protected List<Operation> getAllOperations(final Declaration declaration) {
		List<Operation> allOperations = super.getAllOperations(declaration);
		allOperations.add(0, getDestructorOperation());
		return allOperations;
	}

	private Operation getDestructorOperation() {
		return new Operation(TemplateRenderer.DISPOSABLE_DISPOSE_NAME, false, new VoidType(), Collections.emptyList(), Collections.emptyList());
	}

	@Override
	protected void writeOperation(final LineWriter writer, final Operation operation) throws IOException {
		writer.writeln("@Override");
		super.writeOperation(writer, operation);
	}

	@Override
	protected String getOperationSpec() {
		return "public native ";
	}
}
