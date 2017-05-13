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

package com.matejhrazdira.corbabinding.generators.util;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class LineWriterTest {

	private static final String INDENTATION = "\t";
	private StringWriter mWriter;

	@Before
	public void initWriter() {
		mWriter = new StringWriter();
	}

	@Test
	public void getDefaultLevel() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		assertEquals(0, writer.getLevel());
	}

	@Test
	public void setLevel() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(3);
		assertEquals(3, writer.getLevel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setLevelNegative() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(-1);
	}

	@Test
	public void increaseLevel() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		int level = writer.getLevel();
		writer.increaseLevel();
		assertEquals(level + 1, writer.getLevel());
	}

	@Test
	public void decreaseLevel() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		int level = 3;
		writer.setLevel(level);
		writer.decreaseLevel();
		assertEquals(level - 1, writer.getLevel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void decreaseLevelNegative() {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.decreaseLevel();
	}

	@Test
	public void writelnDefault() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.writeln("foo", "bar", "baz");
		String expected = "foobarbaz\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writelnAtLevel() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.writeln("foo", "bar", "baz");
		String expected = INDENTATION + INDENTATION + "foobarbaz\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writelnAtLevelEmpty() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.writeln();
		String expected = "\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void endl() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.endl();
		String expected = "\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void endlAtLevel() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.endl();
		String expected = "\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writeDefault() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.write("foo", "bar", "baz");
		String expected = "foobarbaz";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writeAtLevel() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write("foo", "bar", "baz");
		String expected = INDENTATION + INDENTATION + "foobarbaz";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writeAtLevelEmpty() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write();
		String expected = INDENTATION + INDENTATION;
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writeAtLevelPartial() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write("foo", "bar", "baz");
		writer.write("FOO", "BAR", "BAZ");
		String expected = INDENTATION + INDENTATION + "foobarbaz" + "FOOBARBAZ";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writelnAfterWrite() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write("foo", "bar", "baz");
		writer.writeln("FOO", "BAR", "BAZ");
		String expected =
				INDENTATION + INDENTATION + "foobarbaz" + "\n" +
				INDENTATION + INDENTATION + "FOOBARBAZ" + "\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void writelnAndWritelnAfterWrite() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write("foo", "bar", "baz");
		writer.writeln("FOO", "BAR", "BAZ");
		writer.writeln("foo", "bar", "baz");
		String expected =
				INDENTATION + INDENTATION + "foobarbaz" + "\n" +
				INDENTATION + INDENTATION + "FOOBARBAZ" + "\n" +
				INDENTATION + INDENTATION + "foobarbaz" + "\n";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void endlEndsParitalWrite() throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, mWriter);
		writer.setLevel(2);
		writer.write("foo", "bar", "baz").endl();
		writer.write("FOO", "BAR", "BAZ");
		String expected =
				INDENTATION + INDENTATION + "foobarbaz" + "\n" +
				INDENTATION + INDENTATION + "FOOBARBAZ";
		String actual = mWriter.toString();
		assertEquals(expected, actual);
	}
}