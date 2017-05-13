package com.matejhrazdira.pojos.SimpleIdl;

public class SimpleStruct {

	public final String stringMember;
	public final int intMember;
	public final long typedefedMember;
	public final long uintMember;
	public final long longMember;
	public final long ulongMember;
	public final com.matejhrazdira.pojos.IncludedIdl.IncludedStruct includedMember;
	public final com.matejhrazdira.pojos.IncludedIdl.IncludedStruct fullyScopedIncludedMember;
	public final String duplicate1;
	public final String duplicate2;

	public SimpleStruct() {
		this.stringMember = "";
		this.intMember = 0;
		this.typedefedMember = 0;
		this.uintMember = 0;
		this.longMember = 0;
		this.ulongMember = 0;
		this.includedMember = new com.matejhrazdira.pojos.IncludedIdl.IncludedStruct();
		this.fullyScopedIncludedMember = new com.matejhrazdira.pojos.IncludedIdl.IncludedStruct();
		this.duplicate1 = "";
		this.duplicate2 = "";
	}

	public SimpleStruct(String stringMember, int intMember, long typedefedMember, long uintMember, long longMember, long ulongMember, com.matejhrazdira.pojos.IncludedIdl.IncludedStruct includedMember, com.matejhrazdira.pojos.IncludedIdl.IncludedStruct fullyScopedIncludedMember, String duplicate1, String duplicate2) {
		this.stringMember = stringMember;
		this.intMember = intMember;
		this.typedefedMember = typedefedMember;
		this.uintMember = uintMember;
		this.longMember = longMember;
		this.ulongMember = ulongMember;
		this.includedMember = includedMember;
		this.fullyScopedIncludedMember = fullyScopedIncludedMember;
		this.duplicate1 = duplicate1;
		this.duplicate2 = duplicate2;
	}

	public Builder builder() {
		return new Builder(stringMember, intMember, typedefedMember, uintMember, longMember, ulongMember, includedMember, fullyScopedIncludedMember, duplicate1, duplicate2);
	}

	public static class Builder {

		private String stringMember;
		private int intMember;
		private long typedefedMember;
		private long uintMember;
		private long longMember;
		private long ulongMember;
		private com.matejhrazdira.pojos.IncludedIdl.IncludedStruct includedMember;
		private com.matejhrazdira.pojos.IncludedIdl.IncludedStruct fullyScopedIncludedMember;
		private String duplicate1;
		private String duplicate2;

		public Builder() {
		}

		private Builder(String stringMember, int intMember, long typedefedMember, long uintMember, long longMember, long ulongMember, com.matejhrazdira.pojos.IncludedIdl.IncludedStruct includedMember, com.matejhrazdira.pojos.IncludedIdl.IncludedStruct fullyScopedIncludedMember, String duplicate1, String duplicate2) {
			this.stringMember = stringMember;
			this.intMember = intMember;
			this.typedefedMember = typedefedMember;
			this.uintMember = uintMember;
			this.longMember = longMember;
			this.ulongMember = ulongMember;
			this.includedMember = includedMember;
			this.fullyScopedIncludedMember = fullyScopedIncludedMember;
			this.duplicate1 = duplicate1;
			this.duplicate2 = duplicate2;
		}

		public Builder withStringMember(String stringMember) {
			this.stringMember = stringMember;
			return this;
		}

		public Builder withIntMember(int intMember) {
			this.intMember = intMember;
			return this;
		}

		public Builder withTypedefedMember(long typedefedMember) {
			this.typedefedMember = typedefedMember;
			return this;
		}

		public Builder withUintMember(long uintMember) {
			this.uintMember = uintMember;
			return this;
		}

		public Builder withLongMember(long longMember) {
			this.longMember = longMember;
			return this;
		}

		public Builder withUlongMember(long ulongMember) {
			this.ulongMember = ulongMember;
			return this;
		}

		public Builder withIncludedMember(com.matejhrazdira.pojos.IncludedIdl.IncludedStruct includedMember) {
			this.includedMember = includedMember;
			return this;
		}

		public Builder withFullyScopedIncludedMember(com.matejhrazdira.pojos.IncludedIdl.IncludedStruct fullyScopedIncludedMember) {
			this.fullyScopedIncludedMember = fullyScopedIncludedMember;
			return this;
		}

		public Builder withDuplicate1(String duplicate1) {
			this.duplicate1 = duplicate1;
			return this;
		}

		public Builder withDuplicate2(String duplicate2) {
			this.duplicate2 = duplicate2;
			return this;
		}

		public SimpleStruct build() {
			return new SimpleStruct(stringMember, intMember, typedefedMember, uintMember, longMember, ulongMember, includedMember, fullyScopedIncludedMember, duplicate1, duplicate2);
		}
	}
}
