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

package com.matejhrazdira.corbabinding.idl.types;

public class PrimitiveType implements Type {

	public enum Type {
		BOOL,

		OCTET,

		CHAR,

		SIGNED_SHORT_INT,
		SIGNED_LONG_INT,
		SIGNED_LONG_LONG_INT,

		UNSIGNED_SHORT_INT,
		UNSIGNED_LONG_INT,
		UNSIGNED_LONG_LONG_INT,

		FLOAT,
		DOUBLE,
	}

	public final Type type;

	public PrimitiveType(final Type type) {
		if (type == null) {
			throw new IllegalArgumentException("Type must be specified.");
		}
		this.type = type;
	}

	public void accept(TypeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "PrimitiveType(" + type + ")";
	}
}
