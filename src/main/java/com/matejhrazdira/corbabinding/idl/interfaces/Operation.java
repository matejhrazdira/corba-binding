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
import com.matejhrazdira.corbabinding.idl.Resolvable;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Operation extends IdlElement implements Resolvable {

	public final String name;
	public final boolean oneway;
	public final Type returnType;
	public final List<Param> params;
	public final List<ScopedName> exceptions;

	public Operation(final String name, final boolean oneway, final Type returnType, final List<Param> params, final List<ScopedName> exceptions) {
		this.name = Validator.assertNotEmpty(name, "Operation name cannot be empty.");
		this.oneway = oneway;
		this.returnType = Validator.assertNotNull(returnType, "Return type must be specified.");
		this.params = Collections.unmodifiableList(params);
		this.exceptions = Collections.unmodifiableList(exceptions);
	}

	@Override
	public Operation resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new Operation(
				name,
				oneway,
				returnType.resolved(scope, resolver),
				params.stream()
						.map(p -> p.resolved(scope, resolver))
						.collect(Collectors.toList()),
				exceptions.stream()
						.map(e -> e.resolved(scope, resolver))
						.collect(Collectors.toList())
		);
	}
}
