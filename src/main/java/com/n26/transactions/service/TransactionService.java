package com.n26.transactions.service;

import com.n26.transactions.model.Transaction;
import com.n26.transactions.persistence.DataStoreSingleton;
import com.n26.transactions.TransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionResult createTransaction(Transaction transaction) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String transactionTime = sdf.format(transaction.getTimestamp());
        String currentTime = sdf.format(new Date());
        logger.info("inside createTransaction : transaction time {}",transactionTime);
        logger.info("inside createTransaction : current time {}",currentTime);
        TransactionResult transactionResult;
        long timeInMillis = System.currentTimeMillis() - transaction.getTimestamp().getTime();
        if (timeInMillis > 60000) {
            transactionResult = TransactionResult.OLD;
        } else if(timeInMillis < 0) {
            transactionResult = TransactionResult.FUTURE;
        } else {
            DataStoreSingleton.getInstance().store(transaction);
            transactionResult = TransactionResult.SUCCESS;
        }
        return transactionResult;
    }

}
