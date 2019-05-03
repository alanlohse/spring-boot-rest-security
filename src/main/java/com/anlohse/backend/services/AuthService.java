package com.anlohse.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anlohse.backend.model.entities.User;
import com.anlohse.backend.model.types.MessageType;
import com.anlohse.backend.model.vo.MessageVO;
import com.anlohse.backend.repositories.UserRepository;
import com.anlohse.backend.security.TokenAuthService;

@RestController
@RequestMapping(path = "/auth")
public class AuthService {

	@Autowired
	private TokenAuthService tokenAuthService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping
	@Transactional(readOnly = true)
	public @ResponseBody ResponseEntity<Object> auth(@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password) {
		User user = userRepository.findByUsernameAndPassword(username, passwordEncoder.encode(password));
		if (user == null) {
			return ResponseEntity
					.status(HttpStatus.FORBIDDEN)
					.body(new MessageVO("Username or password not currect.", MessageType.ERROR));
		}
		return ResponseEntity.ok(tokenAuthService.createTokenForUser(user));
	}
	
}
