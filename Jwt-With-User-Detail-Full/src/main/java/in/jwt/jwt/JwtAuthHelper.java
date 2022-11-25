package in.jwt.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthHelper {

	private final Key key;

	public JwtAuthHelper(@Value("${jwt.secret.key}") String secretKey) {
		key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		
	}

	public String generateJwtAuthToken(String subject, Map<String, Object> claims) {
	 return Jwts.builder()
				.setSubject(subject)
				.addClaims(claims)
				.setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .signWith(key)
				.compact();
	}
	
	public Claims parseClaims(String token) {

		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

	}
}