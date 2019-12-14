import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class ServerSenderVerticle extends AbstractVerticle {
        private JsonObject request;
        ServerSenderVerticle(JsonObject jsonObject){
            request = jsonObject;
        }
        public void start(){
            vertx.eventBus().publish("DiviceManager",request);
        }

}
