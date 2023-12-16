package pl.dominikwawrzyn.productsBar;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/productsBar")
public class ProductsBarController {
    private final ProductsBarRepository productsBarRepository;
    private final CategoryRepository categoryRepository;

    public ProductsBarController(ProductsBarRepository productsBarRepository, CategoryRepository categoryRepository) {
        this.productsBarRepository = productsBarRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add/{categoryId}")
    public String showAddProductsBarForm(@PathVariable Long categoryId, Model model) {
        ProductsBar productsBar = new ProductsBar();
        model.addAttribute("productsBar", productsBar);
        model.addAttribute("categoryId", categoryId);
        return "admin/productsBar/adminProductsBarAdd";
    }

    @PostMapping("/add/{categoryId}")
    public String addProductsBar(@PathVariable Long categoryId, @Valid @ModelAttribute ProductsBar productsBar, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productsBar/adminProductsBarAdd";
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        productsBar.setCategory(category);
        productsBarRepository.save(productsBar);
        return "redirect:/admin/category/listProductsBar";
    }
    @PostMapping("/updateQuantity/{productId}")
    public String updateQuantity(@PathVariable Long productId, @RequestParam("quantity") Integer quantity) {
        ProductsBar productsBar = productsBarRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        productsBar.setQuantity(quantity);
        productsBarRepository.save(productsBar);
        return "redirect:/admin/category/listProductsBar";
    }
//    @PostMapping("/updateName/{productId}")
//    public String updateName(@PathVariable Long productId, @RequestParam("name") String name) {
//        ProductsBar productsBar = productsBarRepository.findById(productId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
//        productsBar.setName(name);
//        productsBarRepository.save(productsBar);
//        return "redirect:/admin/category/listProductsBar";
//    }

    @PostMapping("/delete/{productId}")
    public String deleteProductBar(@PathVariable Long productId) {
        ProductsBar productsBar = productsBarRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        productsBarRepository.delete(productsBar);
        return "redirect:/admin/category/listProductsBar";
    }
}
