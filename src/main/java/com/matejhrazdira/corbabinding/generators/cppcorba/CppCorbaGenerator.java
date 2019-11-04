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
	private final File mOutputDir;
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
		mOutputDir = output;
		mReplacementMap = JniConfig.getStringStringMap(mCorbaEncoding, javaTemplateProjection);
		mOutputDir.mkdirs();

		mOutput = new CorbaOutput.Builder()
				.withJniCacheHeader(new JniCacheHeaderWriter(createBuffer()))
				.withJniCacheHeaderClient(new JniCacheHeaderWriter(createBuffer()))
				.withJniCacheImpl(createBuffer())
				.withConversionHeader(createBuffer())
				.withConversionImpl(createBuffer())
				.withJniImplHeader(createBuffer())
				.withJniImplImpl(createBuffer())
				.withTypeCacheEntries(createBuffer())
				.createCorbaOutput();
		mOutputListener = outputListener;
		mEnumRenderer = new CorbaEnumRenderer(enumProjectionProvider, mOutput, outputListener);
		mStructRenderer = new CorbaStructRenderer(structProjectionProvider, mOutput, outputListener);
		mUnionRenderer = new CorbaUnionRenderer(unionProjectionProvider, mOutput, outputListener);
		mInterfaceRenderer = new CorbaInterfaceRenderer(symbolResolver, interfaceProjectionProvider, mOutput, outputListener);
	}

	private LineWriter createBuffer() {
		return new LineWriter(JniConfig.INDENTATION, new StringWriter());
	}

	public void generate(Model model) throws IOException {
		try {
			generateSymbols(model);
		} finally {
			mOutput.close();
		}
	}

	private void generateSymbols(final Model model) throws IOException {
		mOutput.typeCacheEntries.increaseLevel();
		mOutput.jniCacheImpl.increaseLevel();
		mOutput.jniCacheHeader.increaseLevel();
		mOutput.jniCacheHeaderClient.increaseLevel();
		mOutput.jniCacheHeaderClient.increaseLevel();

		for (Symbol s : model.getSymbols()) {
			AbsCorbaRenderer renderer = getRendererForSymbol(s);
			if (renderer != null) {
				logSymbol(s);
				renderer.render(s);
				mOutput.conversionHeader.writeln();
				mOutput.conversionImpl.writeln();
			}
		}

		mOutput.jniCacheHeader.exitScope();
		mOutput.jniCacheHeaderClient.exitScope();

		writeTemplateSpec(JniConfig.CB_LIB_H, null);
		writeTemplateSpec(JniConfig.CB_LIB_CPP, null);

		writeTemplateSpec(
				JniConfig.JNI_IMPL_PRIVATE_H,
				new TemplateSpec.Builder().withReplacements(mReplacementMap)
		);
		writeOutput(
				JniConfig.JNI_IMPL_PRIVATE_CPP,
				createEntry("$$$ TYPE CACHE ENTRIES $$$", mOutput.typeCacheEntries)
		);

		writeOutput(
				JniConfig.JNI_IMPL_H,
				createEntry("$$$ JNI IMPL HEADER $$$", mOutput.jniImplHeader)
		);
		writeOutput(
				JniConfig.JNI_IMPL_CPP,
				createEntry("$$$ JNI IMPL IMPL $$$", mOutput.jniImplImpl)
		);

		writeOutput(
				JniConfig.CORBABINDING_H,
				createEntry("$$$ CONVERSION INCLUDES $$$", generateConversionIncludes(model)),
				createEntry("$$$ CONVERSION HEADER $$$", mOutput.conversionHeader)
		);
		writeOutput(
				JniConfig.CORBABINDING_CPP,
				new Entry("$$$ DEFINE UTF-8 $$$", generateUtf8Define()),
				createEntry("$$$ CONVERSION IMPL $$$", mOutput.conversionImpl)
		);

		writeOutput(
				JniConfig.JNI_CACHE_H,
				createEntry("$$$ JNI CACHE HEADER INTERFACE $$$", mOutput.jniCacheHeader.getWriter()),
				createEntry("$$$ JNI CACHE HEADER CLIENT $$$", mOutput.jniCacheHeaderClient.getWriter())
		);
		writeOutput(
				JniConfig.JNI_CACHE_CPP,
				createEntry("$$$ JNI CACHE IMPL $$$", mOutput.jniCacheImpl)
		);
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

	private void logSymbol(final Symbol s) {
		mOutputListener.onInfo("Processing cppcorba for '" + s.name.getQualifiedName() + "'.");
	}

	private void writeTemplateSpec(final String file, TemplateSpec.Builder template) throws IOException {
		if (template == null) {
			template = new TemplateSpec.Builder();
		}
		try (LineWriter lineWriter = new LineWriter(JniConfig.INDENTATION, new FileOutputStream(new File(mOutputDir, file)))) {
			template.withTemplateFile(getTemplateForFile(file));
			template.withOutput(lineWriter.getWriter());
			new TemplateProcessor().process(template.createTemplateSpec());
		}
	}

	private String getTemplateForFile(String file) {
		return "/" + file + ".template";
	}

	private void writeOutput(final String file, final Entry... entries) throws IOException {
		Map<String, String> replacementMap = new HashMap<>(mReplacementMap);
		for (final Entry entry : entries) {
			replacementMap.put(entry.key, entry.value);
		}
		TemplateSpec.Builder builder = new TemplateSpec.Builder().withReplacements(replacementMap);
		writeTemplateSpec(file, builder);
	}

	private Entry createEntry(String key, LineWriter writer) {
		String jniImplImpl = getWriterContent(writer.getWriter());
		return new Entry(key, jniImplImpl);
	}

	private String getWriterContent(final Writer writer) {
		StringBuffer typeCacheBuffer = ((StringWriter) writer).getBuffer();
		if (typeCacheBuffer.length() > 0) {
			typeCacheBuffer.setLength(typeCacheBuffer.length() - 1);
		}
		return typeCacheBuffer.toString();
	}

	private LineWriter generateConversionIncludes(final Model model) throws IOException {
		LineWriter writer = createBuffer();
		for (String file : model.mFiles) {
			file = new File(file).getName();
			if (file.endsWith(JniConfig.IDL_SUFFIX)) {
				file = file.substring(0, file.length() - JniConfig.IDL_SUFFIX.length());
			}
			writer.writeln("#include \"", mTaoIdlIncludePrefix, file, "C.h\"");
		}
		return writer;
	}

	private String generateUtf8Define() {
		if (CppCorbaGeneratorBuilder.DEFAULT_ENCODING.equals(mCorbaEncoding)) {
			return "#define " + JniConfig.UTF_8_DEFINE + "\n";
		} else {
			return "";
		}
	}

	private static class Entry {
		public final String key;
		public final String value;

		public Entry(final String key, final String value) {
			this.key = key;
			this.value = value;
		}
	}
}
