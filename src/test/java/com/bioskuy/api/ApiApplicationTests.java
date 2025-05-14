package com.bioskuy.api;

import com.bioskuy.api.controller.UserController;
import com.bioskuy.api.repository.UserRepository;
import com.bioskuy.api.security.JwtUtil;
import com.bioskuy.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApiApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private Environment environment;

	@Test
	void contextLoads() {
		// Verifies that the Spring application context loads successfully
	}

	@Test
	void testComponentAvailability() {
		// Verifies that all critical parts are properly wired
		assertNotNull(userController);
		assertNotNull(userService);
		assertNotNull(userRepository);
		assertNotNull(jwtUtil);
	}

	@Test
	void testJwtConfiguration() {
		// Verifies JWT configuration is properly set
		assertNotNull(environment.getProperty("jwt.secret"));
	}
}
