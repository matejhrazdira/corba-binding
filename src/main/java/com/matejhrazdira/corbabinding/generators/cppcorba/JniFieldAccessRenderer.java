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

public class JniFieldAccessRenderer {

	private final FieldAccessImpl mGetImpl = new FieldAccessImpl("Get");
	private final FieldAccessImpl mSetImpl = new FieldAccessImpl("Set");

	public String getMethod(Type type) {
		return mGetImpl.render(type);
	}

	public String setMethod(Type type) {
		return mSetImpl.render(type);
	}

	private static class FieldAccessImpl extends TypeRenderer {

		private final String mAccessMethod;

		public FieldAccessImpl(final String accessMethod) {
			mAccessMethod = accessMethod;
		}

		@Override
		protected String render(final ScopedName name) {
			return mAccessMethod + "ObjectField";
		}

		@Override
		protected String render(final PrimitiveType type) {
			String result;
			switch (type.type) { // TODO: this should really depend on JavaTypeRenderer result...
				case BOOL:
					result = mAccessMethod + "BooleanField";
					break;
				case OCTET:
					result = mAccessMethod + "ByteField";
					break;
				case CHAR:
					result = mAccessMethod + "CharField";
					break;
				case SIGNED_SHORT_INT:
				case SIGNED_LONG_INT:
				case UNSIGNED_SHORT_INT:
					result = mAccessMethod + "IntField";
					break;
				case SIGNED_LONG_LONG_INT:
				case UNSIGNED_LONG_INT:
				case UNSIGNED_LONG_LONG_INT:
					result = mAccessMethod + "LongField";
					break;
				case FLOAT:
					result = mAccessMethod + "FloatField";
					break;
				case DOUBLE:
					result = mAccessMethod + "DoubleField";
					break;
				default:
					throw new CorbabindingException("Type " + type.type + " is not supported.");
			}
			return result;
		}

		@Override
		protected String render(final SequenceType sequence) {
			return mAccessMethod + "ObjectField";
		}

		@Override
		protected String render(ArrayType array) {
			return mAccessMethod + "ObjectField";
		}

		@Override
		protected String render(final StringType string) {
			return mAccessMethod + "ObjectField";
		}

		@Override
		protected String render(final VoidType voidType) {
			throw new CorbabindingException("Cannot access field with type 'void'.");
		}
	}
}
