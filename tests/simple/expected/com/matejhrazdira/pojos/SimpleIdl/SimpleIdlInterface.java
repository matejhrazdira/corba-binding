package com.matejhrazdira.pojos.SimpleIdl;

public class SimpleIdlInterface implements com.matejhrazdira.pojos.Disposable {

	public static final int NESTED_CONST = 123;

	public static class NestedException extends Exception {

		public NestedException() {
		}

		public Builder builder() {
			return new Builder();
		}

		public static class Builder {

			public Builder() {
			}

			public NestedException build() {
				return new NestedException();
			}
		}
	}

	private long _native_address_;

	public SimpleIdlInterface(long _native_address_) {
		this._native_address_ = _native_address_;
	}

	public native void _dispose_() throws com.matejhrazdira.pojos.CorbaException;

	public native String someStringMethod() throws com.matejhrazdira.pojos.CorbaException;

	public native long methodWithTypedefedValues(long timeArg, com.matejhrazdira.pojos.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.CorbaException;

	public native com.matejhrazdira.pojos.SimpleIdl.SimpleEnum someMethodWithArgs(String strArg, long ullArg, com.matejhrazdira.pojos.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.CorbaException;

	public native com.matejhrazdira.pojos.SimpleIdl.SimpleStruct someMethodWithArgsThatThrows(String strArg, long ullArg, com.matejhrazdira.pojos.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.CorbaException, com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers;

	public native java.util.List<Long> getTimeSequence() throws com.matejhrazdira.pojos.CorbaException;

	public native void throwsNestedException() throws com.matejhrazdira.pojos.CorbaException, com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException;

}
