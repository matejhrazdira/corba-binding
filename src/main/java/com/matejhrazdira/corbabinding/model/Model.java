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

package com.matejhrazdira.corbabinding.model;

import com.matejhrazdira.corbabinding.idl.Specification;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Model {

	public final List<String> mFiles;
	public final List<Symbol> mSymbols;
	public final SymbolResolver mResolver;

	public Model(Specification... specifications) {
		this(Arrays.asList(specifications));
	}

	public Model(List<Specification> specifications) {
		mFiles = specifications.stream()
				.map(specification -> specification.fileName)
				.collect(Collectors.toList());
		List<Symbol> allSymbols = specifications.stream()
				.flatMap(s -> s.getDeclaredSymbols().stream())
				.collect(Collectors.toList());
		SymbolResolver tmpResolver = new SymbolResolver(allSymbols);
		mSymbols = specifications.stream()
				.flatMap(s -> s.resolved(null, tmpResolver).getDeclaredSymbols().stream())
				.collect(Collectors.toList());
		mResolver = new SymbolResolver(mSymbols);
	}

	public List<Symbol> getSymbols() {
		return mSymbols;
	}

	public SymbolResolver getResolver() {
		return mResolver;
	}
}
