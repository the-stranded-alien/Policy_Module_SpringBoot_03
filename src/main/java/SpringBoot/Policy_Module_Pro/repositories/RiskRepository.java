package SpringBoot.Policy_Module_Pro.repositories;

import SpringBoot.Policy_Module_Pro.models.Policy;
import SpringBoot.Policy_Module_Pro.models.Risk;
import SpringBoot.Policy_Module_Pro.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RiskRepository extends JpaRepository<Risk, Long> {
    List<Risk> findAllByCreator(User creator);
    Page<Risk> findAllByCreator(User creator, Pageable pageable);
    List<Risk> findAllByCreatorAndStatus(User creator, Boolean status);
    Page<Risk> findAllByCreatorAndStatus(User creator, Boolean status, Pageable pageable);
    Set<Risk> findAllByCreatorAndStatusAndPoliciesIncludedInContaining(User creator, Boolean status, Policy policy);
}
