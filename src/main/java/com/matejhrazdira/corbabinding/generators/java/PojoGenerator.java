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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.*;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Interface;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.util.NoOpOutputListener;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PojoGenerator {

	private final File mOutputDir;
	private final OutputListener mOutputListener;
	private final Model mModel;
	private final SymbolResolver mResolver;
	private final JavaScopedRenderer mScopedRenderer;
	private final ConstantRenderer mConstantRenderer;
	private final EnumRenderer mEnumRenderer;
	private final StructRenderer mStructRenderer;
	private final UnionRenderer mUnionRenderer;

	public PojoGenerator(File outputDir, String packagePrefix, Model model) {
		this(outputDir, packagePrefix, model, new NoOpOutputListener());
	}

	public PojoGenerator(File outputDir, String packagePrefix, Model model, OutputListener outputListener) {
		mOutputListener = outputListener;
		mOutputDir = getPackageDir(outputDir, packagePrefix);
		mModel = model;
		mResolver = model.getResolver();
		JavaScopedRenderer tmpRenderer = new JavaScopedRenderer(true);
		ScopedName prefix = tmpRenderer.parse(packagePrefix);
		mScopedRenderer = new JavaScopedRenderer(prefix);
		mStructRenderer = new StructRenderer(mScopedRenderer, mResolver, outputListener);
		mConstantRenderer = new ConstantRenderer(mScopedRenderer, mResolver, outputListener);
		mEnumRenderer = new EnumRenderer(mScopedRenderer, outputListener);
		mUnionRenderer = new UnionRenderer(mScopedRenderer, mResolver, outputListener);
	}

	private File getPackageDir(File root, String packagePrefix) {
		String[] packages = packagePrefix.split("\\.");
		File result = root;
		for (final String pkg : packages) {
			result = new File(result, pkg);
		}
		return result;
	}

	public void generatePojos() throws IOException {
		for (Symbol s : mModel.getSymbols()) {
			logSymbol(s);
			AbsJavaRenderer renderer = getRendererForSymbol(s);
			if (renderer != null) {
				File outputFile = prepareFileForName(s.name);
				try (FileWriter writer = new FileWriter(outputFile)) {
					renderer.render(writer, s);
				}
			}
		}
	}

	private void logSymbol(final Symbol s) {
		IdlElement element = s.element;
		File file = getFileForName(s.name);
		if (element instanceof StructType) {
			mOutputListener.onInfo("Creating class for struct '" + s.name.getQualifiedName() + "' in '" + file.getAbsolutePath() + "'.");
		} else if (element instanceof UnionType) {
			mOutputListener.onInfo("Creating class for union '" + s.name.getQualifiedName() + "' in '" + file.getAbsolutePath() + "'.");
		} else if (element instanceof ConstDcl) {
			mOutputListener.onInfo("Creating constant definition '" + s.name.getQualifiedName() + "' in '" + file.getAbsolutePath() + "'.");
		} else if (element instanceof EnumType) {
			mOutputListener.onInfo("Creating enum '" + s.name.getQualifiedName() + "' in '" + file.getAbsolutePath() + "'.");
		} else if (element instanceof EnumType.EnumValue) {
			// ignore, it is part of the enum
		} else if (element instanceof TypeDeclarator) {
			mOutputListener.onInfo("Ignoring typedef '" + s.name.getQualifiedName() + "'.");
		} else if (element instanceof Module){
			mOutputListener.onInfo("Entering module '" + s.name.getQualifiedName() + "'.");
		} else if (element instanceof ExceptDcl) {
			mOutputListener.onInfo("Exceptions not yet supported, skipping '" + s.name.getQualifiedName() + "'.");
		} else if (element instanceof Interface) {
			mOutputListener.onInfo("Interfaces not yet supported, skipping '" + s.name.getQualifiedName() + "'.");
		} else {
			mOutputListener.onWarning("Encountered unexpected element '" + s.name.getQualifiedName() + "' of class " + element.getClass().getName() + ", this is most probably a bug - please report!");
		}
	}

	private File prepareFileForName(ScopedName name) {
		File result = getFileForName(name);
		result.getParentFile().mkdirs();
		return result;
	}

	private File getFileForName(final ScopedName name) {
		File result = mOutputDir;
		for (String pkg : name.getScope()) {
			result = new File(result, pkg);
		}
		result = new File(result, name.getBaseName() + ".java");
		return result;
	}

	private AbsJavaRenderer getRendererForSymbol(Symbol s) {
		IdlElement element = s.element;
		if (element instanceof StructType) {
			return mStructRenderer;
		} else if (element instanceof UnionType) {
			return mUnionRenderer;
		} else if (element instanceof ConstDcl) {
			return mConstantRenderer;
		} else if (element instanceof EnumType) {
			return mEnumRenderer;
		} else {
			return null;
		}
	}

	public JavaProjectionProvider getEnumProjection() {
		return mEnumRenderer;
	}

	public JavaStructProjectionProvider getStructProjection() {
		return mStructRenderer;
	}

	public JavaUnionProjectionProvider getUnionProjection() {
		return mUnionRenderer;
	}
}
