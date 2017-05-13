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

package com.matejhrazdira.corbabinding.idl;

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.idl.definitions.TypeDeclarator;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.tree.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SymbolResolver {

	private static class GlobalScope extends IdlElement {
		// TODO: mutable class (only by SymbolResolver) that is updated as new symbols are added to top scope
	}

	private final Node<Symbol> mSymbolCache = new Node<>(
			new Symbol(
					ScopedName.nameInScope(null, ScopedName.SCOPE_DELIMITER),
					new GlobalScope()));
	private final HashMap<IdlElement, Node<Symbol>> mElementTable = new HashMap<>();

	public SymbolResolver(List<Symbol> symbols) {
		symbols.forEach(this::addSymbol);
	}

	private void addSymbol(Symbol symbol) {
		Node<Symbol> parentSymbol = getParentSymbolNode(symbol);
		Node<Symbol> symbolNode = new Node<>(symbol);
		checkSymbolNotPresent(parentSymbol, symbol); // TODO: if found, merge
		parentSymbol.add(symbolNode);
		mElementTable.put(symbol.element, symbolNode);
	}

	private Node<Symbol> getParentSymbolNode(final Symbol symbol) {
		Node<Symbol> parentSymbol = findInScopeRecursive(mSymbolCache, symbol.name.getScope().iterator());
		if (parentSymbol == null) {
			throw new IllegalStateException("Parent scope for " + symbol.name.getQualifiedName() + " not found.");
		}
		return parentSymbol;
	}

	private Node<Symbol> findInScopeRecursive(Node<Symbol> scope, Iterator<String> nameIterator) {
		if (nameIterator.hasNext()) {
			String requiredName = nameIterator.next();
			Iterator<Node<Symbol>> scopeIterator = scope.childIterator();
			while (scopeIterator.hasNext()) {
				Node<Symbol> scopeElement = scopeIterator.next();
				String scopeElementName = scopeElement.getData().name.getBaseName();
				if (requiredName.equals(scopeElementName)) {
					return findInScopeRecursive(scopeElement, nameIterator);
				}
			}
			return null;
		} else {
			return scope;
		}
	}

	private void checkSymbolNotPresent(final Node<Symbol> scope, final Symbol symbol) {
		Iterator<Node<Symbol>> it = scope.childIterator();
		while (it.hasNext()) {
			Node<Symbol> node = it.next();
			if (node.getData().name.getBaseName().equals(symbol.name.getBaseName())) {
				throw new CorbabindingException("Definition for symbol " + symbol.name.getQualifiedName() + " already present.");
			}
		}
	}

	public ScopedName resolve(ScopedName symbolName, IdlElement scopeElement) {

		Node<Symbol> scopeNode = scopeElement != null ? getSymbolNode(scopeElement) : mSymbolCache;
		Node<Symbol> searchScope = symbolName.fullyQualified ? mSymbolCache : scopeNode;

		Node<Symbol> result = null;
		while (searchScope != null) {
			result = findInScope(searchScope, symbolName);
			if (result != null) {
				break;
			}
			searchScope = searchScope.getParent();
		}

		if (result == null) {
			throw new CorbabindingException("Cannot resolve name " + symbolName.getQualifiedName() + " from scope " + scopeNode.getData().name.getQualifiedName());
		}

		return result.getData().name;
	}

	private Node<Symbol> getSymbolNode(final IdlElement element) {
		Node<Symbol> symbolNode = mElementTable.get(element);
		if (symbolNode == null) {
			throw new NoSuchElementException("Cannot find Symbol for element " + element);
		}
		return symbolNode;
	}

	private Node<Symbol> findInScope(Node<Symbol> scope, ScopedName name) {
		return findInScopeRecursive(scope, name.name.iterator());
	}

	public Symbol findSymbol(ScopedName name) {
		if (!name.fullyQualified) {
			throw new IllegalArgumentException("Name must be fully qualified " + name.getQualifiedName());
		}
		Node<Symbol> symbolNode = findInScope(mSymbolCache, name);
		return symbolNode != null ? symbolNode.getData() : null;
	}

	public Type findBaseType(final ScopedName name) {
		Symbol symbol = findSymbol(name);
		try {
			if (symbol.element instanceof TypeDeclarator) {
				TypeDeclarator typedef = (TypeDeclarator) symbol.element;
				Type type = typedef.type;
				if (type instanceof ScopedName) {
					return findBaseType((ScopedName) type);
				} else {
					return type;
				}
			} else {
				return symbol.name;
			}
		} catch (ClassCastException e) {
			throw new CorbabindingException(
					"name " + name.getQualifiedName() + " doesn't represent " + Type.class.getName() +
					" but " + symbol.element.getClass().getName()
			);
		}
	}

	/** For tests only. */
	final Iterator<Node<Symbol>> getSymbolIterator() {
		return mSymbolCache.iterator();
	}
}
