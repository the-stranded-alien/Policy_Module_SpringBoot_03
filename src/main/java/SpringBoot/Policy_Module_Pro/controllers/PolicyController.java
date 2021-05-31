package SpringBoot.Policy_Module_Pro.controllers;

import SpringBoot.Policy_Module_Pro.models.Policy;
import SpringBoot.Policy_Module_Pro.models.Risk;
import SpringBoot.Policy_Module_Pro.models.UserInfo;
import SpringBoot.Policy_Module_Pro.services.PolicyService;
import SpringBoot.Policy_Module_Pro.services.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private RiskService riskService;

    @GetMapping
    public String viewPolicyHomePage(Model model) {
        return findPaginated(1, "policyName", "asc", model);
    }

    @GetMapping("/showNewPolicyForm")
    public String showNewPolicyForm(Model model) {
        Policy policy = new Policy();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Risk> disabledRisks = riskService.getAllRisksByCreatorAndStatus(userInfo.getUser(), false);
        List<Long> disabledRisksId = new ArrayList<>();
        for(Risk risk : disabledRisks) {
            disabledRisksId.add(risk.getId());
        }

        model.addAttribute("policy", policy);
        model.addAttribute("allRisks", riskService.getAllRisksByCreator(userInfo.getUser()));
        model.addAttribute("disabledRisks", disabledRisksId);
        return "policy/newPolicy";
    }

    @PostMapping("/savePolicy")
    public String savePolicy(@ModelAttribute("policy") Policy policy) {
        Policy newPolicy = new Policy();
        newPolicy.setPolicyName(policy.getPolicyName());
        newPolicy.setRemedyType(policy.getRemedyType());
        newPolicy.setRemedyTime(policy.getRemedyTime());
        newPolicy.setNotifyUser(policy.getNotifyUser());
        newPolicy.setNotifyAdmin(policy.getNotifyAdmin());
        newPolicy.setAdminEmail(policy.getAdminEmail());
        newPolicy.setAdminEmailSubject(policy.getAdminEmailSubject());
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        newPolicy.setCreator(userInfo.getUser());
        Set<Risk> risks = policy.getRisksInvolved();
        newPolicy.addRisks(risks);
        this.policyService.savePolicy(newPolicy);
        return "redirect:/policy";
    }

    @GetMapping("/showUpdatePolicyForm/{id}")
    public String showUpdatePolicyForm(@PathVariable(value="id") Long id, Model model) {
        Policy policy = policyService.getPolicyById(id);

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Risk> userRisks = riskService.getAllRisksByCreator(userInfo.getUser());

        Set<Risk> selected = policy.getRisksInvolved();
        List<Risk> selectedRisks = new ArrayList<>();
        selectedRisks.addAll(selected);
        List<Long> selectedRisksId = new ArrayList<>();
        for(Risk risk : selectedRisks) {
            selectedRisksId.add(risk.getId());
        }

        List<Risk> disabledRisks = riskService.getAllRisksByCreatorAndStatus(userInfo.getUser(), false);
        List<Long> disabledRisksId = new ArrayList<>();
        for(Risk risk : disabledRisks) {
            disabledRisksId.add(risk.getId());
        }

        model.addAttribute("policy", policy);
        model.addAttribute("userRisks", userRisks);
        model.addAttribute("selectedRisks", selectedRisksId);
        model.addAttribute("disabledRisks", disabledRisksId);

        return "policy/updatePolicy";
    }

    @GetMapping("/viewPolicy/{id}")
    public String viewPolicy(@PathVariable(value = "id") Long id, Model model) {
        Policy policy = this.policyService.getPolicyById(id);
        model.addAttribute("policy", policy);
        return "policy/viewPolicy";
    }

    @PostMapping("/updatePolicy")
    public String updatePolicy(@ModelAttribute("policy") Policy policy) {
        this.policyService.updatePolicy(policy);
        return "redirect:/policy";
    }

    @GetMapping("/deletePolicy/{id}")
    public String deletePolicy(@PathVariable(value="id") Long id) {
        this.policyService.deletePolicyById(id);
        return "redirect:/policy";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value="pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        Integer pageSize = 3;
        Page<Policy> page = policyService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Policy> listPolicies = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listPolicies", listPolicies);

        return "policy/homePolicy";
    }

}
