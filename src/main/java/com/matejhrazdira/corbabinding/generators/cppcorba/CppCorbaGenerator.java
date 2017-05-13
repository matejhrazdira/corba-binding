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

import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaUnionProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.generators.util.TemplateProcessor;
import com.matejhrazdira.corbabinding.generators.util.TemplateSpec;
import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.definitions.StructType;
import com.matejhrazdira.corbabinding.idl.definitions.UnionType;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class CppCorbaGenerator {

	private static final String INDENTATION = "\t";
	private static final String IDL_SUFFIX = ".idl";
	private static final String JNI_CACHE_H = "JniCache.h";
	private static final String JNI_CACHE_CPP = "JniCache.cpp";
	private static final String CORBABINDING_H = "corbabinding.h";
	private static final String CORBABINDING_CPP = "corbabinding.cpp";

	private final CorbaEnumRenderer mEnumRenderer;
	private final CorbaStructRenderer mStructRenderer;
	private final CorbaUnionRenderer mUnionRenderer;
	private final CorbaOutput mOutput;
	private final OutputListener mOutputListener;
	private final String mTaoIdlIncludePrefix;

	public CppCorbaGenerator(final File output, final JavaProjectionProvider enumProjectionProvider, final JavaStructProjectionProvider structProjectionProvider, final JavaUnionProjectionProvider unionProjectionProvider, final OutputListener outputListener, final String taoIdlIncludePrefix) throws IOException {
		if (taoIdlIncludePrefix != null && !taoIdlIncludePrefix.isEmpty()) {
			mTaoIdlIncludePrefix = taoIdlIncludePrefix + "/";
		} else {
			mTaoIdlIncludePrefix = "";
		}
		output.mkdirs();
		List<OutputStream> outputStreams = openStreams(
				new File(output, JNI_CACHE_H),
				new File(output, JNI_CACHE_CPP),
				new File(output, CORBABINDING_H),
				new File(output, CORBABINDING_CPP)
		);
		mOutput = new CorbaOutput.Builder()
				.withJniCacheHeader(new JniCacheHeaderWriter(new LineWriter(INDENTATION, outputStreams.get(0))))
				.withJniCacheImpl(new LineWriter(INDENTATION, outputStreams.get(1)))
				.withConversionHeader(new LineWriter(INDENTATION, outputStreams.get(2)))
				.withConversionImpl(new LineWriter(INDENTATION, outputStreams.get(3)))
				.build();
		mOutputListener = outputListener;
		mEnumRenderer = new CorbaEnumRenderer(enumProjectionProvider, mOutput, outputListener);
		mStructRenderer = new CorbaStructRenderer(structProjectionProvider, mOutput, outputListener);
		mUnionRenderer = new CorbaUnionRenderer(unionProjectionProvider, mOutput, outputListener);
	}

	private List<OutputStream> openStreams(File ... outputs) throws IOException {
		List<OutputStream> streams = new ArrayList<>();
		try {
			for (final File output : outputs) {
				streams.add(new FileOutputStream(output));
			}
		} catch(IOException e) {
			for (final OutputStream stream : streams) {
				try {
					stream.close();
				} catch (IOException e2) {
					// ignore
				}
			}
			throw e;
		}
		return streams;
	}

	public void generate(Model model) throws IOException {
		try {
			startJniCacheHeader(model);
		} catch (IOException e) {
			mOutput.close();
			throw e;
		}
	}

	private void startJniCacheHeader(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JNI_CACHE_H))
				.withOutput(mOutput.jniCacheHeader.getWriter().getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI CACHE HEADER $$$")))
				.withReplacements(createReplacementMap())
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						mOutput.jniCacheHeader.getWriter().increaseLevel();
						startJniCacheImpl(model);
						mOutput.jniCacheHeader.exitScope();
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void startJniCacheImpl(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JNI_CACHE_CPP))
				.withOutput(mOutput.jniCacheImpl.getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI CACHE IMPL $$$")))
				.withReplacements(createReplacementMap())
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						mOutput.jniCacheImpl.increaseLevel();
						startConversionHeader(model);
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void startConversionHeader(final Model model) throws IOException {
		final String conversionIncludesEvent = "$$$ CONVERSION INCLUDES $$$";
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(CORBABINDING_H))
				.withOutput(mOutput.conversionHeader.getWriter())
				.withEvents(new HashSet<>(Arrays.asList(
						conversionIncludesEvent,
						"$$$ CONVERSION HEADER $$$"
						)
				))
				.withReplacements(createReplacementMap())
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						if (event.equals(conversionIncludesEvent)) {
							generateConversionIncludes(model);
						} else {
							startConversionImpl(model);
						}
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void generateConversionIncludes(final Model model) throws IOException {
		for (String file : model.mFiles) {
			file = new File(file).getName();
			if (file.endsWith(IDL_SUFFIX)) {
				file = file.substring(0, file.length() - IDL_SUFFIX.length());
			}
			mOutput.conversionHeader.writeln("#include \"", mTaoIdlIncludePrefix, file, "C.h\"");
		}
	}

	private void startConversionImpl(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(CORBABINDING_CPP))
				.withOutput(mOutput.conversionImpl.getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ CONVERSION IMPL $$$")))
				.withReplacements(createReplacementMap())
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						generateSymbols(model);
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void generateSymbols(final Model model) throws IOException {
		for (Symbol s : model.getSymbols()) {
			AbsCorbaRenderer renderer = getRendererForSymbol(s);
			if (renderer != null) {
				logSymbol(s);
				renderer.render(s);
				mOutput.conversionHeader.writeln();
				mOutput.conversionImpl.writeln();
			}
		}
	}

	private String getTemplateForFile(String file) {
		return "/" + file + ".template";
	}

	private Map<String, String> createReplacementMap() {
		final Map<String, String> res = new HashMap<>();
		res.put("$$$ENV$$$", JniConfig.ARG_JNI_ENV);
		res.put("$$$JNI$$$", JniConfig.ARG_JNI_CACHE_VAR);
		res.put("$$$CLS$$$", AbsCorbaRenderer.CACHE_CLASS);
		res.put("$$$CONVERT$$$", AbsCorbaRenderer.CONVERSION_FUNCTION);
		res.put("$$$IN_ARG$$$", AbsCorbaRenderer.CONVERSION_IN_ARG);
		res.put("$$$OUT_ARG$$$", AbsCorbaRenderer.CONVERSION_OUT_ARG);
		res.put("$$$CTOR$$$", AbsCorbaStructRenderer.CACHE_CTOR);
		res.put("$$$RESULT$$$", AbsCorbaStructRenderer.CONVERSION_RESULT_VAR);
		res.put("$$$CONVERT_ELEMENT$$$", AbsCorbaRenderer.CONVERSION_FUNCTION + "Element");
		res.put("$$$ELEMENT$$$", "_element_");
		res.put("$$$JSTRING_CHARS_VAR$$$", "_j_string_chars_");
		res.put("$$$JSTRING_VAR$$$", "_j_string_");
		return res;
	}

	private void logSymbol(final Symbol s) {
		mOutputListener.onInfo("Processing cppcorba for '" + s.name.getQualifiedName() + "'.");
	}

	private AbsCorbaRenderer getRendererForSymbol(Symbol s) {
		IdlElement element = s.element;
		if (element instanceof StructType) {
			return mStructRenderer;
		} else if (element instanceof UnionType) {
			return mUnionRenderer;
		} else if (element instanceof EnumType) {
			return mEnumRenderer;
		} else {
			return null;
		}
	}
}
