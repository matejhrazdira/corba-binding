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
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;

public class ConstantRenderer extends JavaWithMembersRenderer {

	public static final String VALUE_FIELD = "value";

	public ConstantRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener) {
		super(scopedRenderer, resolver, outputListener);
	}

	@Override
	protected String getElementType() {
		return "interface";
	}

	@Override
	protected void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException {
		ConstDcl data = (ConstDcl) symbol.element;
		String type = mTypeRenderer.render(getJavaType(data.type));
		String value = mExpressionRenderer.render(data.value);
		writer.writeln("public static final ", type, " ", VALUE_FIELD, " = ", value, ";");
		writer.writeln();
	}
}
