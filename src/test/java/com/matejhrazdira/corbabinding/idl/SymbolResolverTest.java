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

package com.matejhrazdira.corbabinding.idl;

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.acceptance.SimpleAcceptanceTest;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.SequenceType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.tree.Node;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SymbolResolverTest {

	private final SimpleAcceptanceTest mAcceptanceTest = new SimpleAcceptanceTest();

	@Test
	public void testSymbolResolver() throws IOException, ParseException {
		Specification specification = mAcceptanceTest.getSimpleSpecification();
		List<Symbol> declaredSymbols = specification.getDeclaredSymbols();
		SymbolResolver resolver = new SymbolResolver(declaredSymbols);
		String expected = declaredSymbols.stream().map( s -> s.name.getQualifiedName()).collect(Collectors.joining("\n"));
		String actual = debugString(resolver);
		assertEquals(expected, actual);
	}

	@Test
	public void testResolveSymbol() throws IOException, ParseException {
		SymbolResolver resolver = getSymbolResolver();

		ScopedName simpleIdlName = new ScopedName(Arrays.asList("SimpleIdl"), true);
		Symbol simpleIdl = resolver.findSymbol(simpleIdlName);
		assertNotNull(simpleIdl);
		ScopedName simpleStructName = new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true);
		Symbol simpleStruct = resolver.findSymbol(simpleStructName);
		assertNotNull(simpleStruct);
		ScopedName includedStructName = new ScopedName(Arrays.asList("IncludedIdl", "IncludedStruct"), true);
		Symbol includedStruct = resolver.findSymbol(includedStructName);
		assertNotNull(includedStruct);

		ScopedName includedStructFromSimpleStruct = resolver.resolve(includedStructName, simpleStruct.element);
		assertEquals(includedStructName.getQualifiedName(), includedStructFromSimpleStruct.getQualifiedName());

		ScopedName includedStructFromSimpleStructRelative = resolver.resolve(new ScopedName(includedStructName.name, false), simpleStruct.element);
		assertEquals(includedStructName.getQualifiedName(), includedStructFromSimpleStructRelative.getQualifiedName());
	}

	@Test(expected = CorbabindingException.class)
	public void testResolveUnknownPrimitiveType() throws IOException, ParseException {
		SymbolResolver resolver = getSymbolResolver();

		ScopedName simpleStructName = new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true);
		Symbol simpleStruct = resolver.findSymbol(simpleStructName);
		assertNotNull(simpleStruct);

		resolver.resolve(new ScopedName(Arrays.asList("foo", "bar"), false), simpleStruct.element);
	}


	@Test
	public void testResolveTypedefedPrimitiveType() throws IOException, ParseException {
		SymbolResolver resolver = getSymbolResolver();
		ScopedName typedefName = new ScopedName(Arrays.asList("SimpleIdl", "time"), true);
		Type baseType = resolver.findBaseType(typedefName);

		assertNotNull(baseType);
		assertTrue(baseType instanceof PrimitiveType);
		assertEquals(PrimitiveType.Type.SIGNED_LONG_LONG_INT, ((PrimitiveType) baseType).type);
	}

	@Test
	public void testResolveTypedefedSequenceType() throws IOException, ParseException {
		SymbolResolver resolver = getSymbolResolver();
		ScopedName typedefName = new ScopedName(Arrays.asList("SimpleIdl", "structs"), true);
		Type baseType = resolver.findBaseType(typedefName);

		assertNotNull(baseType);
		assertTrue(baseType instanceof SequenceType);
		SequenceType sequenceType = (SequenceType) baseType;
		assertTrue(sequenceType.elementType instanceof ScopedName);
		ScopedName element = (ScopedName) sequenceType.elementType;
		assertEquals(
				new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true).getQualifiedName(),
				element.getQualifiedName()
		);
	}

	@Test
	public void testResolveTypedefedStructType() throws IOException, ParseException {
		SymbolResolver resolver = getSymbolResolver();
		ScopedName typedefName = new ScopedName(Arrays.asList("SimpleIdl", "typedefedStruct"), true);
		Type baseType = resolver.findBaseType(typedefName);

		assertNotNull(baseType);
		assertTrue(baseType instanceof ScopedName);
		ScopedName structName = (ScopedName) baseType;
		assertEquals(
				new ScopedName(Arrays.asList("SimpleIdl", "SimpleStruct"), true).getQualifiedName(),
				structName.getQualifiedName()
		);
	}

	private SymbolResolver getSymbolResolver() throws ParseException, IOException {
		return new Model(mAcceptanceTest.getSimpleSpecification()).getResolver();
	}

	public String debugString(SymbolResolver resolver) {
		StringBuilder sb = new StringBuilder();
		Iterator<Node<Symbol>> it = resolver.getSymbolIterator();
		while (it.hasNext()) {
			Node<Symbol> name = it.next();
			if (name.isRoot()) {
				continue;
			}
			ArrayList<String> names = new ArrayList<>();
			while (!name.isRoot()) {
				names.add(name.getData().name.getBaseName());
				name = name.getParent();
			}
			Collections.reverse(names);
			String nameStr = names.stream().collect(Collectors.joining("::", name.getData().name.getBaseName(), ""));
			sb.append(nameStr).append("\n");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
}