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

package com.matejhrazdira.corbabinding.idl.definitions.unionmembers;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Resolvable;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class UnionField extends IdlElement implements Resolvable {

	public final Type type;
	public final Declarator declarator;
	public final List<CaseLabel> labels;

	public UnionField(final Type type, final Declarator declarator, final List<CaseLabel> labels) {
		this.type = Validator.assertNotNull(type, "Type cannot be null.");
		this.declarator = Validator.assertNotNull(declarator, "Declarator cannot be null.");
		this.labels = Validator.notEmptyImmutableList(labels, "Labels cannot be empty.");
	}

	@Override
	public UnionField resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new UnionField(
				type.resolved(scope, resolver),
				declarator.resolved(scope, resolver),
				labels.stream().map( l -> l.resolved(scope, resolver)).collect(Collectors.toList())
		);
	}
}
