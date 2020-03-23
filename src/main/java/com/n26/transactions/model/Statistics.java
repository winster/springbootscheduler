package com.n26.transactions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.transactions.util.BigDecimalSerializer;

import java.math.BigDecimal;

@JsonPropertyOrder({"sum","avg","max","min","count"})
public class Statistics {

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal sum;//the total sum of transaction value in the last 60 seconds

    @JsonProperty("avg")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal average;//the average amount of transaction value in the last 60 seconds

    @JsonProperty("max")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal maximum;//single highest transaction value in the last 60 seconds

    @JsonProperty("min")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal minimum;//single lowest transaction value in the last 60 seconds

    private long count;//the total number of transactions that happened in the last 60 seconds

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Statistics stat = (Statistics) obj;
        return count==stat.getCount() && sum.equals(stat.getSum())
                && maximum.equals(stat.getMaximum()) && minimum.equals(stat.getMinimum())
                && average.equals(stat.getAverage());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
