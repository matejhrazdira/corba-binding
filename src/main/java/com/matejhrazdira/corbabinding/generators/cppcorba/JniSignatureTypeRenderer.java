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

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.TypeRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.ArrayType;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.idl.types.VoidType;

public class JniSignatureTypeRenderer extends TypeRenderer {

	private final JniScopedRenderer mJniScopedRenderer;

	public JniSignatureTypeRenderer() {
		mJniScopedRenderer = new JniScopedRenderer();
	}

	public JniSignatureTypeRenderer(ScopedName javaPrefix) {
		mJniScopedRenderer = new JniScopedRenderer(javaPrefix);
	}

	@Override
	protected String render(final ScopedName name) {
		StringBuilder sb = new StringBuilder("L");
		mJniScopedRenderer.render(sb, name);
		sb.append(";");
		return sb.toString();
	}

	@Override
	protected String render(final PrimitiveType type) {
		String result;
		switch (type.type) { // TODO: this should really depend on JavaTypeRenderer result...
			case BOOL:
				result = "Z";
				break;
			case OCTET:
				result = "B";
				break;
			case CHAR:
				result = "C";
				break;
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case UNSIGNED_SHORT_INT:
				result = "I";
				break;
			case SIGNED_LONG_LONG_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
				result = "J";
				break;
			case FLOAT:
				result = "F";
				break;
			case DOUBLE:
				result = "D";
				break;
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	@Override
	protected String render(final SequenceType sequence) {
		return "Ljava/util/List;";
	}

	@Override
	protected String render(ArrayType array) {
		return "[" + render(array.elementType);
	}

	@Override
	protected String render(final StringType string) {
		return "Ljava/lang/String;";
	}

	@Override
	protected String render(final VoidType voidType) {
		return "V";
	}
}
