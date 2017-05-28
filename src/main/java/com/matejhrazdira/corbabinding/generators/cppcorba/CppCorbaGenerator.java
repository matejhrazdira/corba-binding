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

import com.matejhrazdira.corbabinding.generators.java.projection.*;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.generators.util.TemplateProcessor;
import com.matejhrazdira.corbabinding.generators.util.TemplateSpec;
import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.definitions.StructType;
import com.matejhrazdira.corbabinding.idl.definitions.UnionType;
import com.matejhrazdira.corbabinding.idl.interfaces.Declaration;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.*;
import java.util.*;

public class CppCorbaGenerator {

	private final CorbaEnumRenderer mEnumRenderer;
	private final CorbaStructRenderer mStructRenderer;
	private final CorbaUnionRenderer mUnionRenderer;
	private final CorbaInterfaceRenderer mInterfaceRenderer;
	private final CorbaOutput mOutput;
	private final OutputListener mOutputListener;
	private final String mTaoIdlIncludePrefix;
	private final String mCorbaEncoding;
	private final Map<String, String> mReplacementMap;

	CppCorbaGenerator(
			final File output,
			final SymbolResolver symbolResolver, final JavaProjectionProvider enumProjectionProvider,
			final JavaStructProjectionProvider structProjectionProvider,
			final JavaUnionProjectionProvider unionProjectionProvider,
			final JavaTemplateProjection javaTemplateProjection,
			final JavaInterfaceProjectionProvider interfaceProjectionProvider,
			final OutputListener outputListener,
			final String taoIdlIncludePrefix,
			final String corbaEncoding
	) throws IOException {
		if (corbaEncoding == null || corbaEncoding.isEmpty()) {
			mCorbaEncoding = CppCorbaGeneratorBuilder.DEFAULT_ENCODING;
		} else {
			mCorbaEncoding = corbaEncoding;
		}
		if (taoIdlIncludePrefix == null || taoIdlIncludePrefix.isEmpty()) {
			mTaoIdlIncludePrefix = "";
		} else {
			mTaoIdlIncludePrefix = taoIdlIncludePrefix + "/";
		}
		mReplacementMap = JniConfig.getStringStringMap(mCorbaEncoding, javaTemplateProjection);
		output.mkdirs();
		List<OutputStream> outputStreams = openStreams(
				new File(output, JniConfig.JNI_CACHE_H),
				new File(output, JniConfig.JNI_CACHE_CPP),
				new File(output, JniConfig.CORBABINDING_H),
				new File(output, JniConfig.CORBABINDING_CPP),
				new File(output, JniConfig.JNI_IMPL_H),
				new File(output, JniConfig.JNI_IMPL_CPP),
				new File(output, JniConfig.JNI_IMPL_PRIVATE_H),
				new File(output, JniConfig.JNI_IMPL_PRIVATE_CPP),
				new File(output, JniConfig.CB_LIB_H),
				new File(output, JniConfig.CB_LIB_CPP)
		);
		mOutput = new CorbaOutput.Builder()
				.withJniCacheHeader(new JniCacheHeaderWriter(new LineWriter(JniConfig.INDENTATION, outputStreams.get(0))))
				.withJniCacheImpl(new LineWriter(JniConfig.INDENTATION, outputStreams.get(1)))
				.withConversionHeader(new LineWriter(JniConfig.INDENTATION, outputStreams.get(2)))
				.withConversionImpl(new LineWriter(JniConfig.INDENTATION, outputStreams.get(3)))
				.withJniImplHeader(new LineWriter(JniConfig.INDENTATION, outputStreams.get(4)))
				.withJniImplImpl(new LineWriter(JniConfig.INDENTATION, outputStreams.get(5)))
				.withJniImplPrivateHeader(new LineWriter(JniConfig.INDENTATION, outputStreams.get(6)))
				.withJniImplPrivateImpl(new LineWriter(JniConfig.INDENTATION, outputStreams.get(7)))
				.withLibHeader(new LineWriter(JniConfig.INDENTATION, outputStreams.get(8)))
				.withLibImpl(new LineWriter(JniConfig.INDENTATION, outputStreams.get(9)))
				.createCorbaOutput();
		mOutputListener = outputListener;
		mEnumRenderer = new CorbaEnumRenderer(enumProjectionProvider, mOutput, outputListener);
		mStructRenderer = new CorbaStructRenderer(structProjectionProvider, mOutput, outputListener);
		mUnionRenderer = new CorbaUnionRenderer(unionProjectionProvider, mOutput, outputListener);
		mInterfaceRenderer = new CorbaInterfaceRenderer(symbolResolver, interfaceProjectionProvider, mOutput, outputListener);
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
			generateImpl(model);
		} finally {
			mOutput.close();
		}
	}

	protected void generateImpl(final Model model) throws IOException {
		new TemplateProcessor().process(new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.CB_LIB_H))
				.withOutput(mOutput.libHeader.getWriter())
				.createTemplateSpec());
		new TemplateProcessor().process(new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.CB_LIB_CPP))
				.withOutput(mOutput.libImpl.getWriter())
				.createTemplateSpec());
		new TemplateProcessor().process(new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_IMPL_PRIVATE_H))
				.withOutput(mOutput.jniImplPrivateHeader.getWriter())
				.withReplacements(mReplacementMap)
				.createTemplateSpec());
		new TemplateProcessor().process(new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_IMPL_PRIVATE_CPP))
				.withOutput(mOutput.jniImplPrivateImpl.getWriter())
				.withReplacements(mReplacementMap)
				.withEvents(new HashSet<>(Arrays.asList("$$$ TYPE CACHE ENTRIES $$$")))
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						mOutput.jniImplPrivateImpl.increaseLevel();
						startJniCacheHeader(model);
						mOutput.jniImplPrivateImpl.decreaseLevel();
					}
				})
				.createTemplateSpec());
	}

	private void startJniCacheHeader(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_CACHE_H))
				.withOutput(mOutput.jniCacheHeader.getWriter().getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI CACHE HEADER $$$")))
				.withReplacements(mReplacementMap)
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
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_CACHE_CPP))
				.withOutput(mOutput.jniCacheImpl.getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI CACHE IMPL $$$")))
				.withReplacements(mReplacementMap)
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						mOutput.jniCacheImpl.increaseLevel();
						startJniImplHeader(model);
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void startJniImplHeader(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_IMPL_H))
				.withOutput(mOutput.jniImpHeader.getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI IMPL HEADER $$$")))
				.withReplacements(mReplacementMap)
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						startJniImplImpl(model);
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void startJniImplImpl(final Model model) throws IOException {
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.JNI_IMPL_CPP))
				.withOutput(mOutput.jniImplImpl.getWriter())
				.withEvents(new HashSet<>(Arrays.asList("$$$ JNI IMPL IMPL $$$")))
				.withReplacements(mReplacementMap)
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						startConversionHeader(model);
					}
				})
				.createTemplateSpec();
		new TemplateProcessor().process(spec);
	}

	private void startConversionHeader(final Model model) throws IOException {
		final String conversionIncludesEvent = "$$$ CONVERSION INCLUDES $$$";
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.CORBABINDING_H))
				.withOutput(mOutput.conversionHeader.getWriter())
				.withEvents(new HashSet<>(Arrays.asList(
						conversionIncludesEvent,
						"$$$ CONVERSION HEADER $$$"
						)
				))
				.withReplacements(mReplacementMap)
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
			if (file.endsWith(JniConfig.IDL_SUFFIX)) {
				file = file.substring(0, file.length() - JniConfig.IDL_SUFFIX.length());
			}
			mOutput.conversionHeader.writeln("#include \"", mTaoIdlIncludePrefix, file, "C.h\"");
		}
	}

	private void startConversionImpl(final Model model) throws IOException {
		String conversionImplEvent = "$$$ CONVERSION IMPL $$$";
		String encodingDefineEvent = "$$$ DEFINE UTF-8 $$$";
		LineWriter writer = mOutput.conversionImpl;
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile(getTemplateForFile(JniConfig.CORBABINDING_CPP))
				.withOutput(writer.getWriter())
				.withEvents(new HashSet<>(Arrays.asList(conversionImplEvent, encodingDefineEvent)))
				.withReplacements(mReplacementMap)
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						if (event.equals(encodingDefineEvent)) {
							if (CppCorbaGeneratorBuilder.DEFAULT_ENCODING.equals(mCorbaEncoding)) {
								writer.writeln("#define ", JniConfig.UTF_8_DEFINE);
								writer.writeln();
							}
						} else {
							generateSymbols(model);
						}
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
		} else if (element instanceof Declaration) {
			return mInterfaceRenderer;
		} else {
			return null;
		}
	}
}
