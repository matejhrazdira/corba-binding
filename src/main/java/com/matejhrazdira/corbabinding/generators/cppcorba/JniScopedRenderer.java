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

public class JniScopedRenderer extends ScopedRenderer {

	private static final String GLOBAL_SCOPE = "";
	private static final String SCOPE_DELIMITER = "/";

	public JniScopedRenderer() {
		super(GLOBAL_SCOPE, SCOPE_DELIMITER, true);
	}

	public JniScopedRenderer(final ScopedName scope) {
		super(scope, GLOBAL_SCOPE, SCOPE_DELIMITER);
	}
}
