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

package com.matejhrazdira.corbabinding.generators.cppcorba;

import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.interfaces.Param;

import java.io.IOException;

public class JniImplSignatureRenderer {

	public static final String JNI_IMPL_THIS_ARG = "_this_";
	private final JniImplMethodNameRenderer mMethodNameRenderer = new JniImplMethodNameRenderer();
	private final JniJavaTypeRenderer mJniJavaTypeRenderer = new JniJavaTypeRenderer();

	public void render(LineWriter writer, ScopedName scope, Operation operation) throws IOException {
		final String returnType = mJniJavaTypeRenderer.render(operation.returnType);
		writer.write(
				"JNIEXPORT ", returnType, " JNICALL ",
				mMethodNameRenderer.render(ScopedName.nameInScope(scope, operation.name)),
				"(", "JNIEnv * ", JniConfig.ARG_JNI_ENV, ", ", "jobject", " ", JNI_IMPL_THIS_ARG
		);
		for (final Param param : operation.params) {
			writer.write(
					", ",
					param.direction == Param.Direction.IN ? mJniJavaTypeRenderer.render(param.type) : "jobject",
					" ",
					param.name
			);
		}
		writer.write(")");
	}
}
