package SpringBoot.Policy_Module_Pro.services.implementations;

import SpringBoot.Policy_Module_Pro.models.Remedy;
import SpringBoot.Policy_Module_Pro.models.Activity;
import SpringBoot.Policy_Module_Pro.repositories.RemedyRepository;
import SpringBoot.Policy_Module_Pro.services.RemedyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RemedyServiceImpl implements RemedyService {

    @Autowired
    private RemedyRepository remedyRepository;

    @Override
    public void saveRemedy(Remedy remedy) {
        this.remedyRepository.save(remedy);
    }

    @Override
    public Remedy getRemedyByActivity(Activity activity) {
        return this.remedyRepository.findByActivity(activity);
    }

    @Override
    public List<Remedy> getRemedyByStatusAndTime(Boolean status, LocalDateTime startTime, LocalDateTime endTime) {
        return this.remedyRepository.findAllByStatusAndRemedyTimeBetween(status, startTime, endTime);
    }

    @Override
    public void updateRemedyStatusById(Long id) {
        Optional<Remedy> optional = this.remedyRepository.findById(id);
        Remedy currentRemedy = null;
        if(optional.isPresent()) {
            currentRemedy = optional.get();
        } else {
            throw new RuntimeException("Action With This Id Not Found !");
        }
        currentRemedy.setStatus(true);
        this.remedyRepository.save(currentRemedy);
    }
}
