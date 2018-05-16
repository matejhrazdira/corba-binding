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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.NoOpOutputListener;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.io.Writer;

public abstract class AbsJavaRenderer implements JavaProjectionProvider {

	protected final String INDENTATION = "\t";

	protected final ScopedRenderer mScopedRenderer;
	protected final JavaTypeRenderer mTypeRenderer;

	private final OutputListener mOutputListener;

	public AbsJavaRenderer(final ScopedRenderer scopedRenderer) {
		this(scopedRenderer, new NoOpOutputListener());
	}

	public AbsJavaRenderer(final ScopedRenderer scopedRenderer, OutputListener outputListener) {
		mScopedRenderer = scopedRenderer;
		mTypeRenderer = new JavaTypeRenderer(scopedRenderer);
		mOutputListener = outputListener != null ? outputListener : new NoOpOutputListener();
	}

	public void render(Writer output, Symbol symbol) throws IOException {
		LineWriter writer = new LineWriter(INDENTATION, output);
		ScopedName name = getJavaSymbolName(symbol);
		writePackageDeclaration(writer, name);
		writeElementStart(writer, name, symbol, "public ");
		writer.increaseLevel();
		writeBody(writer, symbol, name);
		writer.decreaseLevel();
		writeElementEnd(writer);
	}

	public void renderStatic(LineWriter writer, Symbol symbol) throws IOException {
		ScopedName name = getJavaSymbolName(symbol);
		writeElementStart(writer, name, symbol, "public static ");
		writer.increaseLevel();
		writeBody(writer, symbol, name);
		writer.decreaseLevel();
		writeElementEnd(writer);
	}

	protected ScopedName getJavaSymbolName(final Symbol symbol) {
		return mScopedRenderer.getNameInScope(symbol.name);
	}

	@Override
	public JavaProjection getProjection(final Symbol symbol) {
		return new JavaProjection(symbol, getJavaSymbolName(symbol));
	}

	@Override
	public ScopedName getProjectionPrefix() {
		return mScopedRenderer.getScope();
	}

	private void writePackageDeclaration(final LineWriter writer, final ScopedName name) throws IOException {
		ScopedName scopeName = name.getScopeName();
		if (scopeName != null) {
			String packageName = mScopedRenderer.renderRaw(scopeName);
			writer.writeln("package ", packageName, ";");
			writer.writeln();
		}
	}

	private void writeElementStart(final LineWriter writer, final ScopedName name, final Symbol symbol, final String accessSpecifier) throws IOException {
		String inheritanceSpec = getInheritanceSpec(symbol);
		inheritanceSpec = inheritanceSpec.isEmpty() ? inheritanceSpec : inheritanceSpec + " ";
		writer.writeln(accessSpecifier, getElementType(), " ", name.getBaseName(), " ", inheritanceSpec, "{");
		writer.writeln();
	}

	protected abstract String getElementType();

	protected String getInheritanceSpec(Symbol symbol) {
		return getInheritanceSpec();
	}

	protected String getInheritanceSpec() {
		return "";
	}

	protected abstract void writeBody(final LineWriter writer, final Symbol symbol, final ScopedName name) throws IOException;

	private void writeElementEnd(final LineWriter writer) throws IOException {
		writer.writeln("}");
	}

	protected void logi(String info) {
		mOutputListener.onInfo(info);
	}

	protected void logw(String warning) {
		mOutputListener.onWarning(warning);
	}

	protected void loge(String error) {
		mOutputListener.onError(error);
	}
}
