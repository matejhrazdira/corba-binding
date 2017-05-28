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
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;

import java.util.List;

public class JavaInterfaceProjection extends JavaStructProjection {

	public final List<Operation> interfaceOperations;
	public final Operation destructor;

	public JavaInterfaceProjection(final Symbol symbol, final ScopedName name, final List<Member> members, final List<Operation> interfaceOperations, final Operation destructor) {
		super(symbol, name, members);
		this.interfaceOperations = interfaceOperations;
		this.destructor = destructor;
	}
}
