package com.example.fitness;

import java.util.ArrayList;
import java.util.List;

public class MenuModel {
    private String id;
    private String name, description, image;
    private List<String> numbers;
    private List<FoodModel> foods;

    public MenuModel(String name, String description, String image, List<String> numbers, String id) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.numbers = numbers;
        this.id = id;
        this.foods = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public List<FoodModel> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodModel> foods) {
        this.foods = foods;
    }

    public void addFood(String foodName, int calories) {
        FoodModel food = new FoodModel(foodName, calories);
        this.foods.add(food);
    }
}
