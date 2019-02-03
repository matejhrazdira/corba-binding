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
import com.matejhrazdira.corbabinding.idl.types.*;

public class JniJavaTypeRenderer extends TypeRenderer {

	@Override
	protected String render(final ScopedName name) {
		return "jobject";
	}

	@Override
	protected String render(final PrimitiveType type) {
		String result;
		switch (type.type) { // TODO: this should really depend on JavaTypeRenderer result...
			case BOOL:
				result = "jboolean";
				break;
			case OCTET:
				result = "jbyte";
				break;
			case CHAR:
				result = "jchar";
				break;
			case SIGNED_SHORT_INT:
			case SIGNED_LONG_INT:
			case UNSIGNED_SHORT_INT:
				result = "jint";
				break;
			case SIGNED_LONG_LONG_INT:
			case UNSIGNED_LONG_INT:
			case UNSIGNED_LONG_LONG_INT:
				result = "jlong";
				break;
			case FLOAT:
				result = "jfloat";
				break;
			case DOUBLE:
				result = "jdouble";
				break;
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	@Override
	protected String render(final SequenceType sequence) {
		return "jobject";
	}

	@Override
	protected String render(final StringType string) {
		// Should return "jstring" but doing this would completely break
		// templates in cpp code for arrays, var types, dynamic allocation etc
		// because cpp templates cannot return or accept "subtype" i.e.
		// template<typename T> T * convert(jobject in)
		// cannot be specialized as
		// template<> char * convert<char>(jstring in)
		// unfortunately this means that there will be plenty of checks for strings
		// but on the other hand, strings get special treatment anyway...
		return "jobject";
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
			emptyValue = "nullptr";
		}
		return emptyValue;
	}

	@Override
	protected String render(final VoidType voidType) {
		return "jobject";
	}
}
