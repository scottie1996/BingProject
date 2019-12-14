import io.vertx.core.Vertx;

public class ServerLauncher {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ServerVerticle(),stringAsyncResult -> {
            if (stringAsyncResult.succeeded()){
                System.out.println("ServerManager start successfully！！");
            }
            else {
                System.out.println("ServerManager not started ");
            }
        });
    }
}
