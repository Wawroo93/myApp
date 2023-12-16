package pl.dominikwawrzyn.lossReport;


import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/lossReport")
public class LossReportController {
    private final LossReportRepository lossReportRepository;
    private final LossReportItemRepository lossReportItemRepository;

    public LossReportController(LossReportRepository lossReportRepository, LossReportItemRepository lossReportItemRepository) {
        this.lossReportRepository = lossReportRepository;
        this.lossReportItemRepository = lossReportItemRepository;
    }
    @GetMapping("/{id}")
    public String getLossReportItems(@PathVariable Long id, Model model) {
        LossReport lossReport = lossReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + id));
        List<LossReportItem> lossReportItems = lossReportItemRepository.findAllByLossReport(lossReport);
        model.addAttribute("lossReportItems", lossReportItems);
        model.addAttribute("lossReport", lossReport);
        return "admin/lossReport/adminLossReportItemList";
    }

    @GetMapping("/list")
    public String listLossReports(Model model) {
        List<LossReport> lossReports = lossReportRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("lossReports", lossReports);
        return "admin/lossReport/adminLossReportList";
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("lossReport", new LossReport());
        return "admin/lossReport/adminLossReportAdd";
    }

    @PostMapping("/add")
    public String addLossReport(@Valid @ModelAttribute LossReport lossReport, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/lossReport/adminLossReportAdd";
        }
        lossReportRepository.save(lossReport);
        return "redirect:/admin/lossReport/list";
    }
    @PostMapping("/delete/{reportId}")
    public String deleteLossReport(@PathVariable Long reportId) {
        LossReport lossReport = lossReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + reportId));
        lossReportRepository.delete(lossReport);
        return "redirect:/admin/lossReport/list";
    }
}
