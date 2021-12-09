package edu.bfdestroyeer.pizzeria;

import com.google.protobuf.Empty;
import edu.bfdestroyeer.pizzeria.models.Order;
import edu.bfdestroyeer.pizzeria.models.OrderStatus;
import edu.bfdestroyeer.pizzeria.models.Pizza;
import edu.bfdestroyeer.pizzeria.utils.IdGenerator;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PizzeriaService extends PizzeriaServiceGrpc.PizzeriaServiceImplBase {
    Map<Long, Pizza> menu = new HashMap<>();
    IdGenerator menuIdGenerator = new IdGenerator();
    Map<Long, Order> orders = new HashMap<>();
    IdGenerator ordersIdGenerator = new IdGenerator();

    @Override
    public void addPizzaToMenu(AddPizzaToMenuRequest request, StreamObserver<IdResponse> responseObserver) {
        Pizza pizza = new Pizza(
                request.getName(),
                request.getDescription(),
                request.getCost()
        );
        Long id = this.menuIdGenerator.next();
        menu.put(id, pizza);

        IdResponse response = IdResponse.newBuilder()
                .setId(id)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removePizzaFromMenu(RemovePizzaFromMenuRequest request, StreamObserver<StatusReponse> responseObserver) {
        StatusReponse response;
        if (menu.remove(request.getId()) != null) {
            response = StatusReponse.newBuilder()
                    .setStatus(0)
                    .build();
        } else {
            response = StatusReponse.newBuilder()
                    .setStatus(1)
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMenu(Empty request, StreamObserver<MenuResponse> responseObserver) {
        MenuResponse.Builder responseBuilder = MenuResponse.newBuilder();
        for (Map.Entry<Long, Pizza> entry: menu.entrySet()) {
            responseBuilder.addMenuElement(
                    MenuResponse.MenuElement.newBuilder()
                            .setId(entry.getKey())
                            .setName(entry.getValue().getName())
                            .setDescription(entry.getValue().getDescription())
                            .setCost(entry.getValue().getCost())
                            .build()
            );
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void makeOrder(MakeOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        Long orderId = this.ordersIdGenerator.next();
        Long cost = 0L;
        orders.put(orderId, new Order());
        for (MakeOrderRequest.OrderPosition position: request.getOrderPositionList()) {
            if (this.menu.containsKey(position.getPizzaId())) {
                cost += position.getCount() * this.menu.get(position.getPizzaId()).getCost();
            }
        }
        OrderResponse response = OrderResponse.newBuilder()
                .setId(orderId)
                .setCost(cost)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusRequest request, StreamObserver<Empty> responseObserver) {
        if (this.orders.containsKey(request.getOrderId())) {
            this.orders.get(request.getOrderId()).setOrderStatus(OrderStatus.values()[request.getOrderStatus()]);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderStatus(GetOrderStatusRequest request, StreamObserver<OrderStatusResponse> responseObserver) {
        OrderStatus orderStatus = OrderStatus.UNAVAILABLE;
        if (this.orders.containsKey(request.getOrderId())) {
            orderStatus = this.orders.get(request.getOrderId()).getOrderStatus();
        }
        OrderStatusResponse response = OrderStatusResponse.newBuilder()
                .setOrderStatus(orderStatus.ordinal())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void initialize() {
        this.menu.put(menuIdGenerator.next(), new Pizza("Peperoni", "Tasty", 500L));
        this.menu.put(menuIdGenerator.next(), new Pizza("Four-Cheese", "Yummy", 450L));
        this.menu.put(menuIdGenerator.next(), new Pizza("Hawaiian", "Ew", 550L));
        this.orders.put(ordersIdGenerator.next(), new Order());
        this.orders.put(ordersIdGenerator.next(), new Order());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PizzeriaService pizzeriaService = new PizzeriaService();
        pizzeriaService.initialize();
        Server server = ServerBuilder
                .forPort(8080)
                .addService(pizzeriaService)
                .build();
        server.start();
        server.awaitTermination();
    }
}
