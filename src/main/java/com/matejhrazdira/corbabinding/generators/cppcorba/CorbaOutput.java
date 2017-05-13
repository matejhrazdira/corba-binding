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
	public final LineWriter jniCacheImpl;
	public final LineWriter conversionHeader;
	public final LineWriter conversionImpl;

	CorbaOutput(final JniCacheHeaderWriter jniCacheHeader, final LineWriter jniCacheImpl, final LineWriter conversionHeader, final LineWriter conversionImpl) {
		this.jniCacheHeader = jniCacheHeader;
		this.jniCacheImpl = jniCacheImpl;
		this.conversionHeader = conversionHeader;
		this.conversionImpl = conversionImpl;
	}

	public void close() {
		jniCacheHeader.getWriter().close();
		jniCacheImpl.close();
		conversionHeader.close();
		conversionImpl.close();
	}

	public static class Builder {
		private JniCacheHeaderWriter mJniCacheHeader;
		private LineWriter mJniCacheImpl;
		private LineWriter mConversionHeader;
		private LineWriter mConversionImpl;

		public Builder withJniCacheHeader(final JniCacheHeaderWriter jniCacheHeader) {
			mJniCacheHeader = jniCacheHeader;
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

		public CorbaOutput build() {
			return new CorbaOutput(mJniCacheHeader, mJniCacheImpl, mConversionHeader, mConversionImpl);
		}
	}
}
