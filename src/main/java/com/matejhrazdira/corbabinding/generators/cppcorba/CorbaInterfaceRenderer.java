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

import com.matejhrazdira.corbabinding.generators.java.InterfaceRenderer;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjection;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaInterfaceProjectionProvider;
import com.matejhrazdira.corbabinding.generators.java.projection.JavaProjection;
import com.matejhrazdira.corbabinding.generators.util.LineWriter;
import com.matejhrazdira.corbabinding.idl.Symbol;
import com.matejhrazdira.corbabinding.idl.SymbolResolver;
import com.matejhrazdira.corbabinding.idl.definitions.EnumType;
import com.matejhrazdira.corbabinding.idl.expressions.ScopedName;
import com.matejhrazdira.corbabinding.idl.interfaces.Declaration;
import com.matejhrazdira.corbabinding.idl.interfaces.Operation;
import com.matejhrazdira.corbabinding.idl.interfaces.Param;
import com.matejhrazdira.corbabinding.idl.types.PrimitiveType;
import com.matejhrazdira.corbabinding.idl.types.StringType;
import com.matejhrazdira.corbabinding.idl.types.Type;
import com.matejhrazdira.corbabinding.idl.types.VoidType;
import com.matejhrazdira.corbabinding.util.OutputListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CorbaInterfaceRenderer extends AbsCorbaWithMembersRenderer {

	public static final String CORBA_IMPL_VAR = "_c_this_";
	public static final String CORBA_VAR_SFX = "_var ";
	public static final String CORBA_PTR_SFX = "_ptr";
	public static final String CORBA_PTR_CALL = ".ptr()";
	public static final String CORBA_RETN_CALL = "._retn()";
	private final JniImplSignatureRenderer mJniImplSignatureRenderer = new JniImplSignatureRenderer();
	private final CorbaTypeRenderer mCorbaTypeRenderer = new CorbaTypeRenderer() {
		@Override
		protected String render(final StringType string) {
			return "::CORBA::String_var";
		}
	};
	private final SymbolResolver mResolver;

	public CorbaInterfaceRenderer(final SymbolResolver resolver, final JavaInterfaceProjectionProvider projectionProvider, final CorbaOutput output, final OutputListener outputListener) {
		super(projectionProvider, outputListener, output);
		mResolver = resolver;
	}

	@Override
	protected void writeConversionToJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		String corbaType = mCorbaScopedRenderer.render(projection.symbol.name);
		writer.write(
				"template<> jobject ",
				JniConfig.CONVERSION_FUNCTION, "<", corbaType, ">",
				"(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", ", corbaType, " * ", JniConfig.CONVERSION_IN_ARG, ")"
		);
	}

	@Override
	protected void renderConversionToJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		writer.writeln(
				"return ",
				JniConfig.CONVERSION_IN_ARG, " ? ",
				jniCall(
						"NewObject",
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, JniConfig.JNI_CACHE_CLASS)),
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, JniConfig.JNI_CACHE_CTOR)),
						"(" + mJniJavaTypeRenderer.render(InterfaceRenderer.NATIVE_ADDRESS_TYPE) + ") " + JniConfig.CONVERSION_IN_ARG
				),
				" : nullptr",
				";"
		);
	}

	@Override
	protected void writeConversionFromJavaDeclaration(final LineWriter writer, final JavaProjection projection) throws IOException {
		String corbaType = mCorbaScopedRenderer.render(projection.symbol.name);
		writer.write(
				"template<> ", corbaType, " * ",
				JniConfig.CONVERSION_FUNCTION, "<", corbaType, ">",
				"(JNIEnv * ", JniConfig.ARG_JNI_ENV, ", const jobject ", JniConfig.CONVERSION_IN_ARG, ")"
		);
	}

	@Override
	protected void renderConversionFromJava(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.conversionImpl;
		String corbaType = mCorbaScopedRenderer.render(projection.symbol.name);
		writer.writeln(
				"return ",
				JniConfig.CONVERSION_IN_ARG, " ? ",
				"(", corbaType, " * ", ") ",
				jniCall(
						mJniFieldAccessRenderer.getMethod(InterfaceRenderer.NATIVE_ADDRESS_TYPE),
						JniConfig.CONVERSION_IN_ARG,
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, InterfaceRenderer.NATIVE_ADDRESS))
				),
				" : nullptr",
				";"
		);
	}

	@Override
	protected void renderJniImplHeader(final JavaProjection javaProjection) throws IOException {
		LineWriter writer = mOutput.jniImpHeader;
		renderJniImpl(writer, (JavaInterfaceProjection) javaProjection, this::renderJniImplHeaderBody);
		writer.writeln();
	}

	private void renderJniImplHeaderBody(LineWriter writer, JavaInterfaceProjection projection, Operation operation) throws IOException {
		writer.write(";").endl();
	}

	private void renderJniImpl(final LineWriter writer, final JavaInterfaceProjection projection, final JniImplBodyRenderer bodyRenderer) throws IOException {
		List<Operation> operations = new ArrayList<>();
		operations.add(projection.destructor);
		operations.addAll(projection.interfaceOperations);
		for (final Operation operation : operations) {
			mJniImplSignatureRenderer.render(writer, projection.name, operation);
			bodyRenderer.renderBody(writer, projection, operation);
		}
	}

	@Override
	protected void renderTypeCacheEntries(final JavaProjection projection) throws IOException {
		LineWriter writer = mOutput.jniImplPrivateImpl;
		writer.writeln(
				JniConfig.TYPE_CACHE_INTERFACE_TABLE,
				"[\"", mJavaScopedRenderer.render(projection.name) , "\"]",
				" = ",
				JniConfig.TYPE_CACHE_CONVERT_OBJ, "<", mCorbaScopedRenderer.render(projection.symbol.name), ">",
				";"
		);
	}

	@Override
	protected void renderJniImplBody(final JavaProjection javaProjection) throws IOException {
		LineWriter writer = mOutput.jniImplImpl;
		renderJniImpl(writer, (JavaInterfaceProjection) javaProjection, this::renderJniImplImplBody);
	}

	private void renderJniImplImplBody(final LineWriter writer, final JavaInterfaceProjection projection, final Operation operation) throws IOException {
		writer.write(" {").endl();
		writer.increaseLevel();
		if (operation == projection.destructor) {
			writeDestructorBody(writer, projection);
		} else {
			writerRemoteOperationBody(writer, projection, operation);
		}
		writer.decreaseLevel();
		writer.writeln("}");
		writer.writeln();
	}

	private void writeDestructorBody(final LineWriter writer, final JavaInterfaceProjection projection) throws IOException {
		String corbaName = mCorbaScopedRenderer.render(projection.symbol.name);
		writer.writeln(
				corbaName, CORBA_VAR_SFX, "tmp",
				" = ",
				JniConfig.CONVERSION_FUNCTION, "<", corbaName, ">",
				"(", JniConfig.ARG_JNI_ENV, ", ", JniImplSignatureRenderer.JNI_IMPL_THIS_ARG, ");"
		);
		writer.writeln(
				jniCall(
						mJniFieldAccessRenderer.setMethod(InterfaceRenderer.NATIVE_ADDRESS_TYPE),
						JniImplSignatureRenderer.JNI_IMPL_THIS_ARG,
						mJniCacheRenderer.renderGlobalAccess(ScopedName.nameInScope(projection.name, InterfaceRenderer.NATIVE_ADDRESS)),
						"0x0"
				),
				";"
		);
	}

	private void writerRemoteOperationBody(final LineWriter writer, final JavaInterfaceProjection projection, final Operation operation) throws IOException {
		writeNullCheck(writer, projection, operation);
		Operation nativeOperation = ((Declaration) projection.symbol.element).operations.stream()
				.filter(o -> o.name.equals(operation.name))
				.findFirst()
				.get();
		writer.writeln();
		writer.writeln("try {");
		writer.increaseLevel();
		writeRemoteOperationCall(writer, projection, operation, nativeOperation);
		writer.decreaseLevel();
		writer.write("}");
		writeCatchBlocks(writer, nativeOperation);
		writer.endl();
		writeEmptyReturnStatement(writer, operation);
	}

	private void writeRemoteOperationCall(final LineWriter writer, final JavaInterfaceProjection projection, final Operation operation, final Operation nativeOperation) throws IOException {
		List<String> cparams = new ArrayList<>();
		List<VarParam> varParams = new ArrayList<>();
		for (int i = 0; i < operation.params.size(); i++) {
			final Param param = operation.params.get(i);
			final Param nativeParam = nativeOperation.params.get(i);
			String cparam = "_c_" + param.name + "_";
			String nativeParamType = mCorbaTypeRenderer.render(nativeParam.type);
			boolean isPointerType = isPointerType(getType(param.type)) && param.direction == Param.Direction.OUT;
			final String nativeTypeSuffix = isPointerType ? CORBA_VAR_SFX : "";
			writer.writeln(nativeParamType, nativeTypeSuffix, " ", cparam, ";");
			switch (param.direction) {
				case IN:
					writer.writeln(JniConfig.CONVERSION_FUNCTION, "(", JniConfig.ARG_JNI_ENV, ", ", param.name, ", ", cparam, ");");
					break;
				case OUT:
					varParams.add(new VarParam(param.name, cparam, isPointerType));
					break;
				case IN_OUT:
					varParams.add(new VarParam(param.name, cparam, false));
					writer.writeln(JniConfig.CONVERSION_FUNCTION, "Var", "(", JniConfig.ARG_JNI_ENV, ", ", param.name, ", ", cparam, ");");
					break;
			}
			writer.writeln();
			cparams.add(cparam);
		}

		boolean hasReturnValue = !(operation.returnType instanceof VoidType);
		final ElementType resultType = getType(operation.returnType);
		boolean isResultPointer = isPointerType(resultType);
		if (hasReturnValue) {
			writer.write(mCorbaTypeRenderer.render(nativeOperation.returnType));
			if (isResultPointer) {
				writer.write(CORBA_VAR_SFX);
			}
			writer.write(" ", JniConfig.RESULT_VAR, " = ");
		}
		writer.write(
				CORBA_IMPL_VAR, "->", operation.name, "(", cparams.stream().collect(Collectors.joining(", ")), ")", ";"
		).endl();
		writer.writeln();

		for (final VarParam varParam : varParams) {
			writer.writeln(
					"setVar(",
					JniConfig.ARG_JNI_ENV, ", ",
					varParam.javaName, ", ",
					varParam.corbaName, varParam.corbaVarIsPointer ? CORBA_PTR_CALL : "",
					");"
			);
		}

		if (varParams.size() > 0) {
			writer.writeln();
		}

		if (hasReturnValue) {
			final String resultCallSfx;
			switch (resultType) {
				case POINTER_TYPE:
					resultCallSfx = CORBA_PTR_CALL;
					break;
				case INTERFACE_TYPE:
					resultCallSfx = CORBA_RETN_CALL;
					break;
				default:
					resultCallSfx = "";
					break;
			}
			writer.writeln(
					"return ", JniConfig.CONVERSION_FUNCTION, "(",
					JniConfig.ARG_JNI_ENV, ", ",
					JniConfig.RESULT_VAR, resultCallSfx,
					");"
			);
			writer.writeln();
		}
	}

	private boolean isPointerType(ElementType type) {
		return type == ElementType.POINTER_TYPE || type == ElementType.INTERFACE_TYPE;
	}

	private ElementType getType(final Type type) {
		if (type instanceof PrimitiveType || type instanceof StringType) {
			return ElementType.VALUE_TYPE;
		} else if (type instanceof ScopedName) {
			Symbol typeSymbol = mResolver.findSymbol((ScopedName) type);
			if (typeSymbol.element instanceof EnumType) {
				return ElementType.VALUE_TYPE;
			} else if (typeSymbol.element instanceof Declaration) {
				return ElementType.INTERFACE_TYPE;
			} else {
				return ElementType.POINTER_TYPE;
			}
		} else {
			return ElementType.POINTER_TYPE;
		}
	}

	private void writeNullCheck(final LineWriter writer, final JavaInterfaceProjection projection, final Operation operation) throws IOException {
		String corbaName = mCorbaScopedRenderer.render(projection.symbol.name);
		writer.writeln(
				corbaName, CORBA_PTR_SFX, " ", CORBA_IMPL_VAR,
				" = ",
				JniConfig.CONVERSION_FUNCTION, "<", corbaName, ">",
				"(", JniConfig.ARG_JNI_ENV, ", ", JniImplSignatureRenderer.JNI_IMPL_THIS_ARG, ");"
		);
		writer.writeln("if (!", CORBA_IMPL_VAR, ") {");
		writer.increaseLevel();
		writer.writeln(
				jniCall(
						"Throw",
						"(jthrowable) " + jniCall(
								"NewObject",
								mJniCacheRenderer.renderGlobalAccess(
										new ScopedName(
												Arrays.asList(
														JniConfig.JNI_CACHE_IMPL,
														JniConfig.JNI_CACHE_ITEM_ALREADY_DISPOSED_EXCEPTION,
														JniConfig.JNI_CACHE_CLASS
												),
												true
										)
								),
								mJniCacheRenderer.renderGlobalAccess(
										new ScopedName(
												Arrays.asList(
														JniConfig.JNI_CACHE_IMPL,
														JniConfig.JNI_CACHE_ITEM_ALREADY_DISPOSED_EXCEPTION,
														JniConfig.JNI_CACHE_CTOR
												),
												true
										)
								)
						)
				),
				";"
		);
		writeEmptyReturnStatement(writer, operation);
		writer.decreaseLevel();
		writer.writeln("}");
	}

	private void writeCatchBlocks(final LineWriter writer, final Operation nativeOperation) throws IOException {
		for (final ScopedName exception : nativeOperation.exceptions) {
			writeCatchBlock(writer, mCorbaScopedRenderer.render(exception));
		}
		writeCatchBlock(writer, "::CORBA::Exception");
	}

	private void writeCatchBlock(final LineWriter writer, final String exceptionName) throws IOException {
		writer.write(" catch (", "const ", exceptionName, " & e", ") {").endl();
		writer.increaseLevel();
		writer.writeln(
				jniCall(
						"Throw",
						"(jthrowable) " + JniConfig.CONVERSION_FUNCTION + "(" + JniConfig.ARG_JNI_ENV + ", " + "e" + ")"
						),
				";"
		);
		writer.decreaseLevel();
		writer.write("}");
	}

	private void writeEmptyReturnStatement(final LineWriter writer, final Operation operation) throws IOException {
		writer.write("return");
		if (!(operation.returnType instanceof VoidType)) {
			writer.write(" (", mJniJavaTypeRenderer.render(operation.returnType), ") 0x0");
		}
		writer.write(";").endl();
	}

	@FunctionalInterface
	private interface JniImplBodyRenderer {
		void renderBody(LineWriter writer, JavaInterfaceProjection projection, Operation operation) throws IOException;
	}

	private static class VarParam {
		public final String javaName;
		public final String corbaName;
		public final boolean corbaVarIsPointer;

		public VarParam(final String javaName, final String corbaName, final boolean corbaVarIsPointer) {
			this.javaName = javaName;
			this.corbaName = corbaName;
			this.corbaVarIsPointer = corbaVarIsPointer;
		}
	}

	private enum ElementType {
		VALUE_TYPE, POINTER_TYPE, INTERFACE_TYPE
	}
}
