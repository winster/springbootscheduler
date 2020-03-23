package com.n26.transactions.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.transactions.model.Statistics;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BigDecimalSerializerTest {

    @Test
    public void formatTest() throws Exception {
        Statistics statistics = new Statistics();
        statistics.setSum(new BigDecimal("10000.8"));
        statistics.setAverage((new BigDecimal("100.538978")));
        statistics.setMaximum(new BigDecimal("20000.49"));
        statistics.setMinimum(new BigDecimal("10.345"));
        statistics.setCount(Long.valueOf(10));


        ObjectMapper mapper = new ObjectMapper();
        assertEquals("{\"sum\":\"10000.80\",\"avg\":\"100.54\",\"max\":\"20000.49\",\"min\":\"10.35\",\"count\":10}", mapper.writeValueAsString(statistics));
    }
}
