
package server;

import com.grpc.greet.GreetEveryoneRequest;
import com.grpc.greet.GreetEveryoneResponse;
import com.grpc.greet.GreetServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    //List<GreetEveryoneRequest> allRequests = new ArrayList<>();
    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        StreamObserver<GreetEveryoneRequest> requestStreamObserver = new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest greetEveryoneRequest) {
                System.out.println("Received Request : " + greetEveryoneRequest.getGreeting());
                String response = "Hello " + greetEveryoneRequest.getGreeting().getFirstName();
                GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
                        .setResult(response).build();
                responseObserver.onNext(greetEveryoneResponse);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
                ;
            }
        };

        return requestStreamObserver;
    }
}

