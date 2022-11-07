package com.coubang.coubang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.coubang.coubang.user.repository.UserRepository;

@Configuration
@EnableWebSecurity // 기본적인 웹 보안 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

	private final UserRepository userRepository;
	private final CorsConfig corsConfig;

	public SecurityConfig(UserRepository userRepository, CorsConfig corsConfig) {
		this.userRepository = userRepository;
		this.corsConfig = corsConfig;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().disable()
				.httpBasic().disable()
				.apply(new MyCustomDSL())
				.and()
				.authorizeRequests(authorize -> authorize.antMatchers("/api/v1/user/**")
						.access("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_COUBANG')")
						.antMatchers("/api/v1/seller/**")
						.access("hasRole('ROLE_COUBANG') or hasRole('ROLE_SELLER')")
						.antMatchers("/api/v1/coubang/**")
						.access("hasRole('ROLE_COUBANG')")
						.anyRequest().permitAll())
				.build();
	}

	public class MyCustomDSL extends AbstractHttpConfigurer<MyCustomDSL, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http
					.addFilter(corsConfig.corsFilter());
		}
	}
}
