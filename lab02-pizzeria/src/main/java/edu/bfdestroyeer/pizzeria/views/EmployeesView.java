package edu.bfdestroyeer.pizzeria.views;

import edu.bfdestroyeer.pizzeria.models.EmployeesCommand;
import edu.bfdestroyeer.pizzeria.models.OrderStatus;
import edu.bfdestroyeer.pizzeria.models.Pizza;

import java.util.Scanner;

public class EmployeesView {

    Scanner scanner = new Scanner(System.in);

    public EmployeesCommand requestCommand() {
        System.out.println(
                "Enter one of following commands:\n" +
                "ADD - add pizza to menu\n" +
                "REMOVE - remove pizza from menu\n" +
                "MENU - show menu\n" +
                "STATUS - update order status"
        );
        while (true) {
            try {
                return EmployeesCommand.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    public Pizza requestPizza() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Cost: ");
        Long cost = scanner.nextLong();
        return new Pizza(name, description, cost);
    }

    public Long requestPizzaId() {
        System.out.print("Enter pizza id: ");
        return scanner.nextLong();
    }

    public Long requestOderId() {
        System.out.print("Order ID: ");
        return scanner.nextLong();
    }

    public OrderStatus requestOderStatus() {
        System.out.print("OrderStatus [NOT_STARTED, COOKING, DELIVERING, DONE]: ");
        while (true) {
            try {
                return OrderStatus.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    public void showAddPizzaSuccessMessage(Long id) {
        System.out.println("Success! Pizza id: " + id);
    }

    public void showRemovePizzaFromMenuStatus(Long status) {
        if (status == 0) {
            System.out.println("Success!");
        } else {
            System.out.println("Failed!");
        }
    }

    public void showMenuElement(Long id, String name, String description, Long cost) {
        System.out.println("Id: " + id);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Cost: " + cost);
    }
}

