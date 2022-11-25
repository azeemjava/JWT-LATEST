package in.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//------------------------ 1 -> SecurityConfiguration

@RestController
public class LoginController {

	@GetMapping("/")
	public String homePage() {
		return "Home page.";
	}

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
}
