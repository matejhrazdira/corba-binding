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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class LineWriter implements AutoCloseable {

	private final String mIndentation;
	private final Writer mWriter;

	private int mLevel;
	private String mLevelPrefix;
	private boolean mPartialWriteActive;

	public LineWriter(final String indentation, final OutputStream output) {
		this(indentation, new OutputStreamWriter(output));
	}

	public LineWriter(final String indentation, final Writer writer) {
		mIndentation = indentation;
		mLevelPrefix = indentation;
		mWriter = writer;
		setLevel(0);
	}

	public Writer getWriter() {
		return mWriter;
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(final int level) {
		if (level < 0) {
			throw new IllegalArgumentException("Level must be grater than 0, got " + level + ".");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(mIndentation);
		}
		mLevel = level;
		mLevelPrefix = sb.toString();
	}

	public LineWriter increaseLevel() {
		setLevel(mLevel + 1);
		return this;
	}

	public LineWriter decreaseLevel() {
		setLevel(mLevel - 1);
		return this;
	}

	/**
	 * Writes line with proper indentation containing all values, new line character
	 * is written automatically.
	 *
	 * If there is a partial line from previous calls to write, this line is finished.
	 */
	public LineWriter writeln(String... values) throws IOException {
		endPartialWrite();
		if (values.length > 0) {
			mWriter.write(mLevelPrefix);
			for (final String value : values) {
				mWriter.write(value);
			}
		}
		return endl();
	}

	private void endPartialWrite() throws IOException {
		if (mPartialWriteActive) {
			endl();
		}
	}

	public LineWriter endl() throws IOException {
		mPartialWriteActive = false;
		mWriter.write("\n");
		return this;
	}

	/**
	 * Starts/continues line with supplied values. No new line character is written.
	 * Line is ended by explicitly calling endl() or automatically when writeln() is
	 * called next time.
	 */
	public LineWriter write(String... values) throws IOException {
		startPartialWrite();
		for (final String value : values) {
			mWriter.write(value);
		}
		return this;
	}

	private void startPartialWrite() throws IOException {
		if (!mPartialWriteActive) {
			mWriter.write(mLevelPrefix);
			mPartialWriteActive = true;
		}
	}

	@Override
	public void close() {
		try {
			mWriter.close();
		} catch (IOException e) {
			// ignore
		}
	}
}
