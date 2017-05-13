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

package com.matejhrazdira.corbabinding.idl.definitions;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class TypeDeclarator extends IdlElement implements Definition {

	public final Type type;
	public final List<Declarator> declarators; // TODO: not optimal, should be only Declarator but this fits better to parser

	public TypeDeclarator(final Type type, final List<Declarator> declarators) {
		this.type = Validator.assertNotNull(type, "Type cannot be empty.");
		this.declarators = Validator.notEmptyImmutableList(declarators, "Declarators cannot be empty.");
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		final IdlElement element = this;
		return declarators
				.stream()
				.map( (d) -> new Symbol(ScopedName.nameInScope(null, d.name), element) )
				.collect(Collectors.toList());
	}

	@Override
	public TypeDeclarator resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new TypeDeclarator(type.resolved(scope, resolver), declarators.stream().map( d -> d.resolved(scope, resolver)).collect(Collectors.toList()));
	}
}
