package com.coubang.coubang.user.dto;

import javax.validation.constraints.NotBlank;

public class RequestLogin {
	@NotBlank(message = "이메일을 입력하세요")
	private final String email;
	@NotBlank(message = "비밀번호를 입력하세요.")
	private final String password;

	public RequestLogin(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
