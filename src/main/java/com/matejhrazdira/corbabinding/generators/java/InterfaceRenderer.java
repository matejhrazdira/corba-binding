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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjectionProvider;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.io.Writer;

public class InterfaceRenderer implements JavaRenderer, JavaInterfaceProjectionProvider {

	private InterfaceDeclRenderer mInterfaceDeclRenderer;
	private InterfaceImplRenderer mInterfaceImplRenderer;

	public InterfaceRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener, final ExceptionRenderer exceptionRenderer, final String varType, final String corbaExceptionType, String disposableType) {
		mInterfaceDeclRenderer = new InterfaceDeclRenderer(scopedRenderer, resolver, outputListener, exceptionRenderer, varType, corbaExceptionType, disposableType);
		mInterfaceImplRenderer = new InterfaceImplRenderer(scopedRenderer, resolver, outputListener, exceptionRenderer, varType, corbaExceptionType, disposableType);
	}

	@Override
	public void render(final Writer output, final Symbol symbol) throws IOException {
		mInterfaceDeclRenderer.render(output, symbol);
	}

	public void renderClientImpl(final Writer output, final Symbol symbol) throws IOException {
		mInterfaceImplRenderer.render(output, symbol);
	}

	@Override
	public JavaInterfaceProjection getProjection(final Symbol symbol) {
		return mInterfaceImplRenderer.getProjection(symbol);
	}

	@Override
	public ScopedName getProjectionPrefix() {
		return mInterfaceDeclRenderer.getProjectionPrefix();
	}
}
