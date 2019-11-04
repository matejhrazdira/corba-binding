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

public class CorbaOutput {
	public final JniCacheHeaderWriter jniCacheHeader;
	public final JniCacheHeaderWriter jniCacheHeaderClient;
	public final LineWriter jniCacheImpl;
	public final LineWriter conversionHeader;
	public final LineWriter conversionImpl;
	public final LineWriter jniImplHeader;
	public final LineWriter jniImplImpl;
	public final LineWriter typeCacheEntries;

	private CorbaOutput(final JniCacheHeaderWriter jniCacheHeader, final JniCacheHeaderWriter jniCacheHeaderClient, final LineWriter jniCacheImpl, final LineWriter conversionHeader, final LineWriter conversionImpl, final LineWriter jniImplHeader, final LineWriter jniImplImpl, final LineWriter typeCacheEntries) {
		this.jniCacheHeader = jniCacheHeader;
		this.jniCacheHeaderClient = jniCacheHeaderClient;
		this.jniCacheImpl = jniCacheImpl;
		this.conversionHeader = conversionHeader;
		this.conversionImpl = conversionImpl;
		this.jniImplHeader = jniImplHeader;
		this.jniImplImpl = jniImplImpl;
		this.typeCacheEntries = typeCacheEntries;
	}

	public void close() {
		jniCacheHeader.getWriter().close();
		jniCacheImpl.close();
		conversionHeader.close();
		conversionImpl.close();
		jniImplHeader.close();
		jniImplImpl.close();
		typeCacheEntries.close();
	}

	public static class Builder {
		private JniCacheHeaderWriter mJniCacheHeader;
		private LineWriter mJniCacheImpl;
		private LineWriter mConversionHeader;
		private LineWriter mConversionImpl;
		private LineWriter mJniImpHeader;
		private LineWriter mJniImplImpl;
		private LineWriter mTypeCacheEntries;
		private JniCacheHeaderWriter mJniCacheHeaderClient;
		private LineWriter mJniImplHeader;

		public Builder withJniCacheHeader(final JniCacheHeaderWriter jniCacheHeader) {
			mJniCacheHeader = jniCacheHeader;
			return this;
		}

		public Builder withJniCacheHeaderClient(final JniCacheHeaderWriter jniCacheHeaderClient) {
			mJniCacheHeaderClient = jniCacheHeaderClient;
			return this;
		}

		public Builder withJniCacheImpl(final LineWriter jniCacheImpl) {
			mJniCacheImpl = jniCacheImpl;
			return this;
		}

		public Builder withConversionHeader(final LineWriter conversionHeader) {
			mConversionHeader = conversionHeader;
			return this;
		}

		public Builder withConversionImpl(final LineWriter conversionImpl) {
			mConversionImpl = conversionImpl;
			return this;
		}

		public Builder withJniImplHeader(final LineWriter jniImpHeader) {
			mJniImpHeader = jniImpHeader;
			return this;
		}

		public Builder withJniImplImpl(final LineWriter jniImplImpl) {
			mJniImplImpl = jniImplImpl;
			return this;
		}

		public Builder withTypeCacheEntries(final LineWriter typeCacheEntries) {
			mTypeCacheEntries = typeCacheEntries;
			return this;
		}

		public CorbaOutput createCorbaOutput() {
			return new CorbaOutput(mJniCacheHeader, mJniCacheHeaderClient, mJniCacheImpl, mConversionHeader, mConversionImpl, mJniImpHeader, mJniImplImpl, mTypeCacheEntries);
		}
	}
}
