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

import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.util.stream.Collectors;

public class JniImplMethodNameRenderer {

	private final ScopedRenderer mImpl = new ScopedRenderer("Java_", "_", true);

	public String render(final ScopedName name) {
		return mImpl.render(
				new ScopedName(
						name.name.stream()
								.map(n -> n.replace("_", "_1").toString())
								.collect(Collectors.toList())
						, name.fullyQualified
				)
		);
	}
}
