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

import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.ConstDcl;
import com.matejhrazdira.corbabinding.idl.definitions.Module;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

public class JavaExpressionRenderer extends ExpressionRenderer {

	private final SymbolResolver mResolver;

	public JavaExpressionRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver) {
		super(scopedRenderer);
		mResolver = resolver;
	}

	@Override
	protected void render(final StringBuilder result, final ScopedName element) {
		super.render(result, element);
		Symbol symbol = mResolver.findSymbol(element);
		if (symbol != null && symbol.element instanceof ConstDcl && !symbol.innerSymbol) {
			result.append(mScopedRenderer.getScopeDelimiter());
			result.append(ConstantRenderer.VALUE_FIELD);
		}
	}
}
