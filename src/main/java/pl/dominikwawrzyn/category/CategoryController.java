package pl.dominikwawrzyn.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping("/addBar")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/adminCategoryAddBar";
    }
    @PostMapping("/addBar")
    public String addCategory(Category category) {
        category.setType("bar");
        categoryRepository.save(category);
        return "redirect:/admin/category/listBar";
    }
    @GetMapping("/listBar")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findByType("bar");
        model.addAttribute("categories", categories);
        return "admin/category/adminCategoryListBar";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id kategorii: " + id));
        categoryRepository.delete(category);
        return "redirect:/admin/category/listBar";
    }
    @GetMapping("/editBar/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category/adminCategoryEditBar";
    }

    @PostMapping("/editBar/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return "redirect:/admin/category/listBar";
    }
}
