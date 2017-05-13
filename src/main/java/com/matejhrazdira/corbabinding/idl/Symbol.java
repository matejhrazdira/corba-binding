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

package com.matejhrazdira.corbabinding.idl;

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.Validator;

public class Symbol {

	public final ScopedName name;
	public final IdlElement element;

	public Symbol(final ScopedName name, final IdlElement element) {
		this.name = Validator.assertNotNull(name, "Name cannot be null.");
		this.element = Validator.assertNotNull(element, "Element cannot be null.");
	}

	public Symbol withName(ScopedName name) {
		return new Symbol(name, element);
	}
}
