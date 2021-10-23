
package client;

import com.grpc.greet.GreetEveryoneRequest;
import com.grpc.greet.GreetEveryoneResponse;
import com.grpc.greet.GreetServiceGrpc;
import com.grpc.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello I am gRPC client");
        biDiCall();
    }


    private static void biDiCall() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Sending Request");

        StreamObserver<GreetEveryoneRequest> requestStreamObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
            @Override
            public void onNext(GreetEveryoneResponse greetEveryoneResponse) {
                System.out.println("Response from server: " + greetEveryoneResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList("Ketki", "Ron", "Harry", "Hermoine", "Albus").forEach(
                name -> {
                    System.out.println("Sending : " + name);
                    try {
                        requestStreamObserver.onNext(GreetEveryoneRequest.newBuilder()
                                .setGreeting(Greeting.newBuilder().setFirstName(name).build())
                                .build());
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        );

        requestStreamObserver.onCompleted();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

