package com.santander.fx;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher implements Runnable {

    private BlockingQueue<String> messageBrokerQueue;
    private static final DecimalFormat df = new DecimalFormat("#.####");

    public Publisher() {
        this.messageBrokerQueue = FxMessageQueue.INSTANCE.getMessageQueue();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = getNextTick();
                messageBrokerQueue.put(message);
                //System.out.println(String.format("Sent \n %s", message));
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null,e);
            }
        }

    }

    private String getNextTick() {
        StringBuilder message = new StringBuilder();
        String[] products = new String[]{"EUR/JPY", "EUR/USD", "GBP/USD", "GBP/JPY"};
        int messageCount =  ThreadLocalRandom.current().nextInt(products.length);

        for (int i = 0; i < messageCount+1 ; i++) {
            Integer id = ThreadLocalRandom.current().nextInt(100, 1000);

            Double bid = ThreadLocalRandom.current().nextDouble(1,3);
            Double ask = Double.sum(bid,0.01d);

            String messageLine = String.join(",", id.toString() , products[messageCount],
                    Double.valueOf(df.format(bid)).toString(),
                    Double.valueOf(df.format(ask)).toString(),
                    LocalDateTime.now().toString());

            message.append(messageLine);
            message.append("\n");
        }
        return message.toString();
    }
}
