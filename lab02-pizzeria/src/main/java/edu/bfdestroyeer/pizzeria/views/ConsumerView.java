package edu.bfdestroyeer.pizzeria.views;

import edu.bfdestroyeer.pizzeria.models.ConsumerCommand;
import edu.bfdestroyeer.pizzeria.models.OrderPosition;
import edu.bfdestroyeer.pizzeria.models.OrderStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConsumerView {

    Scanner scanner = new Scanner(System.in);

    public ConsumerCommand requestCommand() {
        System.out.println(
                "Enter one of following commands:\n" +
                "MENU - show menu\n" +
                "ORDER - remove pizza from menu\n" +
                "STATUS - check order status"
        );
        while (true) {
            try {
                return ConsumerCommand.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    public List<OrderPosition> requestOrder() {
        List<OrderPosition> result = new LinkedList<>();
        System.out.print("Order positions count: ");
        long positionsCount = scanner.nextLong();
        for (long i = 0L; i < positionsCount; i++) {
            System.out.print("Pizza ID: ");
            Long pizzaId = scanner.nextLong();
            System.out.print("Count: ");
            Long count = scanner.nextLong();
            result.add(new OrderPosition(pizzaId, count));
        }
        return result;
    }

    public Long requestOderId() {
        System.out.print("Order ID: ");
        return scanner.nextLong();
    }

    public void showMenuElement(Long id, String name, String description, Long cost) {
        System.out.println("Id: " + id);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Cost: " + cost);
    }

    public void showOrderSuccess(Long orderId, Long cost) {
        System.out.println("Order successes, ID: " + orderId + " Cost: " + cost);
    }

    public void showOrderStatus(OrderStatus orderStatus) {
        System.out.println("Order status: " + orderStatus);
    }
}
