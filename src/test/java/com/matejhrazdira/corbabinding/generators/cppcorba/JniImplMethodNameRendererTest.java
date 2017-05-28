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

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class JniImplMethodNameRendererTest {

	@Test
	public void renderMethodWithoutUnderscores() {
		String actual = new JniImplMethodNameRenderer().render(
				new ScopedName(
						Arrays.asList("com", "matejhrazdira", "generated", "SomeClass", "someMethod"),
						true
				)
		);
		String expected = "Java_com_matejhrazdira_generated_SomeClass_someMethod";
		assertEquals(expected, actual);
	}

	@Test
	public void renderMethodWithUnderscores() {
		String actual = new JniImplMethodNameRenderer().render(
				new ScopedName(
						Arrays.asList("com", "matej_hrazdira", "generated", "Some_Class", "_some_method_"),
						true
				)
		);
		String expected = "Java_com_matej_1hrazdira_generated_Some_1Class__1some_1method_1";
		assertEquals(expected, actual);
	}
}