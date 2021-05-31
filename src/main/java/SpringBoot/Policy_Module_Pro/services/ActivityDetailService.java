package SpringBoot.Policy_Module_Pro.services;

import SpringBoot.Policy_Module_Pro.models.Activity;
import SpringBoot.Policy_Module_Pro.models.ActivityDetail;
import org.springframework.data.domain.Page;

public interface ActivityDetailService {
    void saveActivityDetail(ActivityDetail activityDetail);
    ActivityDetail getActivityDetailById(Long id);
    Page<ActivityDetail> findPaginatedByActivity(Activity activity, Integer pageNo, Integer pageSize, String sortField, String sortDirection);
}
