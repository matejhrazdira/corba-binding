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
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.CollectionUtil;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module extends IdlElement implements Definition {

	public final String name;
	public final List<Definition> definitions;

	public Module(final String name, final List<Definition> definitions) {
		this.name = Validator.assertNotEmpty(name, "Name cannot be empty.");
		this.definitions = CollectionUtil.immutableList(definitions);
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		ArrayList<Symbol> declaredSymbols = new ArrayList<>();
		List<String> myScope = Arrays.asList(name);
		declaredSymbols.add(new Symbol(new ScopedName(myScope, false), this));
		definitions.stream()
				.flatMap( (d) -> d.getDeclaredSymbols().stream() )
				.forEachOrdered( (s) -> declaredSymbols.add(s.withName(s.name.inScope(myScope))));
		return declaredSymbols;
	}

	@Override
	public Module resolved(IdlElement scope, final SymbolResolver resolver) {
		List<Definition> resolved = definitions.stream().map(d -> d.resolved(this, resolver)).collect(Collectors.toList());
		return new Module(name, resolved);
	}
}
