package com.matejhrazdira.pojos.cblib.client.SimpleIdl;

public class InheritedInterface extends com.matejhrazdira.pojos.cblib.client.SimpleIdl.SimpleIdlInterface implements com.matejhrazdira.pojos.SimpleIdl.InheritedInterface {

	private long _native_address_;
	private static final String _interface_name_ = "com.matejhrazdira.pojos.SimpleIdl.InheritedInterface";

	public InheritedInterface(long _native_address_) {
		super(_native_address_);
		this._native_address_ = _native_address_;
	}

	@Override
	public native Void _dispose_() throws com.matejhrazdira.pojos.cblib.CorbaException;

	@Override
	public native String someAdditionalMethod() throws com.matejhrazdira.pojos.cblib.CorbaException;

}
