import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;

public class ServerVerticle extends AbstractVerticle {

    private HttpServer httpServer = null;
    private ArrayList<String> requestArray = new ArrayList<String>();
    private JsonObject r = new JsonObject();

    public void start(Future<Void> startFuture){
        vertx.deployVerticle(new BindingVerticle(), stringAsyncResult -> {
            if (stringAsyncResult.succeeded()){
                System.out.println("BindingManager start successfully");
                startFuture.complete();
            }
            else {
                System.out.println("BindingManager not started");
            }
        });

        httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());//获取并处理请求的消息体
        Route route_get = router.route().method(HttpMethod.GET);
        Route route_post = router.route().method(HttpMethod.POST);
        route_get.handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            response.end("This is a get request!");
        });
        route_post.handler(routingContext -> {
            JsonObject request = routingContext.getBodyAsJson();
            //System.out.println(request);
            HttpServerResponse response = routingContext.response();
            //response.putHeader("content-type", "text/plain");
            //response.end("This is a post request! ");
           // String destination = request.g
            if (request.getString("target").equals("User")||request.getString("target").equals("Device")){
                vertx.eventBus().send(request.getString("target"),request,messageAsyncResult -> {

                    if (messageAsyncResult.succeeded()){
                        String reply = (String) messageAsyncResult.result().body();
                        JsonObject r = new JsonObject();
                        r.put("Reply",reply);
                        response.end(r.toBuffer());
                    }
                    else {
                        //返回失败
                    }
                });
            }
            else if (request.getString("target").equals("Bind")){

                    vertx.eventBus().send("Device",request,deviceMessageAsyncResult -> {

                        if (deviceMessageAsyncResult.succeeded()){
                            if (deviceMessageAsyncResult.result().body().equals("true")){
                                vertx.eventBus().send("User",request,userMessageAsyncResult -> {
                                    if (userMessageAsyncResult.result().body().equals("true")){
                                        if (userMessageAsyncResult.succeeded()){
                                            vertx.eventBus().send("Bind", request,bindMessageAsyncResult -> {
                                                if (bindMessageAsyncResult.succeeded()){
                                                    String reply = (String) bindMessageAsyncResult.result().body();
                                                    r.put("Reply",reply);
                                                    response.end(r.toBuffer());
                                                }
                                            });
                                        }
                                    }
                                    else{
                                        String reply = (String) deviceMessageAsyncResult.result().body();
                                        r.put("Reply",reply);
                                        response.end(r.toBuffer());
                                    }
                                });
                            }
                            else{
                                String reply = (String) deviceMessageAsyncResult.result().body();
                                r.put("Reply",reply);
                                response.end(r.toBuffer());
                            }
                           /* String reply =  (String) messageAsyncResult.result().body();
                            vertx.eventBus().send(request.getString("User"),request,messageAsyncResult2 -> {
                                String reply2 = (String) messageAsyncResult.result().body();
                            });*/
                        }
                    });
                    /*vertx.eventBus().send("Bind", request,messageAsyncResult -> {
                       if (messageAsyncResult.succeeded()){

                           String reply = (String) messageAsyncResult.result().body();
                           JsonObject r = new JsonObject();
                           r.put("Reply",reply);
                           response.end(r.toBuffer());
                       }
                    });*/

            }

           /* vertx.deployVerticle(new ServerSenderVerticle(request),stringAsyncResult -> {
                if(stringAsyncResult.succeeded()){
                    System.out.println("send!!");
                }
                else{
                    System.out.println("not send ");
                }

            });*/
        });
        httpServer.requestHandler(router::accept).listen(8080);
    }

    private ArrayList<String> getRequestArray(JsonObject request){
        String operationString= request.getString("operation");
        String targetString= request.getString("target");
        String usernameString = request.getString("username");
        String devicenameString = request.getString("devicename");
        requestArray.add(operationString);
        requestArray.add(targetString);
        requestArray.add(usernameString);
        requestArray.add(devicenameString);
        return requestArray;
    }

}
