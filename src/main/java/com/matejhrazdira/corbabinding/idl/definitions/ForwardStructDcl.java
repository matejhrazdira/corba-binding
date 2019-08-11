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

import java.util.Collections;
import java.util.List;

public class ForwardStructDcl extends StructType {

	public ForwardStructDcl(String name) {
		super(name, Collections.emptyList());
	}

	@Override
	public List<Symbol> getDeclaredSymbols() {
		return Collections.emptyList();
	}

	@Override
	public StructType resolved(IdlElement scope, SymbolResolver resolver) {
		return this;
	}
}
