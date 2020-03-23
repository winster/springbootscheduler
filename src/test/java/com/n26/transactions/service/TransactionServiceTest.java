package com.n26.transactions.service;

import com.n26.transactions.TransactionResult;
import com.n26.transactions.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @Before
    public void initTransaction(){
        transactionService = new TransactionService();
    }

    @Test
    public void successTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(100.896));
        transaction.setTimestamp(new Date());
        assertEquals(TransactionResult.SUCCESS, transactionService.createTransaction(transaction));
    }

    @Test
    public void futureTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(100.896));
        Date today = new Date();
        long timeInMillis = today.getTime();
        Date future = new Date(timeInMillis + 10000);
        transaction.setTimestamp(future);
        assertEquals(TransactionResult.FUTURE, transactionService.createTransaction(transaction));
    }

    @Test
    public void oldTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(100.896));
        Date today = new Date();
        long timeInMillis = today.getTime();
        Date old = new Date(timeInMillis - 100000);
        transaction.setTimestamp(old);
        assertEquals(TransactionResult.OLD, transactionService.createTransaction(transaction));
    }
}
