package sigma.app.api.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import sigma.app.api.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public UserDetails findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findUserByEmail(String email);
}
