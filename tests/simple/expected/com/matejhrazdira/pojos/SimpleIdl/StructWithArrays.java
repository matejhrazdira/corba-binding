package com.matejhrazdira.pojos.SimpleIdl;

public class StructWithArrays {

	public final java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefedArray;
	public final java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> directArray;
	public final java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefTypedefedArray;

	public StructWithArrays() {
		this.typedefedArray = java.util.Collections.unmodifiableList(new java.util.ArrayList<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct>());
		this.directArray = java.util.Collections.unmodifiableList(new java.util.ArrayList<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct>());
		this.typedefTypedefedArray = java.util.Collections.unmodifiableList(new java.util.ArrayList<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct>());
	}

	public StructWithArrays(java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefedArray, java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> directArray, java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefTypedefedArray) {
		this.typedefedArray = typedefedArray != null ? java.util.Collections.unmodifiableList(new java.util.ArrayList<>(typedefedArray)) : null;
		this.directArray = directArray != null ? java.util.Collections.unmodifiableList(new java.util.ArrayList<>(directArray)) : null;
		this.typedefTypedefedArray = typedefTypedefedArray != null ? java.util.Collections.unmodifiableList(new java.util.ArrayList<>(typedefTypedefedArray)) : null;
	}

	public Builder builder() {
		return new Builder(typedefedArray, directArray, typedefTypedefedArray);
	}

	public static class Builder {

		private java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefedArray;
		private java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> directArray;
		private java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefTypedefedArray;

		public Builder() {
		}

		private Builder(java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefedArray, java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> directArray, java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefTypedefedArray) {
			this.typedefedArray = typedefedArray != null ? new java.util.ArrayList<>(typedefedArray) : null;
			this.directArray = directArray != null ? new java.util.ArrayList<>(directArray) : null;
			this.typedefTypedefedArray = typedefTypedefedArray != null ? new java.util.ArrayList<>(typedefTypedefedArray) : null;
		}

		public Builder withTypedefedArray(java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefedArray) {
			this.typedefedArray = typedefedArray;
			return this;
		}

		public Builder withDirectArray(java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> directArray) {
			this.directArray = directArray;
			return this;
		}

		public Builder withTypedefTypedefedArray(java.util.List<com.matejhrazdira.pojos.SimpleIdl.SimpleStruct> typedefTypedefedArray) {
			this.typedefTypedefedArray = typedefTypedefedArray;
			return this;
		}

		public StructWithArrays build() {
			return new StructWithArrays(typedefedArray, directArray, typedefTypedefedArray);
		}
	}
}
