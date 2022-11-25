package in.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.jwt.entity.JwtUser;
import in.jwt.service.UserService;


@RestController
public class LoginController {
	
	
	@Autowired
	UserService service;


	@GetMapping("/home")
	public String homePage1() {
		return "Home page.";
	}
	
	@GetMapping("/user")
	public String userPage() {
		return "User page.";
	}

	@GetMapping("/admin")
	public String adminPage() {
		return "Admin page";
	}

	@PostMapping("/register")
	public JwtUser save(@RequestBody JwtUser jwtUser) {
		return service.saveUser(jwtUser);
	}
}