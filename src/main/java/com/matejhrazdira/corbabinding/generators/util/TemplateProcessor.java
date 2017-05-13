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

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TemplateProcessor {

	public static final String TEMPLATE_START = "$$$ BGN $$$";

	public interface TemplateListener {
		void onTemplateEvent(TemplateSpec template, String event) throws IOException;
	}

	public void process(final TemplateSpec spec) throws IOException {
		try (InputStream stream = getClass().getResourceAsStream(spec.templateFile)) {
			BufferedReader input = new BufferedReader(new InputStreamReader(stream));
			Writer output = spec.output;
			List<Replacement> replacements = spec.replacements.entrySet().stream()
					.map(entry -> new Replacement(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
			String line;
			boolean started = false;
			while ((line = input.readLine()) != null) {
				if (!started) {
					if (line.contains(TEMPLATE_START)) {
						started = true;
					}
					continue;
				}
				boolean eventFound = false;
				for (String event : spec.events) {
					if (line.contains(event)) {
						spec.listener.onTemplateEvent(spec, event);
						eventFound = true;
					}
				}
				if (eventFound) {
					continue;
				}
				for (final Replacement replacement : replacements) {
					line = replacement.apply(line);
				}
				output.write(line);
				output.write('\n');
				output.flush();
			}
		}
	}

	private static class Replacement {
		public final Pattern pattern;
		public final String replacement;

		public Replacement(final String pattern, final String replacement) {
			this.pattern = Pattern.compile(Pattern.quote(pattern));
			this.replacement = Matcher.quoteReplacement(replacement);
		}

		public String apply(String str) {
			return pattern.matcher(str).replaceAll(replacement);
		}
	}
}
