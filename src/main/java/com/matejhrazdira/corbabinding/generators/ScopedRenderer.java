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

package com.matejhrazdira.corbabinding.generators;

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.matejhrazdira.corbabinding.generators.java.StringBuilderSupplier.asString;

public class ScopedRenderer {

	private final ScopedName mScope;
	private final boolean mFullyQualified;
	private final String mGlobalScope;
	private final String mScopeDelimiter;

	public ScopedRenderer(final ScopedName scope, final String globalScope, final String scopeDelimiter) {
		this(
				Validator.assertNotNull(scope, "Scope cannot be null."),
				globalScope,
				scopeDelimiter,
				scope.fullyQualified
		);
	}

	public ScopedRenderer(final String globalScope, final String scopeDelimiter, final boolean fullyQualified) {
		this(null, globalScope, scopeDelimiter, fullyQualified);
	}

	private ScopedRenderer(final ScopedName scope, final String globalScope, final String scopeDelimiter, final boolean fullyQualified) {
		mScope = scope;
		mFullyQualified = fullyQualified;
		mGlobalScope = globalScope;
		mScopeDelimiter = scopeDelimiter;
	}

	public String getScopeDelimiter() {
		return mScopeDelimiter;
	}

	public ScopedName getScope() {
		return mScope;
	}

	public ScopedName getNameInScope(final ScopedName name) {
		if (mScope != null) {
			return name.moveToScope(mScope);
		} else {
			return new ScopedName(name.name, mFullyQualified);
		}
	}

	public String render(ScopedName name) {
		return asString(this::render, name);
	}

	/**
	 * Renders name in specified scope.
	 */
	public void render(final StringBuilder result, final ScopedName name) {
		final ScopedName scopedName = getNameInScope(name);
		renderRaw(result, scopedName);
	}

	public String renderRaw(ScopedName scopedName) {
		return asString(this::renderRaw, scopedName);
	}

	/**
	 * Renders name without changing it's scope.
	 */
	public void renderRaw(final StringBuilder result, final ScopedName scopedName) {
		renderGlobalScope(result, scopedName);
		renderNameElements(result, scopedName.name);
	}

	private void renderGlobalScope(final StringBuilder result, ScopedName scopedName) {
		if (scopedName.fullyQualified) {
			result.append(mGlobalScope);
		}
	}

	private void renderNameElements(final StringBuilder result, final List<String> nameElements) {
		for (String nameElement : nameElements) {
			result.append(nameElement);
			result.append(mScopeDelimiter);
		}
		if (nameElements.size() > 0) {
			result.setLength(result.length() - mScopeDelimiter.length());
		}
	}

	public ScopedName parse(String name) {
		boolean fullyQualified;
		if (mGlobalScope.isEmpty()) {
			fullyQualified = true;
		} else {
			fullyQualified = name.startsWith(mGlobalScope);
			if (fullyQualified) {
				name = name.substring(mGlobalScope.length(), name.length());
			}
		}

		String[] components = name.split(Pattern.quote(mScopeDelimiter));
		return new ScopedName(Arrays.asList(components), fullyQualified);
	}
}
