package edu.bfdestroyeer.pizzeria;

import com.google.protobuf.Empty;
import edu.bfdestroyeer.pizzeria.models.ConsumerCommand;
import edu.bfdestroyeer.pizzeria.models.OrderPosition;
import edu.bfdestroyeer.pizzeria.models.OrderStatus;
import edu.bfdestroyeer.pizzeria.models.Pizza;
import edu.bfdestroyeer.pizzeria.views.ConsumerView;

import java.util.List;
import java.util.Map;

public class PizzeriaConsumersClient extends PizzeriaClient {
    PizzeriaServiceGrpc.PizzeriaServiceBlockingStub client = createClient("localhost", 8080);
    ConsumerView view = new ConsumerView();

    private void start() {
        while (true) {
            ConsumerCommand command = this.view.requestCommand();
            switch (command) {
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
                case ORDER: {
                    List<OrderPosition> order = this.view.requestOrder();

                    MakeOrderRequest.Builder requestBuilder = MakeOrderRequest.newBuilder();
                    for (OrderPosition position: order) {
                        requestBuilder.addOrderPosition(
                                MakeOrderRequest.OrderPosition.newBuilder()
                                        .setPizzaId(position.getPizzaId())
                                        .setCount(position.getCount())
                                        .build()
                        );
                    }
                    OrderResponse response = this.client.makeOrder(requestBuilder.build());
                    this.view.showOrderSuccess(response.getId(), response.getCost());
                    break;
                }
                case STATUS: {
                    Long orderId = this.view.requestOderId();
                    GetOrderStatusRequest request = GetOrderStatusRequest.newBuilder()
                            .setOrderId(orderId)
                            .build();
                    this.view.showOrderStatus(OrderStatus.values()[this.client.getOrderStatus(request).getOrderStatus()]);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        PizzeriaConsumersClient consumerClient = new PizzeriaConsumersClient();
        consumerClient.start();
    }
}
