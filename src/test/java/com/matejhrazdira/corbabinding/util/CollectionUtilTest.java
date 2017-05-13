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

package com.matejhrazdira.corbabinding.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CollectionUtilTest {

	@Test
	public void immutableListTest() {
		ArrayList<String> mutableList = new ArrayList<>(Arrays.asList("foo", "bar"));
		List<String> immutableList = CollectionUtil.immutableList(mutableList);
		assertEquals(mutableList, immutableList);
		assertImmutable(immutableList);
	}

	@Test
	public void emptyImmutableListTest() {
		List<String> immutableList = CollectionUtil.immutableList(new ArrayList<String>());
		assertEquals(0, immutableList.size());
		assertImmutable(immutableList);
	}

	@Test
	public void nullImmutableListTest() {
		List<String> immutableList = CollectionUtil.immutableList(null);
		assertEquals(0, immutableList.size());
		assertImmutable(immutableList);
	}

	@Test
	public void immutableIterator() {
		ArrayList<String> mutableList = new ArrayList<>(Arrays.asList("foo", "bar"));
		Iterator<String> iterator = CollectionUtil.immutableIterator(mutableList.iterator());
		ArrayList<String> elements = new ArrayList<>();
		while (iterator.hasNext()) {
			String element = iterator.next();
			elements.add(element);
			assertImmutable(iterator);
		}
		assertEquals(mutableList, elements);
	}

	private void assertImmutable(Iterator<String> iterator) {
		try {
			iterator.remove();
			fail();
		} catch (UnsupportedOperationException e) {
		}
	}

	private void assertImmutable(List<String> list) {
		try {
			list.add("new element");
			fail();
		} catch (UnsupportedOperationException e) {
		}
		try {
			list.set(0, "new element");
			fail();
		} catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
		}
		try {
			list.remove(0);
			fail();
		} catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
		}
	}




}