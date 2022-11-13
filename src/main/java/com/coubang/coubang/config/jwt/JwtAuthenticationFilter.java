package com.coubang.coubang.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.coubang.coubang.config.auth.PrincipalDetails;
import com.coubang.coubang.user.dto.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
			AuthenticationException {
		log.info("JwtAuthenticationFilter : 진입");

		// request에 있는 username과 password를 파싱해서 자바 Object로 받기
		ObjectMapper om = new ObjectMapper();
		RequestLogin requestLogin = null;
		try {
			requestLogin = om.readValue(request.getInputStream(), RequestLogin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("JwtAuthenticationFilter : {}", requestLogin != null ? requestLogin.getClass().getName() : null);

		// usernamepassword token 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						requestLogin.getEmail(),
						requestLogin.getPassword());

		log.info("JwtAuthenticationFilter : 토큰 생성 완료");

		Authentication authentication =
				authenticationManager.authenticate(authenticationToken);

		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		log.info("Authentication : {}", principalDetails.getUser().getEmail());
		return authentication;
	}

	// token 생성해서 response로 반환
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.withClaim("email", principalDetails.getUser().getEmail())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

	}
}
