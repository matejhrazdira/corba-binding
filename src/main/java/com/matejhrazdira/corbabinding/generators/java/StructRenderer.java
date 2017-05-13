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
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaStructProjectionProvider;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.StructType;
import com.matejhrazdira.corbabinding.idl.definitions.members.ArrayDeclarator;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StructRenderer extends JavaClassRenderer implements JavaStructProjectionProvider {

	public StructRenderer(final ScopedRenderer scopedRenderer, final SymbolResolver resolver, final OutputListener outputListener) {
		super(scopedRenderer, resolver, outputListener);
	}

	@Override
	public JavaStructProjection getProjection(final Symbol symbol) {
		JavaProjection projection = super.getProjection(symbol);
		return new JavaStructProjection(projection.symbol, projection.name, getMembers(symbol));
	}

	private List<Member> getMembers(final Symbol symbol) {
		StructType struct = (StructType) symbol.element;
		ArrayList<Member> result = new ArrayList<>(struct.members.size());
		for (final Member member : struct.members) {
			if (member.declarator instanceof ArrayDeclarator) {
				throw new CorbabindingException(
						"Can't generate field " + member.declarator.name +
								" in " + symbol.name.getQualifiedName() +
								" : Array declarators are not supported");
			}
			result.add(new Member(member.declarator, getJavaType(member.type)));
		}
		return result;
	}

	@Override
	protected RenderingData createRenderingData(final LineWriter writer, final Symbol symbol, final ScopedName name) {
		return new RenderingData(writer, name, getFields(symbol));
	}

	private List<Field> getFields(final Symbol symbol) {
		return getMembers(symbol).stream()
				.map(member -> new Field(
						mTypeRenderer.render(member.type),
						member.declarator.name,
						member)
				)
				.collect(Collectors.toList());
	}
}
