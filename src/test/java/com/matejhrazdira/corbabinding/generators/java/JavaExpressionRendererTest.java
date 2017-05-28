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

package com.matejhrazdira.corbabinding.generators.java;

import com.matejhrazdira.corbabinding.gen.IdlParser;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.Specification;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.Literal;
import com.matejhrazdira.corbabinding.idl.expressions.PrimitiveElement;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.model.Model;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JavaExpressionRendererTest {

	private static final String SIMPLE_IDL =
			"module foo {\n" +
			"    const long bar = 5;\n" +
			"    interface SomeInterface {\n" +
			"        const long baz = 6;\n" +
			"    };" +
			"};";

	@Test
	public void renderLinearTest() {
		ConstExp expression = new ConstExp(
				Arrays.asList(
						new Literal("5"),
						new PrimitiveElement(PrimitiveElement.Type.ADD),
						new Literal("6"),
						new PrimitiveElement(PrimitiveElement.Type.SUBTRACT),
						new PrimitiveElement(PrimitiveElement.Type.OPENING_BRACKET),
						new ScopedName(Arrays.asList("foo", "bar"), true),
						new PrimitiveElement(PrimitiveElement.Type.MULTIPLY),
						new Literal("2"),
						new PrimitiveElement(PrimitiveElement.Type.CLOSING_BRACKET)
				)
		);
		ScopedRenderer scopedRenderer = new JavaScopedRenderer(new ScopedName(Arrays.asList("scope"), true));
		ExpressionRenderer rendered = new JavaExpressionRenderer(
				scopedRenderer,
				createSymbolResolver()
		);
		String actual = rendered.render(expression);
		String expected = "5 + 6 - (scope.foo.bar.value * 2)";
		assertEquals(expected, actual);
	}

	@Test
	public void renderWithInnerConstantTest() {
		ConstExp expression = new ConstExp(
				Arrays.asList(
						new Literal("5"),
						new PrimitiveElement(PrimitiveElement.Type.ADD),
						new ScopedName(Arrays.asList("foo", "SomeInterface", "baz"), true)
				)
		);
		ScopedRenderer scopedRenderer = new JavaScopedRenderer(new ScopedName(Arrays.asList("scope"), true));
		ExpressionRenderer rendered = new JavaExpressionRenderer(
				scopedRenderer,
				createSymbolResolver()
		);
		String actual = rendered.render(expression);
		String expected = "5 + scope.foo.SomeInterface.baz";
		assertEquals(expected, actual);
	}

	@Test
	public void renderNonConstName() {
		ConstExp expression = new ConstExp(
				Arrays.asList(
						new ScopedName(Arrays.asList("foo", "bar", "BAZ"), true)
				)
		);
		ScopedRenderer scopedRenderer = new JavaScopedRenderer(new ScopedName(Arrays.asList("scope"), true));
		ExpressionRenderer rendered = new JavaExpressionRenderer(
				scopedRenderer,
				createSymbolResolver()
		);
		String actual = rendered.render(expression);
		String expected = "scope.foo.bar.BAZ";
		assertEquals(expected, actual);
	}

	private SymbolResolver createSymbolResolver() {
		try {
			IdlParser p = new IdlParser(new ByteArrayInputStream(SIMPLE_IDL.getBytes()));
			Specification specification = p.specification("simple.idl");
			return new Model(specification).getResolver();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}