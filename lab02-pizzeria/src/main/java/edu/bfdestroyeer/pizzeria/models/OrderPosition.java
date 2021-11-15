package edu.bfdestroyeer.pizzeria.models;

public class OrderPosition {
    private final Long pizzaId;
    private final Long count;

    public OrderPosition(Long pizzaId, Long count) {
        this.pizzaId = pizzaId;
        this.count = count;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public Long getCount() {
        return count;
    }
}
