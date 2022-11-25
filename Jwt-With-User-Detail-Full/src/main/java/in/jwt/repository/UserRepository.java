package in.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.jwt.entity.JwtUser;

@Repository
public interface UserRepository extends JpaRepository<JwtUser,Integer>{

	JwtUser findByName(String username);

}