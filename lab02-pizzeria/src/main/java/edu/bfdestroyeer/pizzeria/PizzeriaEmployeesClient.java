package edu.bfdestroyeer.pizzeria;

import com.google.protobuf.Empty;
import edu.bfdestroyeer.pizzeria.models.EmployeesCommand;
import edu.bfdestroyeer.pizzeria.models.Pizza;
import edu.bfdestroyeer.pizzeria.views.EmployeesView;

public class PizzeriaEmployeesClient extends PizzeriaClient {

    PizzeriaServiceGrpc.PizzeriaServiceBlockingStub client = createClient("localhost", 8080);
    EmployeesView view = new EmployeesView();

    private void start() {
        while (true) {
            EmployeesCommand command = this.view.requestCommand();
            switch (command) {
                case ADD: {
                    Pizza pizza = this.view.requestPizza();
                    AddPizzaToMenuRequest request = AddPizzaToMenuRequest.newBuilder()
                            .setName(pizza.getName())
                            .setDescription(pizza.getDescription())
                            .setCost(pizza.getCost())
                            .build();
                    this.view.showAddPizzaSuccessMessage(client.addPizzaToMenu(request).getId());
                    break;
                }
                case REMOVE: {
                    Long id = this.view.requestPizzaId();
                    RemovePizzaFromMenuRequest request = RemovePizzaFromMenuRequest.newBuilder()
                            .setId(id)
                            .build();
                    this.view.showRemovePizzaFromMenuStatus(client.removePizzaFromMenu(request).getStatus());
                    break;
                }
                case MENU: {
                    MenuResponse menu = client.getMenu(Empty.newBuilder().build());
                    for (MenuResponse.MenuElement element : menu.getMenuElementList()) {
                        this.view.showMenuElement(
                                element.getId(),
                                element.getName(),
                                element.getDescription(),
                                element.getCost()
                        );
                    }
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        PizzeriaEmployeesClient employeesClient = new PizzeriaEmployeesClient();
        employeesClient.start();
    }
}
