package com.matejhrazdira.pojos.SimpleIdl;

public class DefaultUnionFromEnum {

	private static final com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _default_type_;

	static {
		com.matejhrazdira.pojos.SimpleIdl.SimpleEnum type = null;
		for (com.matejhrazdira.pojos.SimpleIdl.SimpleEnum value : com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.values()) {
			switch(value) {
				case EN_VAL_A:
				case EN_VAL_B:
					continue;
				default:
					break;
			}
			type = value;
			break;
		}
		_default_type_ = type;
	}

	public final com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_;
	public final String foo;
	public final String bar;

	public DefaultUnionFromEnum() {
		this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.values()[0];
		this.foo = "";
		this.bar = "";
	}

	public DefaultUnionFromEnum(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_, String foo, String bar) {
		this._type_ = _type_;
		this.foo = foo;
		this.bar = bar;
	}

	public Builder builder() {
		return new Builder(_type_, foo, bar);
	}

	public static class Builder {

		private com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_;
		private String foo;
		private String bar;

		public Builder() {
		}

		private Builder(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_, String foo, String bar) {
			this._type_ = _type_;
			this.foo = foo;
			this.bar = bar;
		}

		public Builder with_type_(com.matejhrazdira.pojos.SimpleIdl.SimpleEnum _type_) {
			this._type_ = _type_;
			return this;
		}

		public Builder withFoo(String foo) {
			reset();
			this.foo = foo;
			this._type_ = com.matejhrazdira.pojos.SimpleIdl.SimpleEnum.EN_VAL_A;
			return this;
		}

		public Builder withBar(String bar) {
			reset();
			this.bar = bar;
			this._type_ = _default_type_;
			return this;
		}

		public DefaultUnionFromEnum build() {
			return new DefaultUnionFromEnum(_type_, foo, bar);
		}

		private void reset() {
			if (_type_ == null) {
				return;
			}
			switch(_type_) {
				case EN_VAL_A:
				case EN_VAL_B:
					foo = null;
					break;
				default:
					bar = null;
					break;
			}
		}
	}
}
