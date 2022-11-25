package in.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import in.jwt.entity.JwtUser;
import in.jwt.repository.UserRepository;


@Service
public class CustomizeDetail implements UserDetailsService  {

	@Autowired
	UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		   
    	final JwtUser jwtUser = repository.findByName(username);
        
       UserDetails user = User.withUsername(jwtUser.getName()).password(jwtUser.getPassword()).authorities(jwtUser.getRole()).build();
       return user;
	}
}