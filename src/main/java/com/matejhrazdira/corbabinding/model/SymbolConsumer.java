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

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.*;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.util.List;

public abstract class SymbolConsumer {

	public void consume(List<Symbol> symbols) { // TODO: replace with visitor pattern
		for (Symbol s : symbols) {
			IdlElement element = s.element;
			ScopedName name = s.name;
			if (element instanceof Module) {
				consume((Module) element, name);
			} else if (element instanceof ConstDcl) {
				consume((ConstDcl) element, name);
			} else if (element instanceof StructType) {
				consume((StructType) element, name);
			} else if (element instanceof UnionType) {
				consume((UnionType) element, name);
			} else if (element instanceof TypeDeclarator) {
				consume((TypeDeclarator) element, name);
			} else if (element instanceof ExceptDcl) {
				consume((ExceptDcl) element, name);
			}
		}
	}

	public abstract void consume(Module module, ScopedName name);
	public abstract void consume(ConstDcl constant, ScopedName name);
	public abstract void consume(StructType struct, ScopedName name);
	public abstract void consume(UnionType union, ScopedName name);
	public abstract void consume(TypeDeclarator typedef, ScopedName name);
	public abstract void consume(ExceptDcl exception, ScopedName name);
}
