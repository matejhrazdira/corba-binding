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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbsAcceptanceTest {

	protected File mTestDir;
	protected File mActualDir;
	protected File mExpectedDir;

	public AbsAcceptanceTest(final File testDir, final String actualDir, final String expectedDir) {
		mTestDir = testDir;
		mActualDir = new File(testDir, actualDir);
		mExpectedDir = new File(testDir, expectedDir);
		mExpectedDir.mkdirs();
		mActualDir.mkdirs();
	}

	public void assertFileContentEqueals(String name) throws IOException {
		File expected = getExpectedFile(name);
		File actual = getActualFile(name);
		assertFileContentEquals(expected, actual);
	}

	private void assertFileContentEquals(File expected, File actual) throws IOException {
		String expectedStr = FileUtils.readFileToString(expected, "utf-8");
		String actualStr = FileUtils.readFileToString(actual, "utf-8");
		assertEquals(expectedStr, actualStr);
	}

	public void assertDirectoryContentEquals() throws IOException {
		assertFilesEqualRecursive(mExpectedDir, mActualDir);
	}

	public void assertDirectoryContentEquals(String name) throws IOException {
		File actual = getActualFile(name);
		File expected = getExpectedFile(name);
		assertFilesEqualRecursive(expected, actual);
	}

	private void assertFilesEqualRecursive(final File expected, final File actual) throws IOException {
		if (expected.exists()) {
			assertTrue(actual.exists());
		} else {
			assertFalse(actual.exists());
			return;
		}
		if (expected.isDirectory()) {
			assertTrue(actual.isDirectory());
			String[] expectedChildren = expected.list();
			String[] actualChildren = actual.list();
			assertEquals(
					"directory " + actual.getPath() + " contains unexpected number of files",
					expectedChildren.length,
					actualChildren.length
			);
			for (final String child : expectedChildren) {
				assertFilesEqualRecursive(
						new File(expected, child),
						new File(actual, child)
				);
			}
		} else {
			assertFileContentEquals(expected, actual);
		}
	}

	public File getActualFile(String name) {
		return new File(mActualDir, name);
	}

	private File getExpectedFile(final String name) {
		return new File(mExpectedDir, name);
	}

	public File getActualDir() {
		return mActualDir;
	}
}
