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

package com.matejhrazdira.corbabinding.generators.java.projection;

import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.members.Member;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.util.List;
import java.util.Map;

public class JavaUnionProjection extends JavaStructProjection {

	public final Member typeMember;
	public final Map<Member, UnionField> fieldMap;

	public JavaUnionProjection(final Symbol symbol, final ScopedName name, final List<Member> members, Member typeMember, final Map<Member, UnionField> fieldMap) {
		super(symbol, name, members);
		this.typeMember = typeMember;
		this.fieldMap = fieldMap;
	}

	public static class JavaUnionProjectionBuilder {
		private Symbol mSymbol;
		private ScopedName mName;
		private List<Member> mMembers;
		private Member mTypeMember;
		private Map<Member, UnionField> mFieldMap;

		public JavaUnionProjectionBuilder withSymbol(final Symbol symbol) {
			mSymbol = symbol;
			return this;
		}

		public JavaUnionProjectionBuilder withName(final ScopedName name) {
			mName = name;
			return this;
		}

		public JavaUnionProjectionBuilder withMembers(final List<Member> members) {
			mMembers = members;
			return this;
		}

		public JavaUnionProjectionBuilder withTypeMember(final Member typeMember) {
			mTypeMember = typeMember;
			return this;
		}

		public JavaUnionProjectionBuilder withFieldMap(final Map<Member, UnionField> fieldMap) {
			mFieldMap = fieldMap;
			return this;
		}

		public JavaUnionProjection build() {
			return new JavaUnionProjection(mSymbol, mName, mMembers, mTypeMember, mFieldMap);
		}
	}
}
