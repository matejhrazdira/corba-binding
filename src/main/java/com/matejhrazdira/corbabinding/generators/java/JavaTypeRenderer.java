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

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.generators.TypeRenderer;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.*;

public class JavaTypeRenderer extends TypeRenderer {

	private ScopedRenderer mScopedRenderer;

	public JavaTypeRenderer(final ScopedRenderer scopedRenderer) {
		mScopedRenderer = scopedRenderer;
	}

	@Override
	protected String render(final ScopedName name) {
		return mScopedRenderer.render(name);
	}

	@Override
	protected String render(final PrimitiveType type) {
		String result;
		switch (type.type) {
			case BOOL:
				result = "boolean";
				break;
			case OCTET:
				result = "byte";
				break;
			case CHAR:
				result = "char";
				break;
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case UNSIGNED_SHORT_INT:
				result = "int";
				break;
			case SIGNED_LONG_LONG_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
				result = "long";
				break;
			case FLOAT:
				result = "float";
				break;
			case DOUBLE:
				result = "double";
				break;
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	@Override
	protected String render(SequenceType sequence) {
		return "java.util.List<" + renderGenericType(sequence.elementType) + ">";
	}

	public String renderGenericType(Type type) {
		if (type instanceof PrimitiveType) {
			return renderGenericType((PrimitiveType) type);
		} else {
			return render(type);
		}
	}

	private String renderGenericType(final PrimitiveType type) {
		String result;
		switch (type.type) {
			case BOOL:
				result = "Boolean";
				break;
			case OCTET:
				result = "Byte";
				break;
			case CHAR:
				result = "Char";
				break;
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case UNSIGNED_SHORT_INT:
				result = "Integer";
				break;
			case SIGNED_LONG_LONG_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
				result = "Long";
				break;
			case FLOAT:
				result = "Float";
				break;
			case DOUBLE:
				result = "Double";
				break;
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	@Override
	protected String render(StringType string) {
		return "String";
	}

	@Override
	protected String render(final VoidType voidType) {
		return "void";
	}

	public String getEmptyValue(final Type type) {
		final String emptyValue;
		if (type instanceof PrimitiveType) {
			PrimitiveType primitiveType = (PrimitiveType) type;
			switch (primitiveType.type) {
				case BOOL:
					emptyValue = "false";
					break;
				default:
					emptyValue = "0";
					break;
			}
		} else {
			emptyValue = "null";
		}
		return emptyValue;
	}

	public String getMinValue(final PrimitiveType type) {
		String result;
		switch (type.type) {
			case UNSIGNED_SHORT_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
				result = "(" + render(type) + ") 0";
				break;
			case OCTET:
			case CHAR:
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case SIGNED_LONG_LONG_INT:
			case FLOAT:
			case DOUBLE:
				result = renderGenericType(type) + ".MIN_VALUE";
				break;
			case BOOL:
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	public String getMaxValue(final PrimitiveType type) {
		String result;
		switch (type.type) {
			case UNSIGNED_SHORT_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
			case OCTET:
			case CHAR:
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case SIGNED_LONG_LONG_INT:
			case FLOAT:
			case DOUBLE:
				result = renderGenericType(type) + ".MAX_VALUE";
				break;
			case BOOL:
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

}
