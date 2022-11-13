package com.coubang.coubang.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coubang.coubang.user.model.User;
import com.coubang.coubang.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("PrincipalDetailsService : 진입");
		User user = userRepository.findByEmail(email);
		return new PrincipalDetails(user);
	}
}
