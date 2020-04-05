package com.vn.tbn.HouseForRent.schedulingtasks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
	
	List<House> lstHouse = new ArrayList<House>();
		
	@Scheduled(fixedRate = 100000)
	public void reportCurrentTime() {
		// Before crawling, we need to delete all old data
		houseDetailRepository.deleteAll();
		houseRepository.deleteAll();
		// Start crawling
		try {
			for (int numPage = 1; numPage < 25; numPage++) {
				lstHouse.addAll(crawlData.callAPI(numPage));
			}
			houseRepository.saveAll(lstHouse);
			log.info("+++++++++++++++++++++++++++=");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error when configuring crawler4j : " + e);
		}
	}
}
