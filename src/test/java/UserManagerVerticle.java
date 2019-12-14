import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class UserManagerVerticle extends AbstractVerticle implements getRequestArray {
    String address = "User";
    String name = "UserVerticle";
    ArrayList<String> request = new ArrayList<String>();
    ArrayList<String> userList = new ArrayList<String>();

    public void start(Future<Void> startFuture){
       try{
           startFuture.complete();
           vertx.eventBus().consumer(address,message -> {
               JsonObject j = (JsonObject) message.body();
               request = getRequestArray.getRequestArray(j);
               if (request.get(1).equals("User")){
                   if(request.get(0).equals("Create")){
                       userList.add(request.get(2));
                       message.reply(" User " + request.get(2) + " add successfully!");
                   }
               }
               else if (request.get(1).equals("Bind")){
                   String Username = request.get(2);
                   if (!userList.contains(Username)){
                       message.reply("User not found!");
                   }
                   else{
                       message.reply("true");
                   }
               }
           });
       }catch (Exception e){
           startFuture.fail("something goes wrong..." + e.toString());
       }
    }

}
