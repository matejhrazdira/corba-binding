package com.matejhrazdira.pojos.SimpleIdl;

public interface SimpleIdlInterface extends com.matejhrazdira.pojos.cblib.Disposable {

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

	public String someStringMethod() throws com.matejhrazdira.pojos.cblib.CorbaException;

	public long methodWithTypedefedValues(long timeArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException;

	public com.matejhrazdira.pojos.SimpleIdl.SimpleEnum someMethodWithArgs(String strArg, long ullArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException;

	public com.matejhrazdira.pojos.SimpleIdl.SimpleStruct someMethodWithArgsThatThrows(String strArg, long ullArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException, com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers;

	public java.util.List<Long> getTimeSequence() throws com.matejhrazdira.pojos.cblib.CorbaException;

	public Void throwsNestedException() throws com.matejhrazdira.pojos.cblib.CorbaException, com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException;

}
