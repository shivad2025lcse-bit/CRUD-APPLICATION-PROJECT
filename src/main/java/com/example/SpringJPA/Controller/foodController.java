package com.example.SpringJPA.Controller;

import com.example.SpringJPA.Model.Food;
import com.example.SpringJPA.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/foods", "/api/orders"})
public class foodController {

    @Autowired
    private FoodService foodService;

    // 1. CREATE - Add new Food record
    @PostMapping({"", "/addFood"})
    public ResponseEntity<Food> addFood(@RequestBody Food food) {
        Food createdFood = foodService.createFood(food);
        return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    // 2. READ ALL - Get all foods (with optional search and filter)
    @GetMapping({"", "/getFood"})
    public ResponseEntity<List<Food>> getAllFoods(
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String name) {
        if (available != null) {
            return ResponseEntity.ok(foodService.getFoodByAvailability(available));
        }
        if (name != null && !name.trim().isEmpty()) {
            return ResponseEntity.ok(foodService.searchFoodByName(name));
        }
        return ResponseEntity.ok(foodService.getAllFood());
    }

    // 3. READ ONE - Get food by ID
    @GetMapping({"/{id}", "/getFood/{id}"})
    public ResponseEntity<Food> getFoodById(@PathVariable Long id) {
        Food food = foodService.getFoodById(id);
        return ResponseEntity.ok(food);
    }

    // 4. UPDATE - Update existing food by ID
    @PutMapping({"/{id}", "/updateFood/{id}"})
    public ResponseEntity<Food> updateFood(@PathVariable Long id, @RequestBody Food food) {
        Food updatedFood = foodService.updateFood(id, food);
        return ResponseEntity.ok(updatedFood);
    }

    // 5. DELETE - Delete food by ID
    @DeleteMapping({"/{id}", "/deleteFood/{id}"})
    public ResponseEntity<String> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok("Food with ID " + id + " has been successfully deleted.");
    }
}
