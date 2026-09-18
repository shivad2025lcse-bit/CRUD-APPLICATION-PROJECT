package com.example.SpringJPA.Service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringJPA.Exception.ResourceNotFoundException;
import com.example.SpringJPA.Model.Food;
import com.example.SpringJPA.Repository.FoodRepository;

@Service
public class FoodService {

    private static final Set<String> LEGACY_DEFAULT_DISHES = Set.of(
            "butter chicken", "paneer tikka", "dal makhani", "biryani", "masala dosa", "samosa");

    @Autowired
    private FoodRepository foodRepository;

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public List<Food> getAllFood() {
        return foodRepository.findAll();
    }

    public List<Food> getDashboardFood() {
        removeLegacyDefaultDishes();
        return getAllFood();
    }

    private void removeLegacyDefaultDishes() {
        List<Food> legacyDishes = getAllFood().stream()
            .filter(food -> food.getFoodName() != null
                        && LEGACY_DEFAULT_DISHES.contains(food.getFoodName().trim().toLowerCase()))
            .toList();
        if (!legacyDishes.isEmpty()) {
            foodRepository.deleteAll(legacyDishes);
        }
    }

    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + id));
    }

    public Food updateFood(Long id, Food foodDetails) {
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + id));

        if (foodDetails.getFoodName() != null) {
            existingFood.setFoodName(foodDetails.getFoodName());
        }
        if (foodDetails.getCategory() != null) {
            existingFood.setCategory(foodDetails.getCategory());
        }
        if (foodDetails.getDescription() != null) {
            existingFood.setDescription(foodDetails.getDescription());
        }
        if (foodDetails.getPrice() != null) {
            existingFood.setPrice(foodDetails.getPrice());
        }
        if (foodDetails.getImageUrl() != null) {
            existingFood.setImageUrl(foodDetails.getImageUrl());
        }
        if (foodDetails.getVegetarian() != null) {
            existingFood.setVegetarian(foodDetails.getVegetarian());
        }
        if (foodDetails.getRating() != null) {
            existingFood.setRating(foodDetails.getRating());
        }
        if (foodDetails.getPreparationMinutes() != null) {
            existingFood.setPreparationMinutes(foodDetails.getPreparationMinutes());
        }
        if (foodDetails.isAvailable() != null) {
            existingFood.setAvailable(foodDetails.isAvailable());
        }

        return foodRepository.save(existingFood);
    }

    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with ID: " + id));
        foodRepository.delete(food);
    }

    public void toggleAvailability(Long id) {
        Food food = getFoodById(id);
        food.setAvailable(!Boolean.TRUE.equals(food.isAvailable()));
        foodRepository.save(food);
    }

    public List<Food> getFoodByAvailability(Boolean isAvailable) {
        return foodRepository.findByIsAvailable(isAvailable);
    }

    public List<Food> searchFoodByName(String foodName) {
        return foodRepository.findByFoodNameContainingIgnoreCase(foodName);
    }
}
