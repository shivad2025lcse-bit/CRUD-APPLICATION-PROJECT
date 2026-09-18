package com.example.SpringJPA.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SpringJPA.Model.Food;
import com.example.SpringJPA.Service.FoodService;

@Controller
public class WebController {

    private static final String OWNER_REDIRECT = "redirect:/owner";

    private final FoodService foodService;

    public WebController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping({"/", "/index", "/demo", "/menu"})
    public String home(Model model) {
        model.addAttribute("foods", foodService.getDashboardFood());
        return "index";
    }

    @GetMapping({"/owner", "/dashboard", "/food", "/foodlist"})
    public String ownerView(Model model) {
        model.addAttribute("foods", foodService.getDashboardFood());
        return "foodlist";
    }

    @GetMapping("/food-form")
    public String showFoodForm(Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("formAction", "/food/save");
        return "food-form";
    }

    @GetMapping("/food/edit/{id}")
    public String showEditFoodForm(@PathVariable Long id, Model model) {
        model.addAttribute("food", foodService.getFoodById(id));
        model.addAttribute("formAction", "/food/update/" + id);
        return "food-form";
    }

    @PostMapping("/food/save")
    public String saveFood(@ModelAttribute Food food) {
        foodService.createFood(food);
        return OWNER_REDIRECT;
    }

    @PostMapping("/food/update/{id}")
    public String updateFood(@PathVariable Long id, @ModelAttribute Food food) {
        foodService.updateFood(id, food);
        return OWNER_REDIRECT;
    }

    @PostMapping("/food/toggle/{id}")
    public String toggleAvailability(@PathVariable Long id) {
        foodService.toggleAvailability(id);
        return OWNER_REDIRECT;
    }

    @PostMapping("/food/delete/{id}")
    public String deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return OWNER_REDIRECT;
    }
}