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

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class NodeTest {

	private Node<String> createTree() {
		return new Node<>("root", Arrays.asList(
				new Node<>("a", Arrays.asList(
						new Node<>("aa"),
						new Node<>("ab"),
						new Node<>("ac"),
						new Node<>("ad")
				)),
				new Node<>("b"),
				new Node<>("c", Arrays.asList(
						new Node<>("ca"),
						new Node<>("cb", Arrays.asList(
								new Node<>("cba"),
								new Node<>("cbb"),
								new Node<>("cbc")
						)),
						new Node<>("cc", Arrays.asList(
								new Node<>("cca"),
								new Node<>("ccb"),
								new Node<>("ccc")
						)),
						new Node<>("cd"),
						new Node<>("ce", Arrays.asList(
								new Node<>("cea"),
								new Node<>("ceb", Arrays.asList(
										new Node<>("ceba"),
										new Node<>("cebb", Arrays.asList(
												new Node<>("cebba"),
												new Node<>("cebbb"),
												new Node<>("cebbc")
										)),
										new Node<>("cebc"))),
								new Node<>("cec", Arrays.asList(
										new Node<>("ceca"),
										new Node<>("cecb"),
										new Node<>("cecc", Arrays.asList(
												new Node<>("cecca"),
												new Node<>("ceccb"),
												new Node<>("ceccc")
										))
								))
						))
				)),
				new Node<>("d")
		));
	}

	private String getDepthFirstOrder() {
		return "root, a, aa, ab, ac, ad, b, c, ca, cb, cba, cbb, cbc, cc, cca, ccb, ccc, cd, ce, cea, ceb, ceba, cebb, cebba, cebbb, cebbc, cebc, cec, ceca, cecb, cecc, cecca, ceccb, ceccc, d";
	}

	@Test
	public void streamNodesTest() throws Exception {
		Node<String> root = createTree();
		String expected = getDepthFirstOrder();
		String actual = root.stream().map(Node::getData).collect(Collectors.joining(", "));
		assertEquals(expected, actual);
	}

	@Test
	public void findFirstTest() {
		Node<String> root = createTree();
		String searchedElement = "cbb";
		List<String> elements = new ArrayList<>();
		Optional<String> result = root.stream()
				.map(Node::getData)
				.filter( (s) -> {
					elements.add(s);
					return s.equals(searchedElement);
				} )
				.findFirst();

		String actual = result.orElse(null);
		assertEquals(searchedElement, actual);

		String allElements = getDepthFirstOrder();
		int expectedLength = allElements.indexOf(searchedElement) + searchedElement.length();
		String expectedElements = allElements.substring(0, expectedLength);
		String actualElements = elements.stream().collect(Collectors.joining(", "));
		assertEquals(expectedElements, actualElements);
	}

	@Test
	public void iteratorTest() {
		Node<String> tree = createTree();
		List<String> elements = new ArrayList<>();
		for (final Node<String> node : tree) {
			elements.add(node.getData());
		}
		String expected = getDepthFirstOrder();
		String actual = elements.stream().collect(Collectors.joining(", "));
		assertEquals(expected, actual);
	}

	@Test
	public void iteratorWithDelegateDepthLimitTest() {
		Node<String> tree = createTree();
		List<String> elements = new ArrayList<>();
		Iterator<Node<String>> iterator = tree.iterator(new TreeIteratorDelegate<String>() {
			@Override
			public boolean shouldExpandNode(final Node<String> node1, final int depth) {
				return depth < 2;
			}
		});
		while (iterator.hasNext()) {
			final Node<String> node = iterator.next();
			elements.add(node.getData());
		}
		String expected = "root, a, aa, ab, ac, ad, b, c, ca, cb, cc, cd, ce, d";
		String actual = elements.stream().collect(Collectors.joining(", "));
		assertEquals(expected, actual);
	}

	@Test
	public void iteratorWithDelegateFindItemTest() {
		Node<String> tree = createTree();
		List<String> elements = new ArrayList<>();
		final String searchedElement = "ceccb";
		Iterator<Node<String>> iterator = tree.iterator(new TreeIteratorDelegate<String>() {
			@Override
			public boolean shouldExpandNode(final Node<String> node, final int depth) {
				return depth == 0 || node.getData().charAt(depth - 1) == searchedElement.charAt(depth - 1);
//				return depth == 0 || searchedElement.startsWith(node1.getData()); // equivalent
			}
		});
		while (iterator.hasNext()) {
			final Node<String> node = iterator.next();
			elements.add(node.getData());
			if (node.getData().equals(searchedElement)) {
				break;
			}
		}
		String expected = "root, a, b, c, ca, cb, cc, cd, ce, cea, ceb, cec, ceca, cecb, cecc, cecca, ceccb";
		String actual = elements.stream().collect(Collectors.joining(", "));
		assertEquals(expected, actual);
	}
}