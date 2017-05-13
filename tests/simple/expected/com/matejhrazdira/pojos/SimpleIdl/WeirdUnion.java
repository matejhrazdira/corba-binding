package com.matejhrazdira.pojos.SimpleIdl;

public class WeirdUnion {

	private static final int _default_type_;

	static {
		int type = 0;
		for (int value = Integer.MIN_VALUE; value < Integer.MAX_VALUE; value++) {
			switch(value) {
				case 1 + 3:
				case 1 + 2 * 5:
				case 2 + 6 * (1 - 3) * ( -5 / 2) - 5:
					continue;
				default:
					break;
			}
			type = value;
			break;
		}
		_default_type_ = type;
	}

	public final int _type_;
	public final String foo;
	public final String bar;

	public WeirdUnion() {
		this._type_ = 0;
		this.foo = "";
		this.bar = "";
	}

	public WeirdUnion(int _type_, String foo, String bar) {
		this._type_ = _type_;
		this.foo = foo;
		this.bar = bar;
	}

	public Builder builder() {
		return new Builder(_type_, foo, bar);
	}

	public static class Builder {

		private int _type_;
		private String foo;
		private String bar;

		public Builder() {
		}

		private Builder(int _type_, String foo, String bar) {
			this._type_ = _type_;
			this.foo = foo;
			this.bar = bar;
		}

		public Builder with_type_(int _type_) {
			this._type_ = _type_;
			return this;
		}

		public Builder withFoo(String foo) {
			reset();
			this.foo = foo;
			this._type_ = 1 + 3;
			return this;
		}

		public Builder withBar(String bar) {
			reset();
			this.bar = bar;
			this._type_ = _default_type_;
			return this;
		}

		public WeirdUnion build() {
			return new WeirdUnion(_type_, foo, bar);
		}

		private void reset() {
			switch(_type_) {
				case 1 + 3:
				case 1 + 2 * 5:
				case 2 + 6 * (1 - 3) * ( -5 / 2) - 5:
					foo = null;
					break;
				default:
					bar = null;
					break;
			}
		}
	}
}
