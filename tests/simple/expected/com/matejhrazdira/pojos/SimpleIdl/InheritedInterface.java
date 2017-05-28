package com.matejhrazdira.pojos.SimpleIdl;

public class InheritedInterface extends com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface {

	private long _native_address_;

	public InheritedInterface(long _native_address_) {
		super(_native_address_);
		this._native_address_ = _native_address_;
	}

	public native void _dispose_() throws com.matejhrazdira.pojos.CorbaException;

	public native String someAdditionalMethod() throws com.matejhrazdira.pojos.CorbaException;

}
