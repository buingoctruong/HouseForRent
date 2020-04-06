package com.vn.tbn.HouseForRent.schedulingtasks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.CrawlData.CrawlConfiguration;
import com.vn.tbn.HouseForRent.CrawlData.CrawlHouseData;
import com.vn.tbn.HouseForRent.Model.House;
import com.vn.tbn.HouseForRent.Repository.HouseDetailRepository;
import com.vn.tbn.HouseForRent.Repository.HouseRepository;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	CrawlHouseData crawlData;
	
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	HouseDetailRepository houseDetailRepository;
	
	@Autowired
	CrawlConfiguration crawConfiguration;
	
	List<House> lstHouse = new ArrayList<House>();
		
	@Scheduled(fixedRate = 100000)
	public void reportCurrentTime() {
		// Before crawling, we need to delete all old data
//		houseDetailRepository.deleteAll();
//		houseRepository.deleteAll();
//		log.info("the crawl is started");
		try {
//			for (int numPage = 1; numPage < 25; numPage++) {
//				lstHouse.addAll(crawlData.callAPI(numPage));
//				
//	            Thread.sleep(2000);
//				
//			}
//			houseRepository.saveAll(lstHouse);
//			log.info("the crawl is finished");
			crawConfiguration.configuration("https://gghouse.co.jp/en/h15/");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error when configuring crawler4j : " + e);
		}
	}
}
