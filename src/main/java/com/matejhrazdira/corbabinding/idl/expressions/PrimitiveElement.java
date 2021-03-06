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

package com.matejhrazdira.corbabinding.idl.expressions;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.util.Validator;

public class PrimitiveElement extends IdlElement implements ExpressionElement {

	public enum Type {
		OR,
		XOR,
		AND,
		SHIFT_RIGHT,
		SHIFT_LEFT,
		ADD,
		SUBTRACT,
		MULTIPLY,
		DIVIDE,
		MODULO,
		UNARY_PLUS,
		UNARY_MINUS,
		UNARY_NEGATE,
		OPENING_BRACKET,
		CLOSING_BRACKET
	}

	public final Type type;

	public PrimitiveElement(final Type type) {
		this.type = Validator.assertNotNull(type, "Type cannot be null.");
	}

	public void accept(ExpressionElementVisitor visitor) {
		visitor.visit(this);
	}
}
