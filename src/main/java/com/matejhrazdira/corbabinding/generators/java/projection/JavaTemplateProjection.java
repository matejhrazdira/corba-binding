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

import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;

public class JavaTemplateProjection {

	public final ScopedName varClass;
	public final ScopedName corbaExceptionClass;
	public final ScopedName alreadyDisposedExceptionClass;
	public final ScopedName corbaProviderClass;
	public final ScopedName eventServiceClass;
	public final ScopedName eventConsumerClass;
	public final ScopedName eventProducerClass;

	public JavaTemplateProjection(final ScopedName varClass, final ScopedName corbaExceptionClass, final ScopedName alreadyDisposedExceptionClass, final ScopedName corbaProviderClass, ScopedName eventServiceClass, final ScopedName eventConsumerClass, final ScopedName eventProducerClass) {
		this.varClass = varClass;
		this.corbaExceptionClass = corbaExceptionClass;
		this.alreadyDisposedExceptionClass = alreadyDisposedExceptionClass;
		this.corbaProviderClass = corbaProviderClass;
		this.eventServiceClass = eventServiceClass;
		this.eventConsumerClass = eventConsumerClass;
		this.eventProducerClass = eventProducerClass;
	}
}
