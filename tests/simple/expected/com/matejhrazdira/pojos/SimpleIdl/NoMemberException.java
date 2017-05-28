package com.matejhrazdira.pojos.SimpleIdl;

public class NoMemberException extends Exception {

	public NoMemberException() {
	}

	public Builder builder() {
		return new Builder();
	}

	public static class Builder {

		public Builder() {
		}

		public NoMemberException build() {
			return new NoMemberException();
		}
	}
}
