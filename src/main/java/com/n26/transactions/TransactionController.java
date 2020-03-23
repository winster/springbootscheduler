package com.n26.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.transactions.model.Statistics;
import com.n26.transactions.model.Transaction;
import com.n26.transactions.persistence.DataStoreSingleton;
import com.n26.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class TransactionController {

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;


    @PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> transactions(@RequestBody Map<String, String> transactionInput) {
        logger.debug("Inside transactions API");
        ResponseEntity<Void> responseEntity = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Transaction transaction = mapper.convertValue(transactionInput, Transaction.class);
            TransactionResult transactionResult = transactionService.createTransaction(transaction);
            switch (transactionResult) {
                case SUCCESS:
                    responseEntity = new ResponseEntity<> (HttpStatus.CREATED);
                    break;
                case OLD:
                    responseEntity = new ResponseEntity<> (HttpStatus.NO_CONTENT);
                    break;
                case FUTURE:
                    responseEntity = new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                    break;
                case FAILED:
                default: responseEntity = new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch(Exception e) { //JSON parse exception
            responseEntity = new ResponseEntity<> (HttpStatus.UNPROCESSABLE_ENTITY);
        }
        logger.info("Create Transaction :: {}",responseEntity.getStatusCode());
        return responseEntity;
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<Void>  transactions() {
        DataStoreSingleton.getInstance().deleteTransactions();
        return new ResponseEntity<> (HttpStatus.NO_CONTENT);
    }

    @GetMapping("/statistics")
    public Statistics statistics() {
        logger.info("inside statistics");
        return DataStoreSingleton.getInstance().getStatistics();
    }

}
