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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaTemplateProjection;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TemplateRendererTest {

	@Test
	public void getProjection() {
		ScopedName generatorScope = new ScopedName(
				Arrays.asList("com", "matejhrazdira", "generated")
				, true
		);
		JavaScopedRenderer scopedRenderer = new JavaScopedRenderer(generatorScope);

		ScopedName cbLibScope = PojoGenerator.CB_LIB_PACKAGE.moveToScope(generatorScope);
		JavaTemplateProjection expected = new JavaTemplateProjection(
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.VAR_TYPE),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.CORBA_SYS_EXCEPTION),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.ALREADY_DISPOSED_EXCEPTION),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.CORBA_PROVIDER),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.EVENT_SERVICE_CLASS),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.EVENT_CONSUMER_CLASS),
				ScopedName.nameInScope(cbLibScope, TemplateRenderer.EVENT_PRODUCER_CLASS)
		);

		JavaTemplateProjection actual = new TemplateRenderer(scopedRenderer).getProjection();

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		assertEquals(gson.toJson(expected), gson.toJson(actual));
	}
}