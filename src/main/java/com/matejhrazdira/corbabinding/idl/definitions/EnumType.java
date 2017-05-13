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
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.idl.types.TypeVisitor;
import com.matejhrazdira.corbabinding.util.CollectionUtil;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnumType extends IdlElement implements Definition, Type {

	public class EnumValue extends IdlElement {

		public final String name;

		private EnumValue(final String name) {
			this.name = Validator.assertNotEmpty(name, "Name cannot be empty.");
		}

		public EnumType getEnum() {
			return EnumType.this;
		}
	}

	public final String name;
	public final List<EnumValue> values;

	public EnumType(final String name, final List<String> values) {
		this.name = Validator.assertNotEmpty(name, "Name cannot be empty.");
		List<EnumValue> enumValues = values.stream().map((n) -> new EnumValue(n)).collect(Collectors.toList());
		this.values = CollectionUtil.immutableList(enumValues);
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		ArrayList<Symbol> declaredSymbols = new ArrayList<>(values.size() + 1);
		ScopedName enumName = ScopedName.nameInScope(null, name);
		declaredSymbols.add(new Symbol(enumName, this));
		values.stream().forEachOrdered( (v) -> declaredSymbols.add(new Symbol(ScopedName.nameInScope(enumName, v.name), v)));
		return declaredSymbols;
	}

	@Override
	public EnumType resolved(final IdlElement scope, final SymbolResolver resolver) {
		return this;
	}

	public void accept(TypeVisitor visitor) {
		visitor.visit(this);
	}
}
