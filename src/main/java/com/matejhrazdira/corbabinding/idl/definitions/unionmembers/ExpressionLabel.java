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

package com.matejhrazdira.corbabinding.idl.definitions.unionmembers;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.util.Validator;

public class ExpressionLabel extends CaseLabel {

	public final ConstExp expression;

	public ExpressionLabel(final ConstExp expression) {
		this.expression = Validator.assertNotNull(expression, "Expression cannot be null.");
	}

	@Override
	public CaseLabel resolved(final IdlElement scope, final SymbolResolver resolver) {
		return new ExpressionLabel(expression.resolved(scope, resolver));
	}
}
