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

import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import org.junit.Before;

import java.io.StringWriter;

public class CorbaRendererTest {
	private static final String INDENTATION = "\t";
	protected CorbaOutput mOutput;
	protected StringWriter mJniCacheHeader;
	protected StringWriter mJniCacheImpl;
	protected StringWriter mConversionHeader;
	protected StringWriter mConversionImpl;
	protected StringWriter mJniImplPrivate;

	@Before
	public void setup() {
		mJniCacheHeader = new StringWriter();
		mJniCacheImpl = new StringWriter();
		mConversionHeader = new StringWriter();
		mConversionImpl = new StringWriter();
		mJniImplPrivate = new StringWriter();
		mOutput = new CorbaOutput.Builder()
				.withJniCacheHeader(
						new JniCacheHeaderWriter(new LineWriter(INDENTATION, mJniCacheHeader))
				)
				.withJniCacheImpl(new LineWriter(INDENTATION, mJniCacheImpl))
				.withConversionHeader(new LineWriter(INDENTATION, mConversionHeader))
				.withConversionImpl(new LineWriter(INDENTATION, mConversionImpl))
				.withTypeCacheEntries(new LineWriter(INDENTATION, mJniImplPrivate))
				.createCorbaOutput();
	}
}
