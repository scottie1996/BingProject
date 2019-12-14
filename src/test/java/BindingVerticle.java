import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BindingVerticle extends AbstractVerticle implements getRequestArray {
    String address = "Bind";
    String name = "BindingVerticle";
    ArrayList<String> request = new ArrayList<String>();
    HashMap<String,String> BindMap = new HashMap<>();
    @Override
    public void start(Future<Void> startFuture){
        vertx.deployVerticle(new DeviceManagerVerticle(), stringAsyncResult -> {
            if (stringAsyncResult.succeeded()){
                startFuture.complete();
                vertx.eventBus().consumer(address,message -> {
                    JsonObject j = (JsonObject) message.body();
                    request = getRequestArray.getRequestArray(j);
                    if(request.get(0).equals("Create")){
                        BindMap.put(request.get(2),request.get(3));
                        System.out.println(BindMap);
                        message.reply("User " + request.get(2) +" and Device " +request.get(3)+ " bind successfully!");
                    }
                });
            }
            else{
                System.out.println("DeviceManager not start!");
            }
        });
    }
}
