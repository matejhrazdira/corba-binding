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

import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.ScopedRenderer;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.util.List;

public abstract class JavaWithMembersRenderer extends AbsJavaRenderer {

	protected final SymbolResolver mResolver;
	protected final ExpressionRenderer mExpressionRenderer;

	public JavaWithMembersRenderer(final ScopedRenderer scopedRenderer, SymbolResolver resolver, final OutputListener outputListener) {
		super(scopedRenderer, outputListener);
		mResolver = resolver;
		mExpressionRenderer = new JavaExpressionRenderer(scopedRenderer, resolver);
	}

	protected Type getJavaType(Type type) {
		if (type instanceof ScopedName) {
			Type baseType = mResolver.findBaseType((ScopedName) type);
			if (baseType instanceof ScopedName) {
				ScopedName origName = (ScopedName) type;
				ScopedName baseName = (ScopedName) baseType;
				if (origName.name.equals(baseName.name)) {
					return baseType;
				}
			}
			return getJavaType(baseType);
		} else if (type instanceof SequenceType) {
			SequenceType sequence = (SequenceType) type;
			return new SequenceType(getJavaType(sequence.elementType), sequence.bounds);
		} else {
			return type;
		}
	}
}
