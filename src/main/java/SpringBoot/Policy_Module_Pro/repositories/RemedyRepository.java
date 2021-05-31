package SpringBoot.Policy_Module_Pro.repositories;

import SpringBoot.Policy_Module_Pro.models.Remedy;
import SpringBoot.Policy_Module_Pro.models.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RemedyRepository extends JpaRepository<Remedy, Long> {
    Remedy findByActivity(Activity activity);
    Page<Remedy> findByActivity(Activity activity, Pageable pageable);
    List<Remedy> findAllByStatusOrderByRemedyTimeAsc(Boolean status);
    List<Remedy> findAllByStatusAndRemedyTimeBetween(Boolean status, LocalDateTime remedyStartTime, LocalDateTime remedyEndTime);
}
