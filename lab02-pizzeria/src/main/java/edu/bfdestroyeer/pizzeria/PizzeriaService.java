package edu.bfdestroyeer.pizzeria;

import com.google.protobuf.Empty;
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
        if (menu.remove(request.getId()) == null) {
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

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new PizzeriaService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
