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

package com.matejhrazdira.corbabinding.idl.definitions.members;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Resolvable;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

public class Member extends IdlElement implements Resolvable {

	public final Declarator declarator;
	public final Type type;

	public Member(final Declarator declarator, final Type type) {
		this.declarator = Validator.assertNotNull(declarator, "Declarator cannot be null.");
		this.type = Validator.assertNotNull(type, "Type cannot be null.");
	}

	@Override
	public Member resolved(IdlElement scope, final SymbolResolver resolver) {
		return new Member(declarator.resolved(scope, resolver), type.resolved(scope, resolver));
	}
}
