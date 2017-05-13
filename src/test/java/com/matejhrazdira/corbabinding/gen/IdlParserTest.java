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

package com.matejhrazdira.corbabinding.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matejhrazdira.corbabinding.acceptance.AbsAcceptanceTest;
import com.matejhrazdira.corbabinding.acceptance.ComposedAcceptanceTest;
import com.matejhrazdira.corbabinding.acceptance.SimpleAcceptanceTest;
import com.matejhrazdira.corbabinding.idl.Specification;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IdlParserTest {

	private final SimpleAcceptanceTest mAcceptanceTest = new SimpleAcceptanceTest();
	private final ComposedAcceptanceTest mComposedAcceptanceTest = new ComposedAcceptanceTest();

	@Test
	public void parseSpecificationAcceptanceTest() throws IOException, ParseException {
		testSpecification(mAcceptanceTest, mAcceptanceTest.getSimpleSpecification(), "simple.json");
	}

	@Test
	public void parseSpecificationWithIncludesTest() throws IOException, ParseException {
		testSpecification(mComposedAcceptanceTest, mComposedAcceptanceTest.mainSpecification(), "main.json");
	}

	@Test
	public void parseIncludedSpecificationTest() throws IOException, ParseException {
		testSpecification(mComposedAcceptanceTest, mComposedAcceptanceTest.includedSpecification(), "included.json");
	}

	private void testSpecification(final AbsAcceptanceTest acceptanceTest, final Specification specification, final String outputName) throws ParseException, IOException {
		File actual = acceptanceTest.getActualFile(outputName);
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (FileWriter out = new FileWriter(actual)) {
			gson.toJson(specification, out);
		}
		acceptanceTest.assertFileContentEqueals(outputName);
	}
}