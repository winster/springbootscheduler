package com.n26.transactions.service;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import com.n26.transactions.persistence.DataStoreSingleton;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private TransactionService transactionService;

    @Before
    public void initTransaction(){
        statisticsService = new StatisticsService();
        transactionService = new TransactionService();
        DataStoreSingleton.getInstance().deleteTransactions();
    }


    @Test
    public void reloadStatistics() {
        Statistics expectedStatistics = createSingleTransaction(new Date());
        statisticsService.reloadStatistics();
        Statistics actualStatistics = DataStoreSingleton.getInstance().getStatistics();
        assertEquals(expectedStatistics, actualStatistics);
    }

    @Test
    public void statisticsQuality() {
        Statistics expectedStatistics = createMultiTransaction();
        statisticsService.reloadStatistics();
        Statistics actualStatistics = DataStoreSingleton.getInstance().getStatistics();
        assertEquals(expectedStatistics, actualStatistics);
    }

    @Test
    public void noTransaction() {
        DataStoreSingleton.getInstance().deleteTransactions();
        statisticsService.reloadStatistics();
        Statistics actualStatistics = DataStoreSingleton.getInstance().getStatistics();
        Statistics expectedStatistics = zeroStatistics();
        assertEquals(expectedStatistics, actualStatistics);
    }

    @Test
    public void oldData() {
        long timeInMillis = new Date().getTime();
        Date date = new Date(timeInMillis-59000);
        createSingleTransaction(date);
        try {
            Thread.sleep(1000);
            statisticsService.purgeOldData();
            Thread.sleep(1000);
            statisticsService.purgeOldData();
            statisticsService.reloadStatistics();
            Statistics actualStatistics = DataStoreSingleton.getInstance().getStatistics();
            Statistics expectedStatistics = zeroStatistics();
            assertEquals(expectedStatistics, actualStatistics);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void purgeQuality() {
        Statistics statistics1 = DataStoreSingleton.getInstance().getStatistics();
        long timeInMillis = new Date().getTime();
        Date date = new Date(timeInMillis-40000);
        Statistics expectedStatistics = createSingleTransaction(date);
        statisticsService.purgeOldData();
        statisticsService.reloadStatistics();
        Statistics actualStatistics = DataStoreSingleton.getInstance().getStatistics();
        assertEquals(expectedStatistics, actualStatistics);
    }

    private Statistics createSingleTransaction(Date date){
        BigDecimal amount = new BigDecimal(100.896);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimestamp(date);
        new TransactionService().createTransaction(transaction);
        Statistics statistics = new Statistics();
        statistics.setCount(1);
        statistics.setSum(amount);
        statistics.setMaximum(amount);
        statistics.setMinimum(amount);
        statistics.setAverage(amount.setScale(2, RoundingMode.HALF_UP));
        return statistics;
    }

    private Statistics createMultiTransaction(){
        BigDecimal amount1 = new BigDecimal(10.10);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount1);
        transaction.setTimestamp(new Date());
        transactionService.createTransaction(transaction);
        BigDecimal amount2 = new BigDecimal(200.20);
        transaction = new Transaction();
        transaction.setAmount(amount2);
        transaction.setTimestamp(new Date());
        transactionService.createTransaction(transaction);
        BigDecimal amount3 = new BigDecimal(5.5);
        transaction = new Transaction();
        transaction.setAmount(amount3);
        transaction.setTimestamp(new Date());
        transactionService.createTransaction(transaction);
        Statistics statistics = new Statistics();
        statistics.setCount(3);
        BigDecimal sum = amount1.add(amount2).add(amount3);
        statistics.setSum(sum);
        statistics.setMaximum(amount2);
        statistics.setMinimum(amount3);
        statistics.setAverage(sum.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
        return statistics;
    }

    private Statistics zeroStatistics(){
        Statistics statistics = new Statistics();
        BigDecimal amount = new BigDecimal(0);
        statistics.setMinimum(amount);
        statistics.setMaximum(amount);
        statistics.setSum(amount);
        statistics.setAverage(amount);
        statistics.setCount(0);
        return statistics;
    }
}
