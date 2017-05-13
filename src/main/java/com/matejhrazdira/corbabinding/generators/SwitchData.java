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

import com.matejhrazdira.corbabinding.generators.SwitchRenderingUtility.CaseRenderer;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.util.Data;

import java.util.List;

public class SwitchData<T> {
	public final LineWriter writer;
	public final String switchExpr;
	public final boolean enumBased;
	public final boolean hasCasesInBlocks;
	public final List<Data<UnionField, T>> fields;
	public final CaseRenderer<T> caseRenderer;
	public final Object extra;

	private SwitchData(final LineWriter writer, final String switchExpr, final boolean enumBased, final boolean hasCasesInBlocks, final List<Data<UnionField, T>> fields, final CaseRenderer<T> caseRenderer, final Object extra) {
		this.writer = writer;
		this.switchExpr = switchExpr;
		this.enumBased = enumBased;
		this.hasCasesInBlocks = hasCasesInBlocks;
		this.fields = fields;
		this.caseRenderer = caseRenderer;
		this.extra = extra;
	}

	public static class SwitchDataBuilder<T> {
		private LineWriter mWriter;
		private String mSwitchExpr;
		private boolean mEnumBased;
		private boolean mHasCasesInBlocks = false;
		private List<Data<UnionField, T>> mFields;
		private CaseRenderer<T> mCaseRenderer;
		private Object mExtra;

		public SwitchDataBuilder<T> withWriter(final LineWriter writer) {
			mWriter = writer;
			return this;
		}

		public SwitchDataBuilder<T> withSwitchExpr(final String switchExpr) {
			mSwitchExpr = switchExpr;
			return this;
		}

		public SwitchDataBuilder<T> withEnumBased(final boolean enumBased) {
			mEnumBased = enumBased;
			return this;
		}

		public SwitchDataBuilder<T> withHasCasesInBlocks(final boolean hasCasesInBlocks) {
			mHasCasesInBlocks = hasCasesInBlocks;
			return this;
		}

		public SwitchDataBuilder<T> withFields(final List<Data<UnionField, T>> fields) {
			mFields = fields;
			return this;
		}

		public SwitchDataBuilder<T> withCaseRenderer(final CaseRenderer<T> caseRenderer) {
			mCaseRenderer = caseRenderer;
			return this;
		}

		public SwitchDataBuilder<T> withExtra(final Object extra) {
			mExtra = extra;
			return this;
		}

		public SwitchData<T> build() {
			return new SwitchData<>(mWriter, mSwitchExpr, mEnumBased, mHasCasesInBlocks, mFields, mCaseRenderer, mExtra);
		}
	}
}
