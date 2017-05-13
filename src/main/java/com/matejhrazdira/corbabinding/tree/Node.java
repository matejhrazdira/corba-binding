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

import com.matejhrazdira.corbabinding.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Node<T> implements Iterable<Node<T>> {

	private Node<T> mParent = null;
	private List<Node<T>> mChildren = new ArrayList<>();
	private T mData;

	public Node() {
	}

	public Node(T data) {
		mData = data;
	}

	public Node(T data, List<Node<T>> children) {
		mData = data;
		addAll(children);
	}

	public Node<T> copy() {
		return new Node<>(mData);
	}

	public Node<T> recursiveCopy() {
		Node<T> result = copy();
		for (Node<T> child : mChildren) {
			result.add(child.recursiveCopy());
		}
		return result;
	}

	public T getData() {
		return mData;
	}

	public List<Node<T>> getChildren() {
		return CollectionUtil.immutableList(mChildren);
	}

	public Node<T> getParent() {
		return mParent;
	}

	public boolean isLeaf() {
		return mChildren.size() == 0;
	}

	public boolean isRoot() {
		return mParent == null;
	}

	public Node<T> getRoot() {
		Node<T> root = this;
		Node<T> element = null;
		while ((element = root.getParent()) != null) {
			root = element;
		}
		return root;
	}

	public void add(Node<T> child) {
		if (child.mParent != null) {
			child.mParent.remove(child);
		}
		child.mParent = this;
		mChildren.add(child);
	}

	public void addAll(List<Node<T>> children) {
		children.forEach(this::add);
	}

	public void remove(Node<T> child) {
		int index = mChildren.indexOf(child);
		if (index != -1) {
			mChildren.remove(index);
			child.mParent = null;
		}
	}

	public void removeAll(List<Node<T>> children) {
		children.forEach(this::remove);
	}

	public void removeAll() {
		removeAll(getChildren());
	}

	public Iterator<Node<T>> childIterator() {
		return CollectionUtil.immutableIterator(mChildren.iterator());
	}

	@Override
	public Iterator<Node<T>> iterator() {
		return new TreeIterator<>(this);
	}

	public Iterator<Node<T>> iterator(TreeIteratorDelegate<T> delegate) {
		return new TreeIterator<>(this, delegate);
	}

	public Stream<Node<T>> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}