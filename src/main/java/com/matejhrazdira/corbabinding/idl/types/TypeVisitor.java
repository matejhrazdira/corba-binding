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

import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

/**
 * @todo: missing inlined EnumType.
 */
public interface TypeVisitor {
	void visit(EnumType enumType);
	void visit(ScopedName name);
	void visit(AnyType any);
	void visit(PrimitiveType primitive);
	void visit(SequenceType sequence);
	void visit(StringType string);
	void visit(UnsupportedType unsupportedType);
	void visit(VoidType voidType);
}
