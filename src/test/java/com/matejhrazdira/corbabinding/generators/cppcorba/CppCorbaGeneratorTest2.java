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

import com.matejhrazdira.corbabinding.acceptance.SimpleAcceptanceTest;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.generators.java.PojoGenerator;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaTemplateProjection;
import com.matejhrazdira.corbabinding.idl.Specification;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.util.NoOpOutputListener;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CppCorbaGeneratorTest2 {

	@Test
	public void processSimpleModel() throws IOException, ParseException {
		SimpleAcceptanceTest simpleAcceptanceTest = new SimpleAcceptanceTest("cppcorba");
		Specification specification = simpleAcceptanceTest.getSimpleSpecificationWithoutArrayDeclarators();
		Model model = new Model(specification);
		PojoGenerator pojoGenerator = new PojoGenerator(
				new File(simpleAcceptanceTest.getActualDir(), "java"),
				"com.matejhrazdira.pojos",
				model
		);
		CppCorbaGenerator cppCorbaGenerator = new CppCorbaGeneratorBuilder()
				.withOutput(new File(simpleAcceptanceTest.getActualDir(), "cpp"))
				.withSymbolResolver(model.getResolver())
				.withEnumProjectionProvider(pojoGenerator.getEnumProjection())
				.withStructProjectionProvider(pojoGenerator.getStructProjection())
				.withUnionProjectionProvider(pojoGenerator.getUnionProjection())
				.withInterfaceProjectionProvider(pojoGenerator.getInterfaceProjection())
				.withJavaTemplateProjection(new JavaTemplateProjection(
						new ScopedName(Arrays.asList("Var"), true),
						new ScopedName(Arrays.asList("CorbaException"), true),
						new ScopedName(Arrays.asList("AlreadyDisposedException"), true),
						new ScopedName(Arrays.asList("CorbaProvider"), true),
						new ScopedName(Arrays.asList("EventConsumer"), true)))
				.withOutputListener(new NoOpOutputListener())
				.createCppCorbaGenerator();
		cppCorbaGenerator.generate(model);

		simpleAcceptanceTest.assertDirectoryContentEquals();
	}
}