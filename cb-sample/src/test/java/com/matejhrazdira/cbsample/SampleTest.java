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

package com.matejhrazdira.cbsample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleStruct;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleUnion;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SampleTest {

	static {
		File libPath = new File("libjnilibs.so");
		System.load(libPath.getAbsolutePath());
	}

	@Test
	public void copyUnion() throws Exception {

		SimpleUnion input = new SimpleUnion.Builder()
				.withSimpleStructInUnion(
						new SimpleStruct().builder()
								.withStringMember("This is some string.")
								.withIntMember(12345)
								.build()
				)
				.build();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		long start;

		start = System.currentTimeMillis();
		Sample.initCache();
		System.out.println("Acceptance.initCache() took " + (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		SimpleUnion output = Sample.copyUnion(input);
		System.out.println("Acceptance.copyUnion() took " + (System.currentTimeMillis() - start) + " ms");
		System.out.println(gson.toJson(input));
		assertTrue(input != output);
		assertEquals(gson.toJson(input), gson.toJson(output));
	}

}
