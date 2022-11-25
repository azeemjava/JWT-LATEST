package in.jwt.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import in.jwt.filter.LoginFilter;
import in.jwt.jwt.JwtAuthFilter;

@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

//---------------------------- 7
	@Autowired
	@Lazy 
	private LoginFilter loginFilter;

//---------------25
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	
	@Bean //----------------------------- 2
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager userDt = new InMemoryUserDetailsManager();/* In-memory data-store */
		
		userDt.createUser(User.builder().username("user").password("{noop}user").roles("USER").build());
		userDt.createUser(User.builder().username("admin").password("{noop}admin").roles("ADMIN").build());
		return userDt;
	}

	@Bean //----------------------------- 3
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
		daoAuth.setUserDetailsService(userDetailsService);
		return new ProviderManager(daoAuth);
	}
	
	@Bean //----------------------------- 4 -> LoginFilter
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.authorizeHttpRequests().antMatchers("/home").hasRole("USER")
		                            .antMatchers("/admin").hasRole("ADMIN");
		
//------------------------------- 8 -> JwtAuthHelper
		http.addFilterAt(loginFilter, BasicAuthenticationFilter.class);
		
//------------------------------- 26 -> END
		http.addFilterAfter(jwtAuthFilter, BasicAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();

	}
}
