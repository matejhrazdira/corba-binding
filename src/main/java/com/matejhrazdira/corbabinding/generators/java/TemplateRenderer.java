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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaTemplateProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaTemplateProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.TemplateProcessor;
import com.matejhrazdira.corbabinding.generators.util.TemplateSpec;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TemplateRenderer implements JavaTemplateProjectionProvider {

	public static final String VAR_TYPE = "Var";
	public static final String CORBA_SYS_EXCEPTION = "CorbaException";
	public static final String CORBA_PROVIDER = "CorbaProvider";
	public static final String ALREADY_DISPOSED_EXCEPTION = "AlreadyDisposedException";
	public static final String EVENT_CONSUMER_CLASS = "EventConsumer";
	public static final String EVENT_CONSUMER_CALLBACK = "onEvent";
	public static final String PACKAGE_TOKEN = "$$$PACKAGE$$$";
	public static final String CLASS_TOKEN = "$$$CLS_NAME$$$";
	public static final String VAR_GET = "get";
	public static final String VAR_SET = "set";

	private final ScopedRenderer mScopedRenderer;
	private final ScopedName mScope;
	private final ScopedName mVarType;
	private final ScopedName mCorbaExceptionType;
	private final ScopedName mAlreadyDisposedExceptionType;
	private final ScopedName mCorbaProviderType;
	private final ScopedName mEventConsumerType;

	public TemplateRenderer(final ScopedRenderer scopedRenderer) {
		mScopedRenderer = scopedRenderer;
		mScope = scopedRenderer.getScope();
		mVarType = createName(VAR_TYPE);
		mCorbaExceptionType = createName(CORBA_SYS_EXCEPTION);
		mAlreadyDisposedExceptionType = createName(ALREADY_DISPOSED_EXCEPTION);
		mCorbaProviderType = createName(CORBA_PROVIDER);
		mEventConsumerType = createName(EVENT_CONSUMER_CLASS);
	}

	private ScopedName createName(final String name) {
		return new ScopedName(Arrays.asList(name), false);
	}

	public String getVarType() {
		return render(mVarType);
	}

	public String getCorbaExceptionType() {
		return render(mCorbaExceptionType);
	}

	@Override
	public JavaTemplateProjection getProjection() {
		return new JavaTemplateProjection(
				ScopedName.nameInScope(mScope, VAR_TYPE),
				ScopedName.nameInScope(mScope, CORBA_SYS_EXCEPTION),
				ScopedName.nameInScope(mScope, ALREADY_DISPOSED_EXCEPTION),
				ScopedName.nameInScope(mScope, CORBA_PROVIDER),
				ScopedName.nameInScope(mScope, EVENT_CONSUMER_CLASS)
				);
	}

	private String render(final ScopedName varType) {
		return mScopedRenderer.render(varType);
	}

	public void render(final File outputDir) throws IOException {
		renderVarType(outputDir);
		renderCorbaExceptionType(outputDir);
		renderAlreadyDisposedExceptionType(outputDir);
		renderCorbaProvider(outputDir);
		renderEventConsumer(outputDir);
	}

	private void renderVarType(final File outputDir) throws IOException {
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("$$$GET_NAME$$$", VAR_GET);
		replacements.put("$$$SET_NAME$$$", VAR_SET);
		renderImpl(outputDir, mVarType, replacements);
	}

	private void renderCorbaExceptionType(final File outputDir) throws IOException {
		renderImpl(outputDir, mCorbaExceptionType, new HashMap<>());
	}

	private void renderAlreadyDisposedExceptionType(final File outputDir) throws IOException {
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("$$$CORBA_SYS_EXCEPTION$$$", CORBA_SYS_EXCEPTION);
		renderImpl(outputDir, mAlreadyDisposedExceptionType, replacements);
	}

	private void renderCorbaProvider(final File outputDir) throws IOException {
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("$$$CORBA_SYS_EXCEPTION$$$", CORBA_SYS_EXCEPTION);
		replacements.put("$$$ALREADY_DISPOSED_EXCEPTION$$$", ALREADY_DISPOSED_EXCEPTION);
		renderImpl(outputDir, mCorbaProviderType, replacements);
	}

	private void renderEventConsumer(final File outputDir) throws IOException {
		final Map<String, String> replacements = new HashMap<>();
		replacements.put("$$$CORBA_SYS_EXCEPTION$$$", CORBA_SYS_EXCEPTION);
		replacements.put("$$$CORBA_PROVIDER$$$", CORBA_PROVIDER);
		replacements.put("$$$ON_EVENT_CALLBACK$$$", EVENT_CONSUMER_CALLBACK);
		renderImpl(outputDir, mEventConsumerType, replacements);
	}

	private void renderImpl(final File outputDir, final ScopedName name, final Map<String, String> extraReplacements) throws IOException {
		try (FileWriter writer = new FileWriter(Util.prepareFileForName(outputDir, name))) {
			final Map<String, String> replacements = getBaseReplacements(name.getBaseName());
			replacements.putAll(extraReplacements);
			TemplateSpec spec = new TemplateSpec.Builder()
					.withTemplateFile(getTemplateForFile(name.getBaseName() + Util.JAVA_EXT))
					.withOutput(writer)
					.withReplacements(replacements)
					.createTemplateSpec();
			new TemplateProcessor().process(spec);
		}
	}

	private Map<String, String> getBaseReplacements(String name) {
		final Map<String, String> replacements = new HashMap<>();
		replacements.put(PACKAGE_TOKEN, mScopedRenderer.renderRaw(mScope));
		replacements.put(CLASS_TOKEN, name);
		return replacements;
	}

	private String getTemplateForFile(String file) {
		return "/" + file + ".template";
	}
}
