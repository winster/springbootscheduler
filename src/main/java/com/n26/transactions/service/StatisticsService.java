package com.n26.transactions.service;

import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import com.n26.transactions.persistence.DataStoreSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StatisticsService {

    Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    public void purgeOldData(){
        DataStoreSingleton.getInstance().resetIndices();
    }

    public void reloadStatistics(){
        long time1 = System.currentTimeMillis();
        Statistics statistics = calculateStatistics(DataStoreSingleton.getInstance().getOneMinuteTransactions());
        DataStoreSingleton.getInstance().setStatistics(statistics);
        long time2 = System.currentTimeMillis();
        logger.debug("job execution time :: {}",(time2 - time1));
    }

    private Statistics calculateStatistics(List<List<Transaction>> oneMinuteTransactions) {
        Statistics statistics = new Statistics();
        BigDecimal minimum = null;
        BigDecimal maximum = null;
        BigDecimal sum = new BigDecimal(0);
        BigDecimal average = new BigDecimal(0);
        long count = 0;
        for(int i=0; i<60; ++i) {
            CopyOnWriteArrayList<Transaction> oneSecTransactions = (CopyOnWriteArrayList<Transaction>) oneMinuteTransactions.get(i);
            for(Transaction transaction : oneSecTransactions) {
                count++;
                if(minimum == null || minimum.compareTo(transaction.getAmount()) > 0) {
                    minimum = transaction.getAmount();
                }
                if(maximum == null || maximum.compareTo(transaction.getAmount()) < 0) {
                    maximum = transaction.getAmount();
                }

                sum = sum.add(transaction.getAmount());
            }
        }
        if(minimum == null) {
            minimum = new BigDecimal(0);
        }
        if(maximum == null) {
            maximum = new BigDecimal(0);
        }
        statistics.setMinimum(minimum);
        statistics.setMaximum(maximum);
        statistics.setSum(sum);
        statistics.setCount(count);
        if(count > 0) {
            average = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        }
        statistics.setAverage(average);

        return statistics;
    }
}
