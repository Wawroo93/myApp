package pl.dominikwawrzyn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        // Tutaj możesz dodać logikę, która ma być wykonana przed wyświetleniem strony adminDashboard

        // Zwróć nazwę widoku, który ma być wyświetlony (nazwa pliku HTML bez rozszerzenia)
        return "admin/adminDashboard";
    }
}