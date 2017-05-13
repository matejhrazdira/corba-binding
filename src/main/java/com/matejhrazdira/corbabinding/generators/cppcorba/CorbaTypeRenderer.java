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
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.StringType;

public class CorbaTypeRenderer extends TypeRenderer {

	private final CorbaScopedRenderer mCorbaScopedRenderer = new CorbaScopedRenderer();

	@Override
	protected String render(final ScopedName name) {
		return mCorbaScopedRenderer.render(name);
	}

	@Override
	protected String render(final PrimitiveType type) {
		String result;
		switch (type.type) { // TODO: this should really depend on JavaTypeRenderer result...
			case BOOL:
				result = "::CORBA::Boolean";
				break;
			case OCTET:
				result = "::CORBA::Octet";
				break;
			case CHAR:
				result = "::CORBA::Char";
				break;
			case SIGNED_SHORT_INT:
				result = "::CORBA::Short";
				break;
			case UNSIGNED_SHORT_INT:
				result = "::CORBA::UShort";
				break;
			case SIGNED_LONG_INT:
				result = "::CORBA::Long";
				break;
			case UNSIGNED_LONG_INT:
				result = "::CORBA::ULong";
				break;
			case SIGNED_LONG_LONG_INT:
				result = "::CORBA::LongLong";
				break;
			case UNSIGNED_LONG_LONG_INT:
				result = "::CORBA::ULongLong";
				break;
			case FLOAT:
				result = "::CORBA::Float";
				break;
			case DOUBLE:
				result = "::CORBA::Double";
				break;
			default:
				throw new CorbabindingException("Type " + type.type + " is not supported.");
		}
		return result;
	}

	@Override
	protected String render(final SequenceType sequence) {
		throw new CorbabindingException("SequenceType is not supported, should be handled by template resolution.");
	}

	@Override
	protected String render(final StringType string) {
		throw new CorbabindingException("StringType is not supported, should be handled by template resolution.");
	}
}
