package in.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.jwt.entity.JwtUser;
import in.jwt.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	PasswordEncoder encoder;

	public JwtUser saveUser(JwtUser jwtUser) {
		jwtUser.setPassword(encoder.encode(jwtUser.getPassword()));
		return repository.save(jwtUser);

	}
}