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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matejhrazdira.corbabinding.acceptance.ComposedAcceptanceTest;
import com.matejhrazdira.corbabinding.acceptance.SimpleAcceptanceTest;
import com.matejhrazdira.corbabinding.gen.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationTest {

	private final SimpleAcceptanceTest mAcceptanceTest = new SimpleAcceptanceTest();

	@Test
	public void getDeclaredNamesAcceptanceTest() throws IOException, ParseException {
		List<Symbol> declaredNames = mAcceptanceTest.getSimpleSpecification().getDeclaredSymbols();
		String outputName = "declaredNames.txt";
		File actual = mAcceptanceTest.getActualFile(outputName);
		try (FileWriter out = new FileWriter(actual)) {
			String result = declaredNames.stream().map( (s) -> s.name.getQualifiedName() ).collect(Collectors.joining("\n"));
			out.write(result);
		}
		mAcceptanceTest.assertFileContentEqueals(outputName);
	}

	@Test
	public void getDeclaredSymbolsAcceptanceTest() throws IOException, ParseException {
		List<Symbol> declaredSymbols = mAcceptanceTest.getSimpleSpecification().getDeclaredSymbols();
		String outputName = "declaredSymbols.json";
		File actual = mAcceptanceTest.getActualFile(outputName);
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter out = new FileWriter(actual)) {
			gson.toJson(declaredSymbols, out);
		}
		mAcceptanceTest.assertFileContentEqueals(outputName);
	}

	@Test
	public void getResolvedSpecificationTest() throws IOException, ParseException {
		Specification specification = mAcceptanceTest.getSimpleSpecification();
		SymbolResolver resolver = new SymbolResolver(specification.getDeclaredSymbols());
		Resolvable resolved = specification.resolved(null, resolver);
		String outputName = "resolved.json";
		File actual = mAcceptanceTest.getActualFile(outputName);
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter out = new FileWriter(actual)) {
			gson.toJson(resolved, out);
		}
		mAcceptanceTest.assertFileContentEqueals(outputName);
	}

	@Test
	public void getResolvedSpecificationTestIncluded() throws IOException, ParseException {
		ComposedAcceptanceTest acceptanceTest = new ComposedAcceptanceTest();
		Specification specification = acceptanceTest.includedSpecification();
		SymbolResolver resolver = new SymbolResolver(specification.getDeclaredSymbols());
		Resolvable resolved = specification.resolved(null, resolver);
		String outputName = "resolved_included.json";
		File actual = acceptanceTest.getActualFile(outputName);
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter out = new FileWriter(actual)) {
			gson.toJson(resolved, out);
		}
		acceptanceTest.assertFileContentEqueals(outputName);
	}

	@Test
	public void getResolvedSpecificationTestMain() throws IOException, ParseException {
		ComposedAcceptanceTest acceptanceTest = new ComposedAcceptanceTest();

		Specification includedSpecification = acceptanceTest.includedSpecification();
		List<Symbol> includedSymbols = includedSpecification.getDeclaredSymbols();

		Specification mainSpecification = acceptanceTest.mainSpecification();
		List<Symbol> mainSymbols = mainSpecification.getDeclaredSymbols();

		List<Symbol> allSymbols = new ArrayList<>();
		allSymbols.addAll(includedSymbols);
		allSymbols.addAll(mainSymbols);

		SymbolResolver resolver = new SymbolResolver(allSymbols);
		Resolvable resolved = mainSpecification.resolved(null, resolver);
		String outputName = "resolved_main.json";
		File actual = acceptanceTest.getActualFile(outputName);
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter out = new FileWriter(actual)) {
			gson.toJson(resolved, out);
		}
		acceptanceTest.assertFileContentEqueals(outputName);
	}
}