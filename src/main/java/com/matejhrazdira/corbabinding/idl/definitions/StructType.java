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
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.CollectionUtil;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StructType extends IdlElement implements Definition {

	public final String name;
	public final List<Member> members;

	public StructType(final String name, final List<Member> members) {
		this.name = Validator.assertNotEmpty(name, "Name cannot be empty.");
		this.members = CollectionUtil.immutableList(members);
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		return Arrays.asList(new Symbol(ScopedName.nameInScope(null, name), this));
	}

	@Override
	public StructType resolved(IdlElement scope, final SymbolResolver resolver) {
		List<Member> resolved = resolvedImpl(resolver);
		return new StructType(name, resolved);
	}

	protected List<Member> resolvedImpl(final SymbolResolver resolver) {
		return members.stream().map( m -> m.resolved(this, resolver)).collect(Collectors.toList());
	}
}
