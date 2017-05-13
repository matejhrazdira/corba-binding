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

package com.matejhrazdira.corbabinding;

import java.util.List;

public class Params {

	public final List<String> inputFiles;
	public final String javaOutputDir;
	public final String javaPackagePrefix;
	public final String cppCorbaOutputDir;
	public final String cppCorbaTaoGeneratedPath;

	private Params(final List<String> inputFiles, final String javaOutputDir, final String javaPackagePrefix, String cppCorbaOutputDir, final String cppCorbaTaoGeneratedPath) {
		this.inputFiles = inputFiles;
		this.javaOutputDir = javaOutputDir;
		this.javaPackagePrefix = javaPackagePrefix;
		this.cppCorbaOutputDir = cppCorbaOutputDir;
		this.cppCorbaTaoGeneratedPath = cppCorbaTaoGeneratedPath;
	}

	public static class ParamsBuilder {
		private List<String> inputFiles;
		private String javaOutputDir;
		private String javaPackagePrefix;
		private String cppCorbaOutputDir;
		private String cppCorbaTaoGeneratedPath;

		public ParamsBuilder setInputFiles(final List<String> inputFiles) {
			this.inputFiles = inputFiles;
			return this;
		}

		public ParamsBuilder setJavaOutputDir(final String javaOutputDir) {
			this.javaOutputDir = javaOutputDir;
			return this;
		}

		public ParamsBuilder setJavaPackagePrefix(final String javaPackagePrefix) {
			this.javaPackagePrefix = javaPackagePrefix;
			return this;
		}

		public ParamsBuilder setCppCorbaOutputDir(final String cppCorbaOutputDir) {
			this.cppCorbaOutputDir = cppCorbaOutputDir;
			return this;
		}

		public ParamsBuilder setCppCorbaTaoGeneratedPath(final String cppCorbaTaoGeneratedPath) {
			this.cppCorbaTaoGeneratedPath = cppCorbaTaoGeneratedPath;
			return this;
		}

		public Params createParams() {
			return new Params(inputFiles, javaOutputDir, javaPackagePrefix, cppCorbaOutputDir, cppCorbaTaoGeneratedPath);
		}
	}
}
