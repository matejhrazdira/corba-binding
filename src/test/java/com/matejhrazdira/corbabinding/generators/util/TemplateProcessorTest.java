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

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class TemplateProcessorTest {

	@Test
	public void process() throws IOException {
		HashMap<String, String> replacements = new HashMap<>();
		replacements.put("$$$REPLACED$$$", "_replaced_");
		StringWriter writer = new StringWriter();
		TemplateSpec spec = new TemplateSpec.Builder()
				.withTemplateFile("/TemplateProcessorTest.template")
				.withOutput(writer)
				.withEvents(new HashSet<>(Arrays.asList("$$$EVENT$$$")))
				.withReplacements(replacements)
				.withListener(new TemplateProcessor.TemplateListener() {
					@Override
					public void onTemplateEvent(final TemplateSpec template, final String event) throws IOException {
						template.output.write("This template is going to replace '" + event + "' with something different.\n");
					}
				})
				.createTemplateSpec();
		TemplateProcessor processor = new TemplateProcessor();
		processor.process(spec);
		String expected =
				"First line of output.\n" +
				"This template is going to replace '$$$EVENT$$$' with something different.\n" +
				"Something that should be _replaced_.\n" +
				"And last line\n";
		assertEquals(expected, writer.toString());
	}

}