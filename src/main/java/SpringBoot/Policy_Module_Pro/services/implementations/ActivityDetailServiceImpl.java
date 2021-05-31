package SpringBoot.Policy_Module_Pro.services.implementations;

import SpringBoot.Policy_Module_Pro.models.Activity;
import SpringBoot.Policy_Module_Pro.models.ActivityDetail;
import SpringBoot.Policy_Module_Pro.repositories.ActivityDetailRepository;
import SpringBoot.Policy_Module_Pro.services.ActivityDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityDetailServiceImpl implements ActivityDetailService {

    @Autowired
    private ActivityDetailRepository activityDetailRepository;

    @Override
    public ActivityDetail getActivityDetailById(Long id) {
        Optional<ActivityDetail> optional = activityDetailRepository.findById(id);
        ActivityDetail activityDetail = null;
        if(optional.isPresent()) {
            activityDetail = optional.get();
        } else {
            throw new RuntimeException("Activity Detail With Id : " + id + " Not Found !");
        }
        return activityDetail;
    }

    @Override
    public void saveActivityDetail(ActivityDetail activityDetail) {
        this.activityDetailRepository.save(activityDetail);
    }

    @Override
    public Page<ActivityDetail> findPaginatedByActivity(Activity activity, Integer pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.activityDetailRepository.findAllByActivity(activity, pageable);
    }
}
