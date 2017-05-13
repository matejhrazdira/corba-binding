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

import static org.junit.Assert.assertEquals;

public class JniCacheRendererTest {

	@Test
	public void accessCtorFieldTest() {
		JniCacheRenderer renderer = new JniCacheRenderer();
		String actual = renderer.renderGlobalAccess(
				new ScopedName(
						Arrays.asList("com", "matejhrazdira", "generated", "SomeStruct", "ctor"),
						true
				)
		);
		String expected = "_jni_->com.matejhrazdira.generated.SomeStruct.ctor";
		assertEquals(expected, actual);
	}

	@Test
	public void initCtorFieldTest() {
		JniCacheRenderer renderer = new JniCacheRenderer();
		String actual = renderer.renderQualifiedMember(
				new ScopedName(
						Arrays.asList("com", "matejhrazdira", "generated", "SomeStruct", "ctor"),
						true
				)
		);
		String expected = "com.matejhrazdira.generated.SomeStruct.ctor";
		assertEquals(expected, actual);
	}
}