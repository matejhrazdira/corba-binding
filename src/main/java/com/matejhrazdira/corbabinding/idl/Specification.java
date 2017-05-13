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

package com.matejhrazdira.corbabinding.idl;

import com.matejhrazdira.corbabinding.idl.definitions.Definition;
import com.matejhrazdira.corbabinding.util.CollectionUtil;

import java.util.List;
import java.util.stream.Collectors;

public class Specification extends IdlElement implements Resolvable {

	public final String fileName;
	public final List<Definition> definitions;

	public Specification(final String fileName, final List<Definition> definitions) {
		this.fileName = fileName;
		this.definitions = CollectionUtil.immutableList(definitions);
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		return definitions.stream()
				.flatMap( (d) -> d.getDeclaredSymbols().stream() )
				.map( (s) -> s.withName(s.name.fullyQualified()) )
				.collect(Collectors.toList());
	}

	@Override
	public Specification resolved(IdlElement scope, final SymbolResolver resolver) {
		List<? extends Resolvable> resolved = definitions.stream().map(d -> d.resolved(this, resolver)).collect(Collectors.toList());
		return new Specification(fileName, (List<Definition>) resolved);
	}
}
