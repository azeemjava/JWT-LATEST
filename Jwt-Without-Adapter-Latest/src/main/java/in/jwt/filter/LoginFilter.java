package in.jwt.filter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.jwt.jwt.JwtAuthHelper;
import lombok.RequiredArgsConstructor;

@Component
public class LoginFilter extends OncePerRequestFilter{

	//--------------------------- 6
	@Autowired
	private  AuthenticationManager authenticationManager;
	
	//--------------------------- 13
	@Autowired
	private  JwtAuthHelper jwtAuthHelper;
	
	
	@Override//--------------------------- 6 -> SecurityConfiguration
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		
		String username = request.getHeader("username");
		String password = request.getHeader("password");
		
		var authenticated = authenticationManager.authenticate(
				
					new UsernamePasswordAuthenticationToken(username, password));
		
	
		//--------------------------- 16 -> JwtAuthFilter
		response.setHeader(HttpHeaders.AUTHORIZATION, generateJwtAuthToken(authenticated));
	}

	
	
	//---------------------- 14 , 15 
	private String generateJwtAuthToken(Authentication authentication) {
		
		var user = (User) authentication.getPrincipal();
		var roles = user.getAuthorities()
				        .stream()
			        	.map(GrantedAuthority::getAuthority)
			        	.collect(Collectors.joining(","));
		
		
		return jwtAuthHelper.generateJwtAuthToken(user.getUsername(), 
				                                   Map.of("roles", roles));
	}

	
	
	
	
	@Override //--------------------------- 5
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		String reqMethod = request.getMethod();
		String reqUri = request.getRequestURI();
		boolean isLogin = HttpMethod.POST.matches(reqMethod) && reqUri.startsWith("/login");
		
		return !isLogin;
	}
	
	

	
}
