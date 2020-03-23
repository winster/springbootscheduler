package com.n26.transactions.persistence;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class DataStoreSingleton {

    private static DataStoreSingleton instance;

    Logger logger = LoggerFactory.getLogger(DataStoreSingleton.class);

    private List<List<Transaction>> oneMinuteTransactions;

    private DataStoreSingleton() {
        initializeList();
    }
    private Statistics statistics = new Statistics();

    CountDownLatch latch = null;
    CountDownLatch deleteLatch = null;

    public static synchronized DataStoreSingleton getInstance() {
        if (instance == null) {
            instance = new DataStoreSingleton();
        }
        return instance;
    }

    public void store(Transaction transaction) {
        logger.debug("Inside Store");
        long timeInMillis = System.currentTimeMillis() - transaction.getTimestamp().getTime();
        int timeInSeconds = (int) timeInMillis / 1000;
        int index = timeInSeconds % 61;
        oneMinuteTransactions.get(index).add(transaction);
        latch = new CountDownLatch(1);
    }

    public void resetIndices() {
        List<Transaction> old = oneMinuteTransactions.remove(oneMinuteTransactions.size()-1);
        oneMinuteTransactions.add(0, new CopyOnWriteArrayList<Transaction>());
        logger.info("purgeOldData, nbr of txns for 61st second {}", old.size());
    }

    public List<List<Transaction>> getOneMinuteTransactions() {
        return this.oneMinuteTransactions;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
        if(latch != null) {
            latch.countDown();
        }
    }

    public Statistics getStatistics() {
        if(latch != null) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.warn("Interrupted!", e);
                Thread.currentThread().interrupt();
            }
        }
        return this.statistics;
    }

    public void deleteTransactions() {
        oneMinuteTransactions.clear();
        initializeList();
    }

    private void initializeList() {
        oneMinuteTransactions = Collections.synchronizedList(new ArrayList<>(61));
        for(int i=0;i<61;++i) {
            oneMinuteTransactions.add(new CopyOnWriteArrayList<>());
        }
    }

}
