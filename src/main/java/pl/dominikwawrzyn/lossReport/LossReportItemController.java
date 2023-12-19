package pl.dominikwawrzyn.lossReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin/lossReportItem")
public class LossReportItemController {
    private final LossReportItemRepository lossReportItemRepository;
    private final LossReportRepository lossReportRepository;

    public LossReportItemController(LossReportItemRepository lossReportItemRepository, LossReportRepository lossReportRepository) {
        this.lossReportItemRepository = lossReportItemRepository;
        this.lossReportRepository = lossReportRepository;
    }
    @GetMapping("/add/{reportId}")
    public String showAddForm(@PathVariable Long reportId, Model model) {
        LossReport lossReport = lossReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + reportId));
        model.addAttribute("lossReport", lossReport);
        model.addAttribute("lossReportItem", new LossReportItem());
        return "admin/lossReport/adminLossReportItemAdd";
    }
    @PostMapping("/add/{reportId}")
    public String addLossReportItem(@PathVariable Long reportId, @Valid @ModelAttribute LossReportItem lossReportItem, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LossReport lossReport = lossReportRepository.findById(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + reportId));
            model.addAttribute("lossReport", lossReport);
            return "admin/lossReport/adminLossReportItemAdd";
        }
        LossReport lossReport = lossReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report Id:" + reportId));
        lossReportItem.setLossReport(lossReport);
        lossReportItemRepository.save(lossReportItem);
        return "redirect:/admin/lossReport/" + reportId;
    }
    @DeleteMapping("/delete/{itemId}")
    public String deleteLossReportItem(@PathVariable Long itemId) {
        LossReportItem lossReportItem = lossReportItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + itemId));
        Long reportId = lossReportItem.getLossReport().getId();
        lossReportItemRepository.delete(lossReportItem);
        return "redirect:/admin/lossReport/" + reportId;
    }

    @GetMapping("/edit/{itemId}")
    public String showEditForm(@PathVariable Long itemId, Model model) {
        LossReportItem lossReportItem = lossReportItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + itemId));
        model.addAttribute("lossReportItem", lossReportItem);
        return "admin/lossReport/adminLossReportItemEdit";
    }

    @PostMapping("/edit/{itemId}")
    public String editLossReportItem(@PathVariable Long itemId, @Valid @ModelAttribute LossReportItem lossReportItem, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/lossReport/adminLossReportItemEdit";
        }
        LossReportItem existingLossReportItem = lossReportItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + itemId));
        existingLossReportItem.setDescription(lossReportItem.getDescription());
        existingLossReportItem.setCost(lossReportItem.getCost());
        existingLossReportItem.setWeight(lossReportItem.getWeight());
        existingLossReportItem.setQuantity(lossReportItem.getQuantity());
        existingLossReportItem.setDay(lossReportItem.getDay());
        lossReportItemRepository.save(existingLossReportItem);
        return "redirect:/admin/lossReport/" + existingLossReportItem.getLossReport().getId();
    }
}
