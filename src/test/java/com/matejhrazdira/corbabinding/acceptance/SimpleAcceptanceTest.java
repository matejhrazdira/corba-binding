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

package com.matejhrazdira.corbabinding.acceptance;

import com.matejhrazdira.corbabinding.gen.IdlParser;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.idl.Specification;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleAcceptanceTest extends AbsAcceptanceTest {

	public SimpleAcceptanceTest() {
		this("simple");
	}

	public SimpleAcceptanceTest(String root) {
		super(new File(root), "actual", "expected");
	}

	public Specification getSimpleSpecification() throws ParseException, IOException {
		IdlParser p = new IdlParser(new FileInputStream(new File(mTestDir, "simple.idl")));
		Specification specification = p.specification("simple.idl");
		return specification;
	}

	public Specification getSimpleSpecificationWithoutArrayDeclarators() throws ParseException, IOException {
		IdlParser p = new IdlParser(new FileInputStream(new File(mTestDir, "simple-without-array-declarators.idl")));
		Specification specification = p.specification("simple-without-array-declarators.idl");
		return specification;
	}
}
