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

package com.matejhrazdira.corbabinding.idl.interfaces;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.definitions.Definition;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Declaration extends Interface {

	public final List<Definition> innerDefinitions;
	public final List<ScopedName> inheritance;
	public final List<Operation> operations;

	public Declaration(final String name, final List<ScopedName> inheritance, final List<IdlElement> exports) {
		this(
				name,
				inheritance,
				exports.stream()
						.filter(it -> it instanceof Operation)
						.map(it -> (Operation) it)
						.collect(Collectors.toList()),
				exports.stream()
						.filter(it -> it instanceof Definition)
						.map(it -> (Definition) it)
						.collect(Collectors.toList())
		);
	}

	private Declaration(final String name, final List<ScopedName> inheritance, final List<Operation> operations, final List<Definition> definitions) {
		super(name);
		this.inheritance = Collections.unmodifiableList(inheritance);
		this.operations = Collections.unmodifiableList(operations);
		this.innerDefinitions = Collections.unmodifiableList(definitions);
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		ArrayList<Symbol> result = new ArrayList<>();
		List<String> myScope = Arrays.asList(name);
		result.add(new Symbol(ScopedName.nameInScope(null, name), this));
		innerDefinitions.stream()
				.flatMap(d -> d.getDeclaredSymbols().stream())
				.map(s -> s.withName(s.name.inScope(myScope)).asInner())
				.forEachOrdered(s -> result.add(s));
		return result;
	}

	@Override
	public Declaration resolved(IdlElement scope, final SymbolResolver resolver) {
		return new Declaration(
				name,
				inheritance.stream()
						.map(n -> n.resolved(this, resolver))
						.collect(Collectors.toList()),
				operations.stream()
						.map(op -> op.resolved(this, resolver))
						.collect(Collectors.toList()),
				innerDefinitions.stream()
						.map(op -> op.resolved(this, resolver))
						.collect(Collectors.toList())
				);
	}
}
