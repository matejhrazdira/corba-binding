package com.matejhrazdira.pojos.SimpleIdl;

public interface InheritedInterface extends com.matejhrazdira.pojos.SimpleIdl.SimpleIdlInterface, com.matejhrazdira.pojos.cblib.Disposable {

	public String someAdditionalMethod() throws com.matejhrazdira.pojos.cblib.CorbaException;

}
