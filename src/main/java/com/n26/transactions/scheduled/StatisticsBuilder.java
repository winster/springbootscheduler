package com.n26.transactions.scheduled;

import com.n26.transactions.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class StatisticsBuilder {

    Logger logger = LoggerFactory.getLogger(StatisticsBuilder.class);

    @Autowired
    private StatisticsService statisticsService;

    @Scheduled(fixedRate = 1000)
    public void purgeOldData(){
        logger.debug("task::purgeOldData");
        statisticsService.purgeOldData();
    }

    @Scheduled(fixedRate = 1)
    public void refreshStatistics(){
        logger.debug("task::refreshStatistics");
        statisticsService.reloadStatistics();
    }

}
