package com.matejhrazdira.pojos.IncludedIdl;

public class IncludedStruct {

	public final String someMember;

	public IncludedStruct() {
		this.someMember = "";
	}

	public IncludedStruct(String someMember) {
		this.someMember = someMember;
	}

	public Builder builder() {
		return new Builder(someMember);
	}

	public static class Builder {

		private String someMember;

		public Builder() {
		}

		private Builder(String someMember) {
			this.someMember = someMember;
		}

		public Builder withSomeMember(String someMember) {
			this.someMember = someMember;
			return this;
		}

		public IncludedStruct build() {
			return new IncludedStruct(someMember);
		}
	}
}
