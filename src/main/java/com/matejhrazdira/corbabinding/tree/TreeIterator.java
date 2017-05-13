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

package com.matejhrazdira.corbabinding.tree;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class TreeIterator<T> implements Iterator<Node<T>> {

	private final TreeIteratorDelegate<T> mDelegate;
	private final Deque<Iterator<Node<T>>> mStack = new LinkedList<>();
	private Node<T> mNext;

	TreeIterator(final Node<T> start) {
		this(start, new TreeIteratorDelegate<T>(){});
	}

	TreeIterator(final Node<T> start, final TreeIteratorDelegate<T> delegate) {
		mDelegate = delegate;
		mNext = start;
		if (mDelegate.shouldExpandNode(mNext, 0)) {
			mStack.push(start.childIterator());
		}
	}

	@Override
	public boolean hasNext() {
		return mNext != null;
	}

	@Override
	public Node<T> next() {
		Node<T> result = mNext;
		computeNext();
		return result;
	}

	private void computeNext() {
		Iterator<Node<T>> it = getNextValidIterator();
		if (it != null) {
			mNext = it.next();
			if (mDelegate.shouldExpandNode(mNext, mStack.size())) {
				mStack.push(mNext.childIterator());
			}
		} else {
			mNext = null;
		}
	}

	private Iterator<Node<T>> getNextValidIterator() {
		Iterator<Node<T>> it;
		while ((it = mStack.peek()) != null && !it.hasNext()) {
			mStack.pop();
		}
		return it;
	}
}
