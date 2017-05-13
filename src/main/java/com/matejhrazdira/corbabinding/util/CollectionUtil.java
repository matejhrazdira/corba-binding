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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {

	public static <T> List<T> immutableList(List<T> list) {
		if (list != null) {
			return Collections.unmodifiableList(new ArrayList<>(list));
		} else {
			return Collections.unmodifiableList(new ArrayList<T>());
		}
	}

	public static <T> Iterator<T> immutableIterator(final Iterator<T> iterator) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public T next() {
				return iterator.next();
			}
		};
	}
}
