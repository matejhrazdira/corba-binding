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
import java.io.FileNotFoundException;
import java.io.IOException;

public class ComposedAcceptanceTest extends AbsAcceptanceTest {

	public ComposedAcceptanceTest() {
		super(new File("composed"), "actual", "expected");
	}

	public Specification includedSpecification() throws ParseException, IOException {
		return getSpecification("included.idl");
	}

	public Specification mainSpecification() throws ParseException, IOException {
		return getSpecification("main.idl");
	}

	private Specification getSpecification(final String fileName) throws FileNotFoundException, ParseException {
		IdlParser p = new IdlParser(new FileInputStream(new File(mTestDir, fileName)));
		Specification specification = p.specification(fileName);
		return specification;
	}
}
