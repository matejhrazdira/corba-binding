package com.matejhrazdira.pojos.cblib;

public class AlreadyDisposedException extends CorbaException {

	public AlreadyDisposedException() {
		super("ALREADY_DISPOSED", "corba-binding exception: object is not (no longer) initialized", "");
	}
}
