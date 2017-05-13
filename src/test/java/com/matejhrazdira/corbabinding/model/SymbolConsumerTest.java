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

package com.matejhrazdira.corbabinding.model;

import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.definitions.*;
import com.matejhrazdira.corbabinding.idl.definitions.members.Declarator;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.ExpressionLabel;
import com.matejhrazdira.corbabinding.idl.definitions.unionmembers.UnionField;
import com.matejhrazdira.corbabinding.idl.expressions.ConstExp;
import com.matejhrazdira.corbabinding.idl.expressions.Literal;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SymbolConsumerTest {

	@Test
	public void consume() throws Exception {
		AtomicInteger count = new AtomicInteger(0);
		SymbolConsumer consumer = new SymbolConsumer() {

			@Override
			public void consume(final Module module, final ScopedName name) {
				assertNotNull(module);
				assertEquals(module.name, name.getQualifiedName());
				count.incrementAndGet();
			}

			@Override
			public void consume(final ConstDcl constant, final ScopedName name) {
				assertNotNull(constant);
				assertEquals(constant.name, name.getQualifiedName());
				count.incrementAndGet();
			}

			@Override
			public void consume(final StructType struct, final ScopedName name) {
				assertNotNull(struct);
				assertEquals(struct.name, name.getQualifiedName());
				count.incrementAndGet();
			}

			@Override
			public void consume(final UnionType union, final ScopedName name) {
				assertNotNull(union);
				assertEquals(union.name, name.getQualifiedName());
				count.incrementAndGet();
			}

			@Override
			public void consume(final TypeDeclarator typedef, final ScopedName name) {
				assertNotNull(typedef);
				assertEquals("typedef", name.getQualifiedName());
				count.incrementAndGet();
			}

			@Override
			public void consume(final ExceptDcl exception, final ScopedName name) {
				assertNotNull(exception);
				assertEquals(exception.name, name.getQualifiedName());
				count.incrementAndGet();
			}
		};
		List<Symbol> symbols = Arrays.asList(
				new Symbol(
						ScopedName.nameInScope(null, "module"),
						new Module("module", Collections.emptyList())
				),
				new Symbol(
						ScopedName.nameInScope(null, "constant"),
						new ConstDcl(
								"constant",
								new StringType(),
								new ConstExp(Arrays.asList(new Literal("foo")))
						)
				),
				new Symbol(
						ScopedName.nameInScope(null, "struct"),
						new StructType("struct", Collections.emptyList())
				),
				new Symbol(
						ScopedName.nameInScope(null, "union"),
						new UnionType(
								"union",
								new PrimitiveType(PrimitiveType.Type.SIGNED_LONG_INT),
								Arrays.asList(
										new UnionField(
												new StringType(),
												new Declarator("foo"),
												Arrays.asList(
														new ExpressionLabel(new ConstExp(Arrays.asList(new Literal("1"))))
												)
										)
								)
						)
				),
				new Symbol(
						ScopedName.nameInScope(null, "typedef"),
						new TypeDeclarator(
								new StringType(),
								Arrays.asList(new Declarator("typedef"))
						)
				),
				new Symbol(
						ScopedName.nameInScope(null, "exception"),
						new ExceptDcl("exception", Collections.emptyList()
						)
				)
		);
		consumer.consume(symbols);
		assertEquals(symbols.size(), count.get());
	}

}