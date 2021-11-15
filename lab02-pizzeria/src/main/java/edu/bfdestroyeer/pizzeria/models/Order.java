package edu.bfdestroyeer.pizzeria.models;

public class Order {
    OrderStatus orderStatus = OrderStatus.NOT_STARTED;

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
