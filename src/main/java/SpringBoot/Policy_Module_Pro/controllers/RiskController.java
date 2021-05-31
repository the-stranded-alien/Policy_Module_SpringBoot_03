package SpringBoot.Policy_Module_Pro.controllers;

import SpringBoot.Policy_Module_Pro.models.Policy;
import SpringBoot.Policy_Module_Pro.models.Risk;
import SpringBoot.Policy_Module_Pro.services.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;

    @GetMapping
    public String viewRiskHomePage(Model model) {
        return findPaginated(1, "title", "asc", model);
    }

    @GetMapping("/showNewRiskForm")
    public String showNewRiskForm(Model model) {
        Risk risk = new Risk();
        model.addAttribute("risk", risk);
        return "risk/newRisk";
    }

    @GetMapping("/viewRisk/{id}")
    public String viewRisk(@PathVariable(value = "id") Long id, Model model) {
        Risk risk = this.riskService.getRiskById(id);
        model.addAttribute("risk", risk);
        return "risk/viewRisk";
    }

    @PostMapping("/saveRisk")
    public String saveRisk(@ModelAttribute("risk") Risk risk) {
        this.riskService.saveRisk(risk);
        return "redirect:/risk";
    }

    @GetMapping("/showUpdateRiskForm/{id}")
    public String showUpdateRiskForm(@PathVariable(value="id") Long id, Model model) {
        Risk risk = riskService.getRiskById(id);
        model.addAttribute("risk", risk);
        return "risk/updateRisk";
    }

    @PostMapping("/updateRisk")
    public String updateRisk(@ModelAttribute("risk") Risk risk) {
        Risk updatedRisk = this.riskService.updateRisk(risk);
        return "redirect:/risk";
    }

    @GetMapping("/deleteRisk/{id}")
    public String deleteRisk(@PathVariable(value="id") Long id) {
        Risk risk = riskService.getRiskById(id);
        for(Policy policy : risk.getPoliciesIncludedIn()) {
            policy.getRisksInvolved().remove(risk);
        }
        risk.getPoliciesIncludedIn().clear();
        this.riskService.deleteRiskById(id);
        return "redirect:/risk";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value="pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {

        Integer pageSize = 5;
        Page<Risk> page = riskService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Risk> listRisks = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listRisks", listRisks);

        return "risk/homeRisk";
    }
}
