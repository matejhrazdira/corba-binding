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
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.util.CollectionUtil;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ExceptDcl extends StructType implements Definition {

	public ExceptDcl(final String name, final List<Member> members) {
		super(name, members);
	}

	@Override
	public ExceptDcl resolved(IdlElement scope, final SymbolResolver resolver) {
		return new ExceptDcl(name, resolvedImpl(resolver));
	}

}
