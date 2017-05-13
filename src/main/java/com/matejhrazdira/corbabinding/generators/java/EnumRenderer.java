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
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;

public class EnumRenderer extends AbsJavaRenderer {

	public EnumRenderer(final ScopedRenderer scopedRenderer, final OutputListener outputListener) {
		super(scopedRenderer, outputListener);
	}

	@Override
	protected String getElementType() {
		return "enum";
	}

	@Override
	protected void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException {
		EnumType data = (EnumType) symbol.element;
		for (EnumType.EnumValue value : data.values) {
			writer.writeln(value.name, ",");
		}
		writer.writeln();
	}
}
