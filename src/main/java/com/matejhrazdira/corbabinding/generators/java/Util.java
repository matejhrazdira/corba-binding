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

package com.matejhrazdira.corbabinding.generators.java;

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

import java.io.File;

public class Util {

	public static final String JAVA_EXT = ".java";

	public static File getFileForName(final File outputDir, final ScopedName name) {
		File result = outputDir;
		for (String pkg : name.getScope()) {
			result = new File(result, pkg);
		}
		result = new File(result, name.getBaseName() + JAVA_EXT);
		return result;
	}

	public static File prepareFileForName(final File outputDir, final ScopedName name) {
		File result = getFileForName(outputDir, name);
		result.getParentFile().mkdirs();
		return result;
	}
}
