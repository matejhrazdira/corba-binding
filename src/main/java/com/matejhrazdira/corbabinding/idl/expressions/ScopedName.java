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

package com.matejhrazdira.corbabinding.idl.expressions;

import com.matejhrazdira.corbabinding.idl.IdlElement;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.idl.types.TypeVisitor;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: why is it IdlElement? Is it necessary?
public class ScopedName extends IdlElement implements Type, ExpressionElement {

	public static final String SCOPE_DELIMITER = "::";

	public final List<String> name;
	public final boolean fullyQualified;

	private transient String mQualifiedName;

	public ScopedName(List<String> scopedName, boolean fullyQualified) {
		this.name = Validator.notEmptyImmutableList(scopedName, "Scoped name cannot be empty.");
		this.fullyQualified = fullyQualified;
	}

	public static ScopedName nameInScope(ScopedName scope, String name) {
		Validator.assertNotEmpty(name, "Name cannot be empty.");
		boolean fullyQualified = false;
		List<String> scopedName = new ArrayList<>();
		if (scope != null) {
			fullyQualified = scope.fullyQualified;
			scopedName.addAll(scope.name);
		}
		scopedName.add(name);
		return new ScopedName(scopedName, fullyQualified);
	}

	public ScopedName inScope(List<String> scope) {
		if (fullyQualified) {
			throw new IllegalArgumentException("Name is already fully qualified");
		}
		ArrayList<String> scopedName = new ArrayList<>();
		if (scope != null) {
			scopedName.addAll(scope);
		}
		scopedName.addAll(name);
		return new ScopedName(scopedName, false);
	}

	public ScopedName moveToScope(ScopedName scope) {
		Validator.assertNotNull(scope, "Scope cannot be null.");
		ArrayList<String> scopedName = new ArrayList<>();
		scopedName.addAll(scope.name);
		scopedName.addAll(name);
		return new ScopedName(scopedName, scope.fullyQualified);
	}

	public ScopedName fullyQualified() {
		return new ScopedName(name, true);
	}

	public String getQualifiedName() {
		if (mQualifiedName == null) {
			String prefix = fullyQualified ? SCOPE_DELIMITER : "";
			String suffix = "";
			mQualifiedName = name.stream().collect(Collectors.joining(SCOPE_DELIMITER, prefix, suffix));
		}
		return mQualifiedName;
	}

	public String getBaseName() {
		return name.get(name.size() - 1);
	}

	public List<String> getScope() {
		return name.subList(0, name.size() - 1);
	}

	public ScopedName getScopeName() {
		List<String> scope = getScope();
		if (scope.size() > 0) {
			return new ScopedName(scope, fullyQualified);
		} else {
			return null;
		}
	}

	@Override
	public ScopedName resolved(IdlElement scope, final SymbolResolver resolver) {
		return resolver.resolve(this, scope);
	}

	public void accept(ExpressionElementVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(TypeVisitor visitor) {
		visitor.visit(this);
	}
}
