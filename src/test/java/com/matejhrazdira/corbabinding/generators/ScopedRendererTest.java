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

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScopedRendererTest {

	public static final String GLOBAL_SCOPE = "<<GLOBAL>>";
	public static final String DELIMITER = "<<DELIMITER>>";

	@Test
	public void fullyQualifiedInFullyQualifiedTest() {
		ScopedRenderer renderer = new ScopedRenderer(
				new ScopedName(Arrays.asList("scope1", "scope2"), true),
				GLOBAL_SCOPE,
				DELIMITER
		);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), true));
		String expected = "<<GLOBAL>>scope1<<DELIMITER>>scope2<<DELIMITER>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void fullyQualifiedInRelativeTest() {
		ScopedRenderer renderer = new ScopedRenderer(
				new ScopedName(Arrays.asList("scope1", "scope2"), false),
				GLOBAL_SCOPE,
				DELIMITER
		);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), true));
		String expected = "scope1<<DELIMITER>>scope2<<DELIMITER>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void relativeInFullyQualifiedTest() {
		ScopedRenderer renderer = new ScopedRenderer(
				new ScopedName(Arrays.asList("scope1", "scope2"), true),
				GLOBAL_SCOPE,
				DELIMITER
		);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), false));
		String expected = "<<GLOBAL>>scope1<<DELIMITER>>scope2<<DELIMITER>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void relativeInRelativeTest() {
		ScopedRenderer renderer = new ScopedRenderer(
				new ScopedName(Arrays.asList("scope1", "scope2"), false),
				GLOBAL_SCOPE,
				DELIMITER
		);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), false));
		String expected = "scope1<<DELIMITER>>scope2<<DELIMITER>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}
	
	@Test
	public void relativeInAbsoluteTop() {
		ScopedRenderer renderer = new ScopedRenderer(GLOBAL_SCOPE, DELIMITER, true);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), false));
		String expected = "<<GLOBAL>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void relativeInRelativeTop() {
		ScopedRenderer renderer = new ScopedRenderer(GLOBAL_SCOPE, DELIMITER, false);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), false));
		String expected = "fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void absoluteInAbsoluteTop() {
		ScopedRenderer renderer = new ScopedRenderer(GLOBAL_SCOPE, DELIMITER, true);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), true));
		String expected = "<<GLOBAL>>fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void absoluteInRelativeTop() {
		ScopedRenderer renderer = new ScopedRenderer(GLOBAL_SCOPE, DELIMITER, false);
		String actual = renderer.render(new ScopedName(Arrays.asList("fully", "qualified", "name"), true));
		String expected = "fully<<DELIMITER>>qualified<<DELIMITER>>name";
		assertEquals(expected, actual);
	}

	@Test
	public void parseFullyQualified() {
		ScopedRenderer renderer = new ScopedRenderer("::", ".", true);
		ScopedName parsed = renderer.parse("::foo.bar.baz");
		assertTrue(parsed.fullyQualified);
		assertEquals("::foo::bar::baz", parsed.getQualifiedName());
	}

	@Test
	public void parseRelative() {
		ScopedRenderer renderer = new ScopedRenderer("::", ".", true);
		ScopedName parsed = renderer.parse("foo.bar.baz");
		assertFalse(parsed.fullyQualified);
		assertEquals("foo::bar::baz", parsed.getQualifiedName());
	}

	@Test
	public void parseWithoutGlobalScope() {
		ScopedRenderer renderer = new ScopedRenderer("", ".", true);
		ScopedName parsed = renderer.parse("foo.bar.baz");
		assertTrue(parsed.fullyQualified);
		assertEquals("::foo::bar::baz", parsed.getQualifiedName());
	}
}