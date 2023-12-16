package pl.dominikwawrzyn.category;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/category")
public class UserCategoryController {
    private final CategoryRepository categoryRepository;

    public UserCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/listBar")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findByType("bar");
        model.addAttribute("categories", categories);
        return "user/userCategoryListBar";
    }
    @GetMapping("/listKitchen")
    public String listCategoriesKitchen(Model model) {
        List<Category> categories = categoryRepository.findByType("kitchen");
        model.addAttribute("categories", categories);
        return "user/userCategoryListKitchen";
    }
}
