package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {


    public static void main(String[] args) {
        try {
            System.out.println("gRPC server started");

            Server server = ServerBuilder.forPort(50052)
                    .addService(new GreetServiceImpl())
                    .build();

            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Received shutdown request");
                server.shutdown();
                System.out.println("Successfully stopped the server");
            }));
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}


