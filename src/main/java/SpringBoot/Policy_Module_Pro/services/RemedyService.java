package SpringBoot.Policy_Module_Pro.services;

import SpringBoot.Policy_Module_Pro.models.Remedy;
import SpringBoot.Policy_Module_Pro.models.Activity;

import java.time.LocalDateTime;
import java.util.List;

public interface RemedyService {
    void saveRemedy(Remedy remedy);
    Remedy getRemedyByActivity(Activity activity);
    List<Remedy> getRemedyByStatusAndTime(Boolean status, LocalDateTime startTime, LocalDateTime endTime);
    void updateRemedyStatusById(Long id);
}
