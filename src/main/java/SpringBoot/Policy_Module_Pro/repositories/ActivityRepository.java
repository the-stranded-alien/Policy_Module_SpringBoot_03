package SpringBoot.Policy_Module_Pro.repositories;

import SpringBoot.Policy_Module_Pro.models.Activity;
import SpringBoot.Policy_Module_Pro.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findAllByUser(User user, Pageable pageable);
}
