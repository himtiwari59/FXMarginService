package com.santander.fx.boundry;


import com.santander.fx.Publisher;
import com.santander.fx.Subscriber;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.concurrent.*;

@Path("/fxMargin")
public class FxMarginRest {

    private ConcurrentMap<Integer, String > tickDataMap;

    public FxMarginRest(){

        tickDataMap = new ConcurrentHashMap<>();

        Thread publisher = new Thread(new Publisher());
        publisher.start();
        System.out.println("Publisher Started...");
    }

    @GET
    @Path("/getFxTick")
    public void getFxTick(@Suspended final AsyncResponse fxResponse){

        fxResponse.setTimeoutHandler(asyncResponse ->
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("TIME OUT !").build()));

        fxResponse.setTimeout(40, TimeUnit.SECONDS);

        Thread subscriber= new Thread(new Subscriber(tickDataMap));
        subscriber.start();

        fxResponse.resume(tickDataMap);
    }
}
