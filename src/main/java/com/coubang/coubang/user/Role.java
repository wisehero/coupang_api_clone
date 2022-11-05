package com.coubang.coubang.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	CUSTOMER("ROLE_CUSTOMER", "고객"),
	SELLER("ROLE_SELLER", "판매자"),
	COUBANG("ROLE_COUBANG", "쿠방");

	private final String authority;
	private final String description;
}
