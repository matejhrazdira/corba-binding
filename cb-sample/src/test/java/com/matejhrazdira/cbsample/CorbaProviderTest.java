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

package com.matejhrazdira.cbsample;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleObject;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleServer;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleServer.NestedException;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleStruct;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleUnion;
import com.matejhrazdira.cbsample.generated.SimpleIdl.StructWithRealArrays;
import com.matejhrazdira.cbsample.generated.cblib.CorbaException;
import com.matejhrazdira.cbsample.generated.cblib.CorbaProvider;
import com.matejhrazdira.cbsample.generated.cblib.EventConsumer;
import com.matejhrazdira.cbsample.generated.cblib.EventProducer;
import com.matejhrazdira.cbsample.generated.cblib.EventService;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CorbaProviderTest {

	static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void nestedExceptions() throws Exception {
		try {
			try {
				System.out.println("try entered");
				throw new Exception("innerException");
//				System.out.println("after throw");
			} finally {
				System.out.println("finally executed");
				throw new Exception("exception from finally");
			}
		} catch (Exception e) {
			System.out.println("Caught: " + e.getMessage());
		}
	}

	static {
		File libPath = new File("libjnilibs.so");
		System.load(libPath.getAbsolutePath());
	}

	@Test(expected = CorbaException.class)
	public void initializeFail() throws CorbaException {
		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBinvalidArgument"
				}
		);
	}

	@Test
	public void initializeSuccess() throws CorbaException {
		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBDottedDecimalAddresses", "1",
						"-ORBInitRef", "NameService=corbaloc:iiop::2809/NameService",
				}
		);
		provider._dispose_();
	}

	@Test
	public void resolveSimpleInterface() throws CorbaException {
		try (CorbaContext ctx = new CorbaContext()) {
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			assertNotNull(simpleServer);
			String string = simpleServer.getString();
			System.out.println("Resolved server says: " + string);
			simpleServer._dispose_();
		}
	}

	@AfterClass
	public static void terminateServer() throws CorbaException {
		try (CorbaContext ctx = new CorbaContext()) {
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			assertNotNull(simpleServer);
			String string = simpleServer.getString();
			System.out.println("Resolved server says: " + string);
			simpleServer.exit();
			simpleServer._dispose_();
		}
	}

	@Test
	public void getAndDestroyObject() throws CorbaException {
		try (CorbaContext ctx = new CorbaContext()) {
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			assertNotNull(simpleServer);
			SimpleObject simpleObject = simpleServer.getSimpleObject("foo-bar");
			assertNotNull(simpleObject);
			String name = simpleObject.getString();
			assertEquals("foo-bar", name);
			simpleObject.destroy();
			simpleObject._dispose_();
			simpleServer._dispose_();
		}
	}

	@Test
	public void getSomeArrays() throws CorbaException {
		try (CorbaContext ctx = new CorbaContext()) {
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			assertNotNull(simpleServer);
			StructWithRealArrays arg = new StructWithRealArrays().builder().withStringMember("foo").build();
			arg.longArr[1] = 123;
			arg.structArr[1] = new SimpleStruct("foo bar baz", 567);
			StructWithRealArrays structWithArray = simpleServer.getStructWithArray(arg);
			System.out.println("received: " + GSON.toJson(structWithArray));
			assertArrayEquals(new int[] {0, 2, 4, 6, 8}, structWithArray.longArr);
			assertEquals("Hello from array!", structWithArray.structArr[1].stringMember);
			simpleServer._dispose_();
		}
	}

	@Test
	public void catchNestedException() throws CorbaException {
		try (CorbaContext ctx = new CorbaContext()) {
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			assertNotNull(simpleServer);
			NestedException exception = null;
			try {
				simpleServer.throwNestedException();
			} catch (NestedException e) {
				System.out.println("Caught exception " + e.getClass().getSimpleName() + ": " + e.cause);
				exception = e;
			}
			assertNotNull(exception);
			simpleServer._dispose_();
		}

	}

	@Test
	public void listenForSomeEvents() throws CorbaException, InterruptedException {
		try (CorbaContext ctx = new CorbaContext()) {
			EventConsumer<SimpleUnion> consumer1 = getEventConsumer(ctx.eventService1, 1);
			EventConsumer<SimpleUnion> consumer2 = getEventConsumer(ctx.eventService2, 1);
			EventConsumer<SimpleUnion> consumer3 = getEventConsumer(ctx.eventService1, 1);
			SimpleServer simpleServer = ctx.corbaProvider.resolve(SimpleServer.class, getServerStr("SimpleServer"));
			simpleServer.getString();
			EventConsumer<SimpleUnion> consumer4 = getEventConsumer(ctx.eventService2, 1);
			EventConsumer<SimpleUnion> consumer5 = getEventConsumer(ctx.eventService1, 1);
			EventConsumer<SimpleUnion> consumer6 = getEventConsumer(ctx.eventService2, 1);
			EventConsumer<SimpleUnion> consumer7 = getEventConsumer(ctx.eventService1, 1);
			EventConsumer<SimpleUnion> consumer8 = getEventConsumer(ctx.eventService2, 1);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			consumer1._dispose_();
			consumer2._dispose_();
			consumer3._dispose_();
			simpleServer._dispose_();
			consumer4._dispose_();
			consumer5._dispose_();
			consumer6._dispose_();
			consumer7._dispose_();
			consumer8._dispose_();
		}
	}

	private EventConsumer<SimpleUnion> getEventConsumer(final EventService eventService, final int subscription) throws CorbaException {
		return new EventConsumer<SimpleUnion>(eventService, subscription, SimpleUnion.class) {

			@Override
			public void onEvent(final SimpleUnion event) {
				System.out.println("got event: " + GSON.toJson(event));
			}
		};
	}

	@Test
	public void listenAndPushEvents() throws CorbaException, InterruptedException {
		try (CorbaContext ctx = new CorbaContext()) {
			EventConsumer<SimpleUnion> consumer1 = getEventConsumer(ctx.eventService1, 1);
			EventProducer<SimpleUnion> producer = new EventProducer<>(ctx.eventService1, 1, 1, SimpleUnion.class);
			SimpleUnion event = new SimpleUnion().builder()
					.withStrInUnion("Hello world from event")
					.build();
			System.out.println("pushing " + GSON.toJson(event));
			for (int i = 0; i < 10; i++) {
				producer.pushEvent(event);
				System.out.println("pushed " + (i + 1) + "x");
				Thread.sleep(50);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			consumer1._dispose_();
			producer._dispose_();
		}
	}

	private static class CorbaContext implements AutoCloseable {
		public final CorbaProvider corbaProvider;
		public final EventService eventService1;
		public final EventService eventService2;

		public CorbaContext() throws CorbaException {
			try {
				corbaProvider = new CorbaProvider(
						new String[] {
								"-ORBDottedDecimalAddresses", "1",
								"-ORBInitRef", "NameService=corbaloc:iiop::2809/NameService",
						}
				);
				eventService1 = new EventService(corbaProvider, getServerStr("EventService1"));
				eventService2 = new EventService(corbaProvider, getServerStr("EventService2"));

				eventService1.connect();
				eventService2.connect();
			} catch (CorbaException e) {
				close();
				throw e;
			}
		}


		@Override
		public void close() {
			if (eventService1 != null) {
				try {
					eventService1._dispose_();
				} catch (CorbaException e) {
					e.printStackTrace();
				}
			}
			if (eventService2 != null) {
				try {
					eventService2._dispose_();
				} catch (CorbaException e) {
					e.printStackTrace();
				}
			}
			if (corbaProvider != null) {
				try {
					corbaProvider._dispose_();
				} catch (CorbaException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String getServerStr(String name) {
		String address = "";
		String port = "2809";
		return "corbaname::" + address + ":" + port + "#" + name;
	}
}