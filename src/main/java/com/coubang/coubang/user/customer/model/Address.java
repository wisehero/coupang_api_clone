package com.coubang.coubang.user.customer.model;

import javax.persistence.Embeddable;

import lombok.Getter;

@Getter
@Embeddable
public class Address {

	private String city;
	private String gu;
	private String dong;

	protected Address() {
	}

	public Address(String city, String gu, String dong) {
		this.city = city;
		this.gu = gu;
		this.dong = dong;
	}
}
