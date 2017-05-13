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

import java.util.List;

public class Validator {

	public static <T> List<T> notEmptyImmutableList(List<T> list, String message) throws IllegalArgumentException {
		assertNotEmpty(list, message);
		return CollectionUtil.immutableList(list);
	}

	public static <T> void assertNotEmpty(List<T> list, String message) throws IllegalArgumentException {
		if (list == null || list.size() == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	public static String assertNotEmpty(final String item, final String message) {
		assertNotNull(item, message);
		if (item.trim().length() == 0) {
			throw new IllegalArgumentException(message);
		}
		return item;
	}

	public static <T> T assertNotNull(T item, String message) throws IllegalArgumentException {
		if (item == null) {
			throw new IllegalArgumentException(message);
		}
		return item;
	}
}
