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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.matejhrazdira.corbabinding.Params.ParamsBuilder;
import com.matejhrazdira.corbabinding.gen.IdlParser;
import com.matejhrazdira.corbabinding.gen.IdlParser.OutputListener;
import com.matejhrazdira.corbabinding.gen.ParseException;
import com.matejhrazdira.corbabinding.generators.cppcorba.CppCorbaGenerator;
import com.matejhrazdira.corbabinding.generators.cppcorba.CppCorbaGeneratorBuilder;
import com.matejhrazdira.corbabinding.generators.java.PojoGenerator;
import com.matejhrazdira.corbabinding.idl.Specification;
import com.matejhrazdira.corbabinding.model.Model;
import com.matejhrazdira.corbabinding.util.NoOpOutputListener;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		String resourceName = "version.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
			props.load(resourceStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String versionStr = props.getProperty("version");
		String timestampStr = new Date(Long.parseLong(props.getProperty("timestamp", "0"))).toString();
		System.out.println("This is com.matejhrazdira.corbabinding idl parser & code generator v" + versionStr + " from " + timestampStr + "!");
		System.out.println();

		if (args.length != 1) {
			System.out.println("This program expects single argument - path to configuration file.");
			return;
		}

		String configFileName = args[0];

		MyOutputListener outputListener = new MyOutputListener();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try (FileInputStream is = new FileInputStream(configFileName)) {
			Params params = gson.fromJson(new InputStreamReader(is), Params.class);

			System.out.println("Using config:");
			System.out.println(gson.toJson(params));
			System.out.println();


			List<Specification> specifications = new ArrayList<>(params.inputFiles.size());
			for (final String idlFile : params.inputFiles) {
				try (FileInputStream idlStream = new FileInputStream(idlFile)) {
					IdlParser idlParser = new IdlParser(idlStream);
					idlParser.setOutputListener(outputListener);
					specifications.add(idlParser.specification(idlFile));
				}
			}

			Model model = new Model(specifications);

			PojoGenerator pojoGenerator = new PojoGenerator(new File(params.javaOutputDir), params.javaPackagePrefix, model, outputListener);

			pojoGenerator.generatePojos();

			CppCorbaGenerator cppCorbaGenerator = new CppCorbaGeneratorBuilder()
					.withOutput(new File(params.cppCorbaOutputDir))
					.withSymbolResolver(model.getResolver()) // TODO: either remove model from generate or set resolver there...
					.withTaoIdlIncludePrefix(params.cppCorbaTaoGeneratedPath)
					.withCorbaEncoding(params.cppCorbaEncoding)
					.withEnumProjectionProvider(pojoGenerator.getEnumProjection())
					.withStructProjectionProvider(pojoGenerator.getStructProjection())
					.withUnionProjectionProvider(pojoGenerator.getUnionProjection())
					.withJavaTemplateProjection(pojoGenerator.getTemplateProjection())
					.withInterfaceProjectionProvider(pojoGenerator.getInterfaceProjection())
					.withOutputListener(new NoOpOutputListener())
					.createCppCorbaGenerator();
			cppCorbaGenerator.generate(model);

			System.out.println();
			System.out.println("All files generated successfully.");

		} catch (IOException | JsonIOException | JsonSyntaxException | ParseException  | CorbabindingException e) {
			outputListener.onError(e.getMessage());
			System.out.println();
			System.out.println("Execution failed because of: ");
			e.printStackTrace(System.out);
		}
	}

	private static class MyOutputListener implements OutputListener, com.matejhrazdira.corbabinding.util.OutputListener {

		@Override
		public void onInfo(final String info) {
			System.out.println("I: " + info);
		}

		@Override
		public void onWarning(final String warning) {
			System.out.println("W: " + warning);
		}

		@Override
		public void onError(final String error) {
			System.out.println("E: " + error);
		}
	}
}
