package com.matejhrazdira.pojos.cblib;

public class CorbaException extends Exception {

	public final String name;
	public final String info;
	public final String repositoryId;

	public CorbaException(final String name, final String info, final String repositoryId) {
		super(info);
		this.name = name;
		this.info = info;
		this.repositoryId = repositoryId;
	}
}
