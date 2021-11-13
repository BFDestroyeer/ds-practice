package edu.bfdestroyeer.pizzeria;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

public class PizzeriaClient {
    protected static PizzeriaServiceGrpc.PizzeriaServiceBlockingStub createClient(String host, int port) {
        Channel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return PizzeriaServiceGrpc.newBlockingStub(channel);
    }
}
