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

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScopedNameTest {

	@Test
	public void qualifiedNameTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope1", "scope2", "name"), false);
		String expected = "scope1::scope2::name";
		String actual = name.getQualifiedName();
		assertEquals(expected, actual);
	}

	@Test
	public void fullyQualifiedNameTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope1", "scope2", "name"), true);
		String expected = "::scope1::scope2::name";
		String actual = name.getQualifiedName();
		assertEquals(expected, actual);
	}

	@Test
	public void nameInScopeTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope2", "name"), false);
		ScopedName nameInScope = name.inScope(Arrays.asList("scope0", "scope1"));
		String expected = "scope0::scope1::scope2::name";
		String actual = nameInScope.getQualifiedName();
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void fullyQualifiedNameInScopeTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope2", "name"), true);
		ScopedName nameInScope = name.inScope(Arrays.asList("scope0", "scope1"));
	}

	@Test
	public void fullyQualifiedTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope1", "name"), false);
		ScopedName fullyQualified = name.fullyQualified();
		assertTrue(fullyQualified.fullyQualified);
		assertEquals(name.name, fullyQualified.name);
	}

	@Test
	public void nameInScopeTest2() {
		ScopedName relativeScope = new ScopedName(Arrays.asList("scope1", "scope2"), false);
		ScopedName nameInRelativeScope = ScopedName.nameInScope(relativeScope, "name");
		assertEquals("scope1::scope2::name", nameInRelativeScope.getQualifiedName());

		ScopedName absoluteScope = new ScopedName(Arrays.asList("scope1", "scope2"), true);
		ScopedName nameInAbsoluteScope = ScopedName.nameInScope(absoluteScope, "name");
		assertEquals("::scope1::scope2::name", nameInAbsoluteScope.getQualifiedName());
	}

	@Test
	public void getBaseNameTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope1", "scope2", "name"), false);
		assertEquals("name", name.getBaseName());
	}

	@Test
	public void getScopeTest() {
		ScopedName name = new ScopedName(Arrays.asList("scope1", "scope2", "name"), false);
		assertEquals(Arrays.asList("scope1", "scope2"), name.getScope());
	}
}