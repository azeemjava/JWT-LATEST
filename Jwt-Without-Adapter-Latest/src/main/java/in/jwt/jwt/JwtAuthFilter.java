package in.jwt.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

//--------------------------- 21
	private final JwtAuthHelper jwtAuthHelper;

	
	@Override //------------------------ 17
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {

//------------------------ 19
		var getToken = getToken(request);
		
//--------------------------- 22
		var claims = jwtAuthHelper.parseClaims((String) getToken);
		
//-----------------------------24 -> SecurityConfiguration
		SecurityContextHolder.getContext()
		                     .setAuthentication(createJwtAuthentication(claims));
		filterChain.doFilter(request, response);

	}

//------------------------- 23	
	private Authentication createJwtAuthentication(Map<String, Object> claims) {

		var roles = Arrays.stream(claims.get("roles")
				                        .toString()
				                        .split(","))
				                        .map(SimpleGrantedAuthority::new)
			                          	.collect(Collectors.toList());
		return new UsernamePasswordAuthenticationToken(claims.get(Claims.SUBJECT), null, roles);
	}

//--------------------------- 20
	private Object getToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.filter(auth -> auth.startsWith("Bearer "))
				                    .map(auth -> auth.replace("Bearer ", ""))
				.orElseThrow(() -> new BadCredentialsException("Invalid token."));
	}

	@Override //------------------------ 18
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getRequestURI().startsWith("/login");
	}
}
