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
import com.matejhrazdira.cbsample.generated.CorbaException;
import com.matejhrazdira.cbsample.generated.CorbaProvider;
import com.matejhrazdira.cbsample.generated.EventConsumer;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleServer;
import com.matejhrazdira.cbsample.generated.SimpleIdl.SimpleUnion;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

public class CorbaProviderTest {

	static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	static {
		File libPath = new File("libjnilibs.so");
		System.load(libPath.getAbsolutePath());
	}

	@Test(expected = CorbaException.class)
	public void initializeFail() throws CorbaException {

		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBinvalidArgument"
				},
				"EventService"
		);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// ignore
		}
		provider.dispose();
	}

	@Test
	public void initializeSuccess() throws CorbaException {

		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBDottedDecimalAddresses", "1",
						"-ORBInitRef", "NameService=corbaloc:iiop::2809/NameService",
				},
				"EventService"
		);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// ignore
		}
		provider.dispose();
	}

	@Test
	public void resolveSimpleInterface() throws CorbaException {

		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBDottedDecimalAddresses", "1",
						"-ORBInitRef", "NameService=corbaloc:iiop::2809/NameService",
				},
				"EventService"
		);

		try {
			SimpleServer simpleServer = provider.resolve(SimpleServer.class, "SimpleServer");
			assertNotNull(simpleServer);
			String string = simpleServer.getString();
			System.out.println("Resolved server says: " + string);
			simpleServer._dispose_();
		} finally {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ignore
			}
			provider.dispose();
		}

	}

	@Test
	public void listenForSomeEvents() throws CorbaException {

		CorbaProvider provider = new CorbaProvider(
				new String[] {
						"-ORBDottedDecimalAddresses", "1",
						"-ORBInitRef", "NameService=corbaloc:iiop::2809/NameService",
				},
				"EventService"
		);

		try {
			EventConsumer<SimpleUnion> consumer = new EventConsumer<SimpleUnion>(provider, 1, SimpleUnion.class) {

				@Override
				public void onEvent(final SimpleUnion event) {
					System.out.println("got event: " + GSON.toJson(event));
				}
			};
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			consumer.dispose();
		} finally {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// ignore
			}
			provider.dispose();
		}
	}
}