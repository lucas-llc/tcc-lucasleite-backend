package sigma.app.api.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import sigma.app.api.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
