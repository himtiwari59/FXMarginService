package com.santander.fx;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public enum  FxMessageQueue {

    INSTANCE;

    private BlockingQueue<String> messageQueue;

    FxMessageQueue(){
        messageQueue = new ArrayBlockingQueue<>(10);
    }

    public BlockingQueue<String> getMessageQueue(){
        return messageQueue;
    }
}
