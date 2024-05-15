package com.example.fitness;

public class FoodModel {
    private String name;
    private int calories;

    public FoodModel() {
        this.name = "";
        this.calories = 0;
    }

    public FoodModel(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
