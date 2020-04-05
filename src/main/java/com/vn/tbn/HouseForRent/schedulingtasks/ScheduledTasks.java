package com.vn.tbn.HouseForRent.schedulingtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.CrawlData.CrawlData;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	CrawlData crawlData;
		
	@Scheduled(fixedRate = 100000)
	public void reportCurrentTime() {
		try {
			crawlData.callAPI(2);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error when configuring crawler4j : " + e);
		}
	}
}
