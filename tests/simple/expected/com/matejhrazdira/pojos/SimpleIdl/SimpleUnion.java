package com.matejhrazdira.pojos.SimpleIdl;

public class SimpleUnion {

	public final com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_;
	public final String strInUnion;
	public final long ullInUnion;
	public final com.matejhrazdira.pojos.SimpleIdl.SimpleStruct simpleStructInUnion;

	public SimpleUnion() {
		this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.values()[0];
		this.strInUnion = "";
		this.ullInUnion = 0;
		this.simpleStructInUnion = new com.matejhrazdira.pojos.SimpleIdl.SimpleStruct();
	}

	public SimpleUnion(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_, String strInUnion, long ullInUnion, com.matejhrazdira.pojos.SimpleIdl.SimpleStruct simpleStructInUnion) {
		this._type_ = _type_;
		this.strInUnion = strInUnion;
		this.ullInUnion = ullInUnion;
		this.simpleStructInUnion = simpleStructInUnion;
	}

	public Builder builder() {
		return new Builder(_type_, strInUnion, ullInUnion, simpleStructInUnion);
	}

	public static class Builder {

		private com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_;
		private String strInUnion;
		private long ullInUnion;
		private com.matejhrazdira.pojos.SimpleIdl.SimpleStruct simpleStructInUnion;

		public Builder() {
		}

		private Builder(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_, String strInUnion, long ullInUnion, com.matejhrazdira.pojos.SimpleIdl.SimpleStruct simpleStructInUnion) {
			this._type_ = _type_;
			this.strInUnion = strInUnion;
			this.ullInUnion = ullInUnion;
			this.simpleStructInUnion = simpleStructInUnion;
		}

		public Builder with_type_(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_) {
			this._type_ = _type_;
			return this;
		}

		public Builder withStrInUnion(String strInUnion) {
			reset();
			this.strInUnion = strInUnion;
			this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.EN_VAL_A;
			return this;
		}

		public Builder withUllInUnion(long ullInUnion) {
			reset();
			this.ullInUnion = ullInUnion;
			this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.EN_VAL_B;
			return this;
		}

		public Builder withSimpleStructInUnion(com.matejhrazdira.pojos.SimpleIdl.SimpleStruct simpleStructInUnion) {
			reset();
			this.simpleStructInUnion = simpleStructInUnion;
			this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.EN_VAL_C;
			return this;
		}

		public SimpleUnion build() {
			return new SimpleUnion(_type_, strInUnion, ullInUnion, simpleStructInUnion);
		}

		private void reset() {
			if (_type_ == null) {
				return;
			}
			switch(_type_) {
				case EN_VAL_A:
					strInUnion = null;
					break;
				case EN_VAL_B:
					ullInUnion = 0;
					break;
				case EN_VAL_C:
					simpleStructInUnion = null;
					break;
			}
		}
	}
}
