import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class DeviceManagerReceiverVerticle extends AbstractVerticle {
    String name ;
    String address;
    DeviceManagerReceiverVerticle(String name , String address){
        this.name = name;
        this.address = address;
       // this.address = "DiviceManager";
    }
    public void start(Future<Void> startFuture){
        System.out.println("DeviceManagerReceiver "+ name + " start successfully~");
        vertx.eventBus().consumer(address , message -> {
            System.out.println(this.name + " reveive: " + message.body());
        });

    }
}
