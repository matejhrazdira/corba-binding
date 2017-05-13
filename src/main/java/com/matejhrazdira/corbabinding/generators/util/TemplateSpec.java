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

package com.matejhrazdira.corbabinding.generators.util;

import java.io.Writer;
import java.util.Map;
import java.util.Set;

public class TemplateSpec {

	public static class Builder {
		private String mTemplateFile;
		private Set<String> mEvents;
		private Map<String, String> mReplacements;
		private Writer mOutput;
		private TemplateProcessor.TemplateListener mListener;

		public Builder withTemplateFile(final String templateFile) {
			mTemplateFile = templateFile;
			return this;
		}

		public Builder withEvents(final Set<String> events) {
			mEvents = events;
			return this;
		}

		public Builder withReplacements(final Map<String, String> replacements) {
			mReplacements = replacements;
			return this;
		}

		public Builder withOutput(final Writer output) {
			mOutput = output;
			return this;
		}

		public Builder withListener(final TemplateProcessor.TemplateListener listener) {
			mListener = listener;
			return this;
		}

		public TemplateSpec createTemplateSpec() {
			return new TemplateSpec(mTemplateFile, mEvents, mReplacements, mOutput, mListener);
		}
	}

	public String templateFile;
	public Set<String> events;
	public Map<String, String> replacements;
	public Writer output;
	public TemplateProcessor.TemplateListener listener;

	public TemplateSpec(final String templateFile, final Set<String> events, final Map<String, String> replacements, final Writer output, final TemplateProcessor.TemplateListener listener) {
		this.templateFile = templateFile;
		this.events = events;
		this.replacements = replacements;
		this.output = output;
		this.listener = listener;
	}
}
