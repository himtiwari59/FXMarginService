package com.santander.fx;


import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Subscriber implements Runnable {

    private BlockingQueue<String> messageBrokerQueue;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private ConcurrentMap<Integer, String > tickDataMap ;
    private static final BigDecimal offset = new BigDecimal(0.001);
    private Margin margin ;

    public Subscriber( ConcurrentMap<Integer, String > tickDataMap){
        this.messageBrokerQueue = FxMessageQueue.INSTANCE.getMessageQueue();
        this.tickDataMap = tickDataMap;
        this.margin = input -> input.signum() > 0 ? input.add(offset) : input.subtract(offset);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                String message = messageBrokerQueue.take();
                //System.out.println(String.format("Received \n %s", message));
                validate(message);
                if(!atomicBoolean.get()){
                    System.out.println(String.format("Invalid Message %S", message));
                }
                parse(message);
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null,e);
            }
        }
    }

    private void validate(final String message){
        //YTD assume all valid messages as of now
        atomicBoolean.getAndSet(true);
    }

    private void parse(final String message){
        String[] lines = message.split("\\n");

        for(String line : lines){
            String[] tickData = line.split(",");

            BigDecimal adjustedBid = this.margin.apply(new BigDecimal(tickData[2]).negate()).negate().setScale(4, BigDecimal.ROUND_HALF_UP);
            BigDecimal adjustedAsk = this.margin.apply(new BigDecimal(tickData[3])).setScale(4, BigDecimal.ROUND_HALF_UP);

            tickDataMap.put(Integer.parseInt(tickData[0]), String.join(",", tickData[1], adjustedBid.toString(),
                    adjustedAsk.toString(), tickData[4] ));

        }
    }
}
