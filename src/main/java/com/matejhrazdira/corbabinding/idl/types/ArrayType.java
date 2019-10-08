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

package com.matejhrazdira.corbabinding.idl.types;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;

/**
 * According to grammar definition this is not a type. Arrays are declared
 * by array declarator but for processing purposes it is better to treat them
 * as separate type.
 */
public class ArrayType implements Type {

	public final Type elementType;
	public final ConstExp size;

	public ArrayType(final Type elementType, final ConstExp size) {
		this.elementType = elementType;
		if (size == null) {
			throw new IllegalArgumentException("Size must be specified.");
		}
		this.size = size;
	}

	@Override
	public ArrayType resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new ArrayType(elementType.resolved(scope, resolver), size.resolved(scope, resolver));
	}

	public void accept(TypeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ArrayType<" + elementType + ">";
	}
}
