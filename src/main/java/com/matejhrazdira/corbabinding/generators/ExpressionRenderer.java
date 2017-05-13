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

package com.matejhrazdira.corbabinding.generators;

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.*;

import static com.matejhrazdira.corbabinding.generators.java.StringBuilderSupplier.asString;

public class ExpressionRenderer {

	protected final ScopedRenderer mScopedRenderer;

	public ExpressionRenderer(ScopedRenderer scopedRenderer) {
		mScopedRenderer = scopedRenderer;
	}

	public String render(final ConstExp value) {
		return asString(this::render, value);
	}

	public void render(StringBuilder result, final ConstExp value) {
		ExpressionElementVisitor visitor = new ElementVisitor(result);
		for (final ExpressionElement element : value.elements) {
			element.accept(visitor);
		}
	}

	protected void render(final StringBuilder result, final ScopedName element) {
		mScopedRenderer.render(result, element);
	}

	protected void render(final StringBuilder result, final Literal element) {
		result.append(element.value);
	}

	protected void render(final StringBuilder result, final BooleanLiteral element) {
		result.append(element.value ? "true" : "false");
	}

	protected void render(final StringBuilder result, final PrimitiveElement element) {
		PrimitiveElement primitive = element;
		switch (primitive.type) {
			case OR:
				result.append(" || ");
				break;
			case XOR:
				result.append(" ^ ");
				break;
			case AND:
				result.append(" && ");
				break;
			case SHIFT_RIGHT:
				result.append(" >> ");
				break;
			case SHIFT_LEFT:
				result.append(" << ");
				break;
			case ADD:
				result.append(" + ");
				break;
			case SUBTRACT:
				result.append(" - ");
				break;
			case MULTIPLY:
				result.append(" * ");
				break;
			case DIVIDE:
				result.append(" / ");
				break;
			case MODULO:
				result.append(" % ");
				break;
			case UNARY_PLUS:
				result.append(" +");
				break;
			case UNARY_MINUS:
				result.append(" -");
				break;
			case UNARY_NEGATE:
				result.append(" !");
				break;
			case OPENING_BRACKET:
				result.append("(");
				break;
			case CLOSING_BRACKET:
				result.append(")");
				break;
			default:
				throw new CorbabindingException("Cannot render primitive element " + primitive.type + ".");
		}
	}

	private class ElementVisitor implements ExpressionElementVisitor {
		private final StringBuilder mResult;

		public ElementVisitor(final StringBuilder result) {
			mResult = result;
		}

		@Override
		public void visit(final BooleanLiteral literal) {
			render(mResult, literal);
		}

		@Override
		public void visit(final ConstExp expression) {
			render(mResult, expression);
		}

		@Override
		public void visit(final Literal literal) {
			render(mResult, literal);
		}

		@Override
		public void visit(final PrimitiveElement element) {
			render(mResult, element);
		}

		@Override
		public void visit(final ScopedName name) {
			render(mResult, name);
		}
	}
}
