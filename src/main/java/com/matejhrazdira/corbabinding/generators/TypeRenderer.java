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

package com.matejhrazdira.corbabinding.generators;

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.*;

public abstract class TypeRenderer {

	public String render(Type type) {
		TypeVisitorImpl visitor = new TypeVisitorImpl(); // TODO: too many allocations;
		type.accept(visitor);
		return visitor.getResult();
	}

	protected abstract String render(ScopedName name);

	protected abstract String render(PrimitiveType type);

	protected abstract String render(SequenceType sequence);

	protected abstract String render(ArrayType array);

	protected abstract String render(StringType string);

	protected abstract String render(VoidType voidType);

	private class TypeVisitorImpl implements TypeVisitor {

		private String mResult;

		@Override
		public void visit(final EnumType enumType) {
			failWithException(enumType);
		}

		@Override
		public void visit(final ScopedName name) {
			mResult = render(name);
		}

		@Override
		public void visit(final AnyType any) {
			failWithException(any);
		}

		@Override
		public void visit(final PrimitiveType primitive) {
			mResult = render(primitive);
		}

		@Override
		public void visit(final SequenceType sequence) {
			mResult = render(sequence);
		}

		@Override
		public void visit(ArrayType array) {
			mResult = render(array);
		}

		@Override
		public void visit(final StringType string) {
			mResult = render(string);
		}

		@Override
		public void visit(final UnsupportedType unsupportedType) {
			failWithException(unsupportedType);
		}

		@Override
		public void visit(final VoidType voidType) {
			mResult = render(voidType);
		}

		private void failWithException(final Type type) {
			throw new CorbabindingException(type.getClass().getSimpleName() + " is not supported.");
		}

		public String getResult() {
			return mResult;
		}
	}
}
