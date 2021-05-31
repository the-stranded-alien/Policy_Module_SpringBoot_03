package SpringBoot.Policy_Module_Pro.services;

import SpringBoot.Policy_Module_Pro.models.Policy;
import SpringBoot.Policy_Module_Pro.models.Risk;
import SpringBoot.Policy_Module_Pro.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RiskService {
    void saveRisk(Risk risk);
    Risk getRiskById(Long id);
    Risk updateRisk(Risk updatedRisk);
    void deleteRiskById(Long id);
    List<Risk> getAllRisksByCreator(User creator);
    Page<Risk> findPaginated(Integer pageNo, Integer pageSize, String sortField, String sortDirection);
    List<Risk> getAllRisksByCreatorAndStatus(User creator, Boolean status);
    Page<Risk> findPaginatedByStatus(Boolean status, Integer pageNo, Integer pageSize, String sortField, String sortDirection);
    Set<Risk> getAllRisksByCreatorAndStatusAndPolicy(User creator, Boolean status, Policy policy);
}
