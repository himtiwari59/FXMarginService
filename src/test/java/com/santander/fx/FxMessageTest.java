package com.santander.fx;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FxMessageTest {

    @Test
    public void start()  {

        ConcurrentMap<Integer, String > tickDataMap = new ConcurrentHashMap<>();

        Thread publisher = new Thread(new Publisher());
        Thread subscriber= new Thread(new Subscriber(tickDataMap));
        publisher.start();
        subscriber.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(!Thread.currentThread().isInterrupted()){
            for(Map.Entry<Integer, String> entry : tickDataMap.entrySet() ){
                System.out.println(entry.getKey() + " :: "+entry.getValue());
            }
        }


        try {
            publisher.join();
            subscriber.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
