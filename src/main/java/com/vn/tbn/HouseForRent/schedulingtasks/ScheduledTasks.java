package com.vn.tbn.HouseForRent.schedulingtasks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.CrawlData.CrawlHouseData;
import com.vn.tbn.HouseForRent.CrawlData.CrawlHouseDetailData;
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
	CrawlHouseDetailData crawlHouseDetailData;
			
	@Scheduled(fixedRate = 100000000)
	public void reportCurrentTime() {
		log.info("the crawl is started");
		try {
			// Before crawling, we need to delete all old data
			houseDetailRepository.deleteAll();
			houseRepository.deleteAll();
			
			log.info("the crawl is started");
			List<House> lstHouses = new ArrayList<House>();
			for (int numPage = 1; numPage < 25; numPage++) {
				
				List<House> lstItems = crawlData.callAPI(numPage);
				
				if (null != lstItems) {
					lstHouses.addAll(lstItems);
				}				
				// make wait 2 seconds between pages
	            Thread.sleep(2000);		
			}
			houseRepository.saveAll(lstHouses);
			log.info("the crawl is finished");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error when configuring crawler4j : " + e);
		}
	}
}
