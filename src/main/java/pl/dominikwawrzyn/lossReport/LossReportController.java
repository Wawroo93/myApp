package pl.dominikwawrzyn.lossReport;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.productsBar.ProductsBar;

import java.util.List;

@Controller
@RequestMapping("/admin/lossReport")
public class LossReportController {
    private final LossReportRepository lossReportRepository;

    public LossReportController(LossReportRepository lossReportRepository) {
        this.lossReportRepository = lossReportRepository;
    }

    @GetMapping("/list")
    public String listLossReports(Model model) {
        List<LossReport> lossReports = lossReportRepository.findAll();
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
