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
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.Arrays;
import java.util.List;

public class ConstDcl extends IdlElement implements Definition {

	public final String name;
	public final Type type;
	public final ConstExp value;

	public ConstDcl(final String name, final Type type, final ConstExp value) {
		this.name = Validator.assertNotEmpty(name, "Name cannot be empty.");
		this.type = Validator.assertNotNull(type, "Type cannot be null.");
		this.value = Validator.assertNotNull(value, "Value cannot be empty.");
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		return Arrays.asList(new Symbol(ScopedName.nameInScope(null, name), this));
	}

	@Override
	public ConstDcl resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new ConstDcl(name, type.resolved(scope, resolver), value.resolved(scope, resolver));
	}
}
