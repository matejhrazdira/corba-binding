package com.matejhrazdira.pojos;

public class Var<T> {

	private T _value_;

	public Var(final T _value_) {
		this._value_ = _value_;
	}

	public Var() {
	}

	public T get() {
		return _value_;
	}

	public void set(final T _value_) {
		this._value_ = _value_;
	}
}
