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

import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JavaTypeRendererTest {

	private ScopedRenderer getScope() {
		return new ScopedRenderer(new ScopedName(Arrays.asList("scope"), true), "::", ".");
	}

	private JavaTypeRenderer newTypeRenderer() {
		return new JavaTypeRenderer(getScope());
	}

	@Test
	public void renderStringTypeTest() {
		Type type = new StringType();
		assertEquals("String", newTypeRenderer().render(type));
	}

	@Test
	public void renderStringArrayTest() {
		Type type = new SequenceType(new StringType(), null);
		assertEquals("java.util.List<String>", newTypeRenderer().render(type));
	}

	@Test
	public void renderScopedNameTest() {
		Type type = new ScopedName(Arrays.asList("foo", "bar", "baz"), true);
		assertEquals("::scope.foo.bar.baz", newTypeRenderer().render(type));
	}

	@Test
	public void renderScopedNameArrayTest() {
		Type type = new SequenceType(new ScopedName(Arrays.asList("foo", "bar", "baz"), true), null);
		assertEquals("java.util.List<::scope.foo.bar.baz>", newTypeRenderer().render(type));
	}

	@Test
	public void renderLongIntTest() {
		Type type = new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT);
		assertEquals("int", newTypeRenderer().render(type));
	}

	@Test
	public void renderLongIntArrayTest() {
		Type type = new SequenceType(new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT), null);
		assertEquals("java.util.List<Integer>", newTypeRenderer().render(type));
	}
}