package SpringBoot.Policy_Module_Pro.repositories;

import SpringBoot.Policy_Module_Pro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
