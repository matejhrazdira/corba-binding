package com.matejhrazdira.pojos.cblib.client.SimpleIdl;

public class SimpleIdlInterface implements com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface {

	private long _native_address_;

	public SimpleIdlInterface(long _native_address_) {
		this._native_address_ = _native_address_;
	}

	@Override
	public native Void _dispose_() throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native String someStringMethod() throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native long methodWithTypedefedValues(long timeArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native com.matejhrazdira.pojos.SimpleIdl.SimpleEnum someMethodWithArgs(String strArg, long ullArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native com.matejhrazdira.pojos.SimpleIdl.SimpleStruct someMethodWithArgsThatThrows(String strArg, long ullArg, com.matejhrazdira.pojos.cblib.Var<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> structArg) throws com.matejhrazdira.pojos.cblib.CorbaException, com.matejhrazdira.pojos.SimpleIdl.ExceptionWithMembers;

	@Override
	public native java.util.List<Long> getTimeSequence() throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native Void throwsNestedException() throws com.matejhrazdira.pojos.cblib.CorbaException, com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface.NestedException;

}
