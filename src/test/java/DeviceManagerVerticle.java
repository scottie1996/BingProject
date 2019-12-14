import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class DeviceManagerVerticle extends AbstractVerticle implements getRequestArray {
    String address = "Device";
    String name = "DeviceVerticle";
    ArrayList<String> request = new ArrayList<String>();
    ArrayList<String> deviceList = new ArrayList<String>();

    public void start(Future<Void> startFuture){
        vertx.deployVerticle(new UserManagerVerticle(), stringAsyncResult -> {
            if (stringAsyncResult.succeeded()){
                startFuture.complete();
                System.out.println("UserManager start successfully");
                vertx.eventBus().consumer(address , message -> {
                    //System.out.println("!!!!");
                   //System.out.println(this.name + " reveive: " + message.body());
                   JsonObject j = (JsonObject) message.body();
                   request = getRequestArray.getRequestArray(j);
                   if (request.get(1).equals("Device")){
                       if (request.get(0).equals("Create")){
                           deviceList.add(request.get(3));
                           //System.out.println(deviceList);
                           message.reply(" Device "+ request.get(3) + " add successfully!");
                       }
                   }
                   else if (request.get(1).equals("Bind")){
                       String Devicename = request.get(3);
                       if (!deviceList.contains(Devicename)){
                           message.reply("Device not found!");
                       }
                       else{
                           message.reply("true");
                       }
                   }

                });

                /*vertx.deployVerticle(new DeviceManagerReceiverVerticle("R2D2","DiviceManager"),stringAsyncResult1 -> {
                    if (stringAsyncResult1.succeeded()){
                        System.out.println("reveive!!");
                    }
                    else {
                        System.out.println("not reveive!!");
                    }

                });*/
            }
            else {
                System.out.println("UserManager not started");
            }
        });
    }
}
