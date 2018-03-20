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

import com.matejhrazdira.corbabinding.acceptance.AbsAcceptanceTest;
import com.matejhrazdira.corbabinding.acceptance.ComposedAcceptanceTest;
import com.matejhrazdira.corbabinding.acceptance.SimpleAcceptanceTest;
import com.matejhrazdira.corbabinding.gen.IdlParser;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.idl.Specification;
import com.matejhrazdira.corbabinding.model.Model;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PojoGeneratorTest {

	private final SimpleAcceptanceTest mAcceptanceTest = new SimpleAcceptanceTest();
	private final ComposedAcceptanceTest mComposedAcceptanceTest = new ComposedAcceptanceTest();

	@Test
	public void generatePojosTest() throws IOException, ParseException {
		Specification specification = mAcceptanceTest.getSimpleSpecificationWithoutArrayDeclarators();
		Model model = new Model(specification);
		PojoGenerator generator = new PojoGenerator(mAcceptanceTest.getActualDir(), "com.matejhrazdira.pojos", model);
		generator.generatePojos();
		mAcceptanceTest.assertDirectoryContentEquals("com");
	}

	@Test
	public void generatePojosWithIncludesTest() throws IOException, ParseException {
		Specification includedSpecification = mComposedAcceptanceTest.includedSpecification();
		Specification mainSpecification = mComposedAcceptanceTest.mainSpecification();
		Model model = new Model(includedSpecification, mainSpecification);
		PojoGenerator generator = new PojoGenerator(mComposedAcceptanceTest.getActualDir(), "com.matejhrazdira.pojos", model);
		generator.generatePojos();
		mComposedAcceptanceTest.assertDirectoryContentEquals("com");
	}

	@Test
	public void generatePojosWithCommentsTest() throws IOException, ParseException {
		final AbsAcceptanceTest test = new AbsAcceptanceTest("comments");
		IdlParser p = new IdlParser(new FileInputStream(new File(test.getTestDir(), "comments.idl")));
		Specification specification = p.specification("comments.idl");
		Model model = new Model(specification);
		PojoGenerator generator = new PojoGenerator(test.getActualDir(), "com.matejhrazdira.pojos", model);
		generator.generatePojos();
		test.assertDirectoryContentEquals("com");
	}
}