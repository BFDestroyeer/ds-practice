package edu.bfdestroyeer.pizzeria;

public class PizzeriaConsumersClient extends PizzeriaClient {
    public static void main(String[] args) {
        PizzeriaServiceGrpc.PizzeriaServiceBlockingStub client = createClient("localhost", 8080);
        System.out.println("Connected");
    }
}
