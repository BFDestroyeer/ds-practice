package edu.bfdestroyeer.pizzeria.models;

public class Pizza {
    private String name;
    private String description;
    private Long cost;

    public Pizza(String name, String description, Long cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCost() {
        return cost;
    }
}
