package com.santander.fx;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class FxMarginRestClient {

    @Test
    public void getTickData(){
        getRequest("/fxMargin/getFxTick", Void.class);
    }

    public static <T> T getRequest(String uri, Class<T> responseType) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080" + uri);
        return target.request().get(responseType);
    }
}
