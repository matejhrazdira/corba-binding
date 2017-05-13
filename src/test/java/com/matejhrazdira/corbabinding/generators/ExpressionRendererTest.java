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

import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.Literal;
import com.matejhrazdira.corbabinding.idl.expressions.PrimitiveElement;
import com.matejhrazdira.corbabinding.idl.expressions.PrimitiveElement.Type;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ExpressionRendererTest {

	@Test
	public void renderLinearTest() {
		ConstExp expression = new ConstExp(
				Arrays.asList(
						new Literal("5"),
						new PrimitiveElement(Type.ADD),
						new Literal("6"),
						new PrimitiveElement(Type.SUBTRACT),
						new PrimitiveElement(Type.OPENING_BRACKET),
						new ScopedName(Arrays.asList("foo", "bar"), true),
						new PrimitiveElement(Type.MULTIPLY),
						new Literal("2"),
						new PrimitiveElement(Type.CLOSING_BRACKET)
				)
		);
		ScopedRenderer scopedRenderer = new ScopedRenderer(new ScopedName(Arrays.asList("scope"), true), "::", ".");
		ExpressionRenderer rendered = new ExpressionRenderer(scopedRenderer);
		String actual = rendered.render(expression);
		String expected = "5 + 6 - (::scope.foo.bar * 2)";
		assertEquals(expected, actual);
	}

	@Test
	public void renderRecursiveTest() {
		ConstExp expression = new ConstExp(
				Arrays.asList(
						new Literal("5"),
						new PrimitiveElement(Type.ADD),
						new Literal("6"),
						new PrimitiveElement(Type.SUBTRACT),
						new PrimitiveElement(Type.OPENING_BRACKET),
						new ScopedName(Arrays.asList("foo", "bar"), true),
						new PrimitiveElement(Type.MULTIPLY),
						new PrimitiveElement(Type.OPENING_BRACKET),
						new ConstExp(
								Arrays.asList(
										new Literal("7"),
										new PrimitiveElement(Type.ADD),
										new Literal("8"),
										new PrimitiveElement(Type.SUBTRACT),
										new PrimitiveElement(Type.OPENING_BRACKET),
										new ScopedName(Arrays.asList("foo", "bar", "baz"), true),
										new PrimitiveElement(Type.DIVIDE),
										new Literal("6"),
										new PrimitiveElement(Type.CLOSING_BRACKET)
								)
						),
						new PrimitiveElement(Type.CLOSING_BRACKET),
						new PrimitiveElement(Type.CLOSING_BRACKET)
				)
		);
		ScopedRenderer scopedRenderer = new ScopedRenderer(new ScopedName(Arrays.asList("scope"), true), "::", ".");
		ExpressionRenderer rendered = new ExpressionRenderer(scopedRenderer);
		String actual = rendered.render(expression);
		String expected = "5 + 6 - (::scope.foo.bar * (7 + 8 - (::scope.foo.bar.baz / 6)))";
		assertEquals(expected, actual);
	}
}