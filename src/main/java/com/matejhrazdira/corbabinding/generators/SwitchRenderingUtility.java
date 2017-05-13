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

import com.matejhrazdira.corbabinding.CorbabindingException;
import com.matejhrazdira.corbabinding.generators.ExpressionRenderer;
import com.matejhrazdira.corbabinding.generators.SwitchData;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.CaseLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.ExpressionLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.ExpressionElement;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.util.Data;

import java.io.IOException;

public class SwitchRenderingUtility {

	@FunctionalInterface
	public interface CaseRenderer<T> {
		void writeCaseBody(final SwitchData<T> data, final Data<UnionField, T> unionField) throws IOException;
	}

	private final ExpressionRenderer mExpressionRenderer;

	public SwitchRenderingUtility(final ExpressionRenderer expressionRenderer) {
		mExpressionRenderer = expressionRenderer;
	}

	public <T> void writeUnionSwitch(final SwitchData<T> data) throws IOException {
		LineWriter writer = data.writer;
		writer.writeln("switch(", data.switchExpr, ") {");
		writer.increaseLevel();

		for (final Data<UnionField, T> fieldData : data.fields) {
			UnionField field = fieldData.data;
			writeCaseLabels(data, field);
			if (data.hasCasesInBlocks) {
				writer.writeln("{");
			}
			writer.increaseLevel();
			data.caseRenderer.writeCaseBody(data, fieldData);
			writer.decreaseLevel();
			if (data.hasCasesInBlocks) {
				writer.writeln("}");
			}
		}

		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeCaseLabels(SwitchData data, final UnionField unionField) throws IOException {
		for (final CaseLabel label : unionField.labels) {
			writeCaseLabel(data, label);
		}
	}

	private void writeCaseLabel(SwitchData data, final CaseLabel label) throws IOException {
		if (label instanceof ExpressionLabel) {
			writeExpressionBasedCaseLabel(data, ((ExpressionLabel) label).expression);
		} else {
			writeDefaultCaseLabel(data);
		}
	}

	private void writeExpressionBasedCaseLabel(SwitchData data, final ConstExp labelExpression) throws IOException {
		String expressionStr;
		if (data.enumBased) {
			expressionStr = getEnumBasedCaseExpression(labelExpression);
		} else {
			expressionStr = getExpressionBasedCaseExpression(labelExpression);
		}
		data.writer.writeln("case ", expressionStr, ":");
	}

	private String getEnumBasedCaseExpression(final ConstExp labelExpression) {
		final String expressionStr;
		ExpressionElement expressionElement = labelExpression.elements.get(0);
		if (expressionElement instanceof ScopedName) {
			expressionStr = renderEnumConstant((ScopedName) expressionElement);
		} else {
			throw new CorbabindingException(
					"Enum based union MUST use enum to declare its fields, " +
					"expected single enum constant but got '" +
					mExpressionRenderer.render(labelExpression) +
					"'."
			);
		}
		return expressionStr;
	}

	protected String renderEnumConstant(final ScopedName expressionElement) {
		final String expressionStr;
		expressionStr = expressionElement.getBaseName();
		return expressionStr;
	}

	private String getExpressionBasedCaseExpression(final ConstExp labelExpression) {
		return mExpressionRenderer.render(labelExpression);
	}

	private void writeDefaultCaseLabel(final SwitchData data) throws IOException {
		data.writer.writeln("default:");
	}
}
