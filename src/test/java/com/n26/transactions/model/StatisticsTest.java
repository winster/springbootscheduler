package com.n26.transactions.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    @Test
    public void equalObject() {
        Statistics statistics1 = new Statistics();
        BigDecimal amount1 = new BigDecimal(100.89);
        statistics1.setMinimum(amount1);
        statistics1.setMaximum(amount1);
        statistics1.setSum(amount1);
        statistics1.setAverage(amount1);
        statistics1.setCount(1);

        Statistics statistics2 = new Statistics();
        BigDecimal amount2 = new BigDecimal(100.89);
        statistics2.setMinimum(amount2);
        statistics2.setMaximum(amount2);
        statistics2.setSum(amount2);
        statistics2.setAverage(amount2);
        statistics2.setCount(1);

        assertEquals(true, statistics1.equals(statistics2));
    }
}
