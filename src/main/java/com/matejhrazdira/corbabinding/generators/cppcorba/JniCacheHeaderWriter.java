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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JniCacheHeaderWriter {

	private final LineWriter mWriter;
	private final List<String> mCurrentScope = new ArrayList<>();

	public JniCacheHeaderWriter(final LineWriter writer) {
		mWriter = writer;
	}

	public LineWriter getWriter() {
		return mWriter;
	}

	public void writeln(String... values) throws IOException {
		mWriter.writeln(values);
	}

	public void enterScope(ScopedName scope) throws IOException {
		List<String> newScope = scope.name;
		int i;
		for (i = 0; i < mCurrentScope.size(); i++) {
			if (i < newScope.size()) {
				String currentElement = mCurrentScope.get(i);
				String newElement = newScope.get(i);
				if (currentElement.equals(newElement)) {
					continue; // skip common prefix
				}
			}
			break;
		}
		if (i < mCurrentScope.size()) { // consumed current scope
			decreaseScope(mCurrentScope.size() - i);
		}
		if (i < newScope.size()) {
			increaseScope(newScope.subList(i, newScope.size()));
		}
	}

	public void exitScope() throws IOException {
		decreaseScope(mCurrentScope.size());
	}

	private void increaseScope(final List<String> additionalScope) throws IOException {
		for (final String scope : additionalScope) {
			mWriter.writeln("struct {");
			mWriter.increaseLevel();
			mCurrentScope.add(scope);
		}
	}

	private void decreaseScope(final int level) throws IOException {
		for (int i = 0; i < level; i++) {
			int lastIndex = mCurrentScope.size() - 1;
			String scope = mCurrentScope.get(lastIndex);
			mWriter.decreaseLevel();
			mWriter.writeln("} ", scope, ";");
			mCurrentScope.remove(lastIndex);
		}
	}
}
