package com.coubang.coubang.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coubang.coubang.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
