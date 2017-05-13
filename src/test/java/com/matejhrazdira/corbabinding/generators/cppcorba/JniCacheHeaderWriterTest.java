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

package com.matejhrazdira.corbabinding.generators.cppcorba;

import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JniCacheHeaderWriterTest {

	private static final String INDENTATION = "\t";
	private StringWriter mStringWriter;
	private LineWriter mWriter;

	@Before
	public void initWriter() {
		mStringWriter = new StringWriter();
		mWriter = new LineWriter(INDENTATION, mStringWriter);
	}

	@Test
	public void writeToSimpleScope() throws IOException {
		JniCacheHeaderWriter writer = new JniCacheHeaderWriter(mWriter);
		writer.enterScope(new ScopedName(Arrays.asList("SomeScope"), true));
		writer.writeln("value in scope");
		writer.exitScope();
		String actual = mStringWriter.toString();
		String expected = "struct {\n" +
				"\tvalue in scope\n" +
				"} SomeScope;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void writeToComposedScope() throws IOException {
		JniCacheHeaderWriter writer = new JniCacheHeaderWriter(mWriter);
		writer.enterScope(new ScopedName(Arrays.asList("ScopeA", "ScopeB"), true));
		writer.writeln("value in scope");
		writer.exitScope();
		String actual = mStringWriter.toString();
		String expected = "struct {\n" +
				"\tstruct {\n" +
				"\t\tvalue in scope\n" +
				"\t} ScopeB;\n" +
				"} ScopeA;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void returnFromScope() throws IOException {
		JniCacheHeaderWriter writer = new JniCacheHeaderWriter(mWriter);
		ScopedName innerScope = new ScopedName(Arrays.asList("ScopeA", "ScopeB"), true);
		writer.enterScope(innerScope);
		writer.writeln("value in inner scope");
		writer.enterScope(innerScope.getScopeName());
		writer.writeln("value in outer scope");
		writer.exitScope();
		String actual = mStringWriter.toString();
		String expected = "struct {\n" +
				"\tstruct {\n" +
				"\t\tvalue in inner scope\n" +
				"\t} ScopeB;\n" +
				"\tvalue in outer scope\n" +
				"} ScopeA;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void returnFromAndEnterScope() throws IOException {
		JniCacheHeaderWriter writer = new JniCacheHeaderWriter(mWriter);
		ScopedName innerScope = new ScopedName(Arrays.asList("ScopeA", "ScopeB"), true);
		writer.enterScope(innerScope);
		writer.writeln("value in ScopeB");
		writer.enterScope(ScopedName.nameInScope(innerScope.getScopeName(), "ScopeC"));
		writer.writeln("value in ScopeC");
		writer.exitScope();
		String actual = mStringWriter.toString();
		String expected = "struct {\n" +
				"\tstruct {\n" +
				"\t\tvalue in ScopeB\n" +
				"\t} ScopeB;\n" +
				"\tstruct {\n" +
				"\t\tvalue in ScopeC\n" +
				"\t} ScopeC;\n" +
				"} ScopeA;\n";
		assertEquals(expected, actual);
	}

	@Test
	public void returnFromAndEnterNewDifferentScope() throws IOException {
		JniCacheHeaderWriter writer = new JniCacheHeaderWriter(mWriter);
		writer.enterScope(new ScopedName(Arrays.asList("ScopeA", "ScopeB"), true));
		writer.writeln("value in ScopeB");
		writer.enterScope(new ScopedName(Arrays.asList("ScopeA2", "ScopeB2"), true));
		writer.writeln("value in ScopeB2");
		writer.exitScope();
		String actual = mStringWriter.toString();
		String expected = "struct {\n" +
				"\tstruct {\n" +
				"\t\tvalue in ScopeB\n" +
				"\t} ScopeB;\n" +
				"} ScopeA;\n" +
				"struct {\n" +
				"\tstruct {\n" +
				"\t\tvalue in ScopeB2\n" +
				"\t} ScopeB2;\n" +
				"} ScopeA2;\n";
		assertEquals(expected, actual);
	}

}