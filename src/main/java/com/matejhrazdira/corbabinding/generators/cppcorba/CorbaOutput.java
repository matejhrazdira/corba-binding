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
	public final LineWriter jniImpHeader;
	public final LineWriter jniImplImpl;
	public final LineWriter libHeader;
	public final LineWriter libImpl;
	public final LineWriter jniImplPrivateHeader;
	public final LineWriter jniImplPrivateImpl;

	private CorbaOutput(final JniCacheHeaderWriter jniCacheHeader, final LineWriter jniCacheImpl, final LineWriter conversionHeader, final LineWriter conversionImpl, final LineWriter jniImpHeader, final LineWriter jniImplImpl, final LineWriter libHeader, final LineWriter libImpl, final LineWriter jniImplPrivateHeader, final LineWriter jniImplPrivateImpl) {
		this.jniCacheHeader = jniCacheHeader;
		this.jniCacheImpl = jniCacheImpl;
		this.conversionHeader = conversionHeader;
		this.conversionImpl = conversionImpl;
		this.jniImpHeader = jniImpHeader;
		this.jniImplImpl = jniImplImpl;
		this.libHeader = libHeader;
		this.libImpl = libImpl;
		this.jniImplPrivateHeader = jniImplPrivateHeader;
		this.jniImplPrivateImpl = jniImplPrivateImpl;
	}

	public void close() {
		jniCacheHeader.getWriter().close();
		jniCacheImpl.close();
		conversionHeader.close();
		conversionImpl.close();
		jniImpHeader.close();
		jniImplImpl.close();
		libHeader.close();
		libImpl.close();
		jniImplPrivateHeader.close();
		jniImplPrivateImpl.close();
	}

	public static class Builder {
		private JniCacheHeaderWriter mJniCacheHeader;
		private LineWriter mJniCacheImpl;
		private LineWriter mConversionHeader;
		private LineWriter mConversionImpl;
		private LineWriter mJniImpHeader;
		private LineWriter mJniImplImpl;
		private LineWriter mLibHeader;
		private LineWriter mLibImpl;
		private LineWriter mJniImplPrivateHeader;
		private LineWriter mJniImplPrivateImpl;

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

		public Builder withJniImplHeader(final LineWriter jniImpHeader) {
			mJniImpHeader = jniImpHeader;
			return this;
		}

		public Builder withJniImplImpl(final LineWriter jniImplImpl) {
			mJniImplImpl = jniImplImpl;
			return this;
		}

		public Builder withJniImpHeader(final LineWriter jniImpHeader) {
			mJniImpHeader = jniImpHeader;
			return this;
		}

		public Builder withLibHeader(final LineWriter libHeader) {
			mLibHeader = libHeader;
			return this;
		}

		public Builder withLibImpl(final LineWriter libImpl) {
			mLibImpl = libImpl;
			return this;
		}

		public Builder withJniImplPrivateHeader(final LineWriter jniImplPrivateHeader) {
			mJniImplPrivateHeader = jniImplPrivateHeader;
			return this;
		}

		public Builder withJniImplPrivateImpl(final LineWriter jniImplPrivateImpl) {
			mJniImplPrivateImpl = jniImplPrivateImpl;
			return this;
		}

		public CorbaOutput createCorbaOutput() {
			return new CorbaOutput(mJniCacheHeader, mJniCacheImpl, mConversionHeader, mConversionImpl, mJniImpHeader, mJniImplImpl, mLibHeader, mLibImpl, mJniImplPrivateHeader, mJniImplPrivateImpl);
		}
	}
}
