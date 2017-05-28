package com.matejhrazdira.pojos.SimpleIdl;

public class ExceptionWithMembers extends Exception {

	public final String someStringMember;

	public ExceptionWithMembers() {
		this.someStringMember = "";
	}

	public ExceptionWithMembers(String someStringMember) {
		this.someStringMember = someStringMember;
	}

	public Builder builder() {
		return new Builder(someStringMember);
	}

	public static class Builder {

		private String someStringMember;

		public Builder() {
		}

		private Builder(String someStringMember) {
			this.someStringMember = someStringMember;
		}

		public Builder withSomeStringMember(String someStringMember) {
			this.someStringMember = someStringMember;
			return this;
		}

		public ExceptionWithMembers build() {
			return new ExceptionWithMembers(someStringMember);
		}
	}
}
