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
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

public class Param extends IdlElement implements Resolvable {

	public enum Direction {
		IN, OUT, IN_OUT
	}

	public final String name;
	public final Direction direction;
	public final Type type;

	public Param(final String name, final Direction direction, final Type type) {
		this.name = Validator.assertNotEmpty(name, "Parameter name cannot be empty.");
		this.direction = Validator.assertNotNull(direction, "Parameter direction must be specified.");
		this.type = Validator.assertNotNull(type, "Parameter type must be specified.");
	}

	@Override
	public Param resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new Param(name, direction, type.resolved(scope, resolver));
	}
}
