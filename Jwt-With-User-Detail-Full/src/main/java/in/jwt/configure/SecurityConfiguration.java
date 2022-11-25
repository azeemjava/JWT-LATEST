package in.jwt.configure;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import in.jwt.filter.LoginFilter;
import in.jwt.jwt.JwtAuthFilter;
import in.jwt.repository.UserRepository;

@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration    {
	

	@Autowired
	@Lazy 
	private LoginFilter loginFilter;

	@Autowired
	@Lazy
	private JwtAuthFilter jwtAuthFilter;

	@Autowired
	UserRepository repository;
	
	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }


	@Bean 
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
		daoAuth.setUserDetailsService(userDetailsService);
		daoAuth.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoAuth);
	}
	
	@Bean 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.authorizeHttpRequests().antMatchers("/user").hasAnyAuthority("USER","ADMIN")
		                            .antMatchers("/admin").hasAuthority("ADMIN")
		                            .antMatchers("/home").permitAll();
		
		http.addFilterAt(loginFilter, BasicAuthenticationFilter.class);
		http.addFilterAfter(jwtAuthFilter, BasicAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();

	}
}