package com.vn.tbn.HouseForRent.schedulingtasks;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.CrawlData.CrawlConfiguration;
import com.vn.tbn.HouseForRent.CrawlData.CrawlHouseData;
import com.vn.tbn.HouseForRent.Repository.HouseDetailRepository;
import com.vn.tbn.HouseForRent.Repository.HouseRepository;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	private static final String pathImage = "src/main/resources/static/image/";
	
	@Autowired
	CrawlHouseData crawlData;
	
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	HouseDetailRepository houseDetailRepository;
	
	@Autowired
	CrawlConfiguration crawConfiguration;
			
	@Scheduled(fixedRate = 100000)
	public void reportCurrentTime() {
		log.info("the crawl is started");
		try {
			// Before crawling, we need to delete all old data
			houseDetailRepository.deleteAll();
			houseRepository.deleteAll();
			
			// deleting the folder holds house's image
			File dir = new File(pathImage);
			FileUtils.deleteDirectory(dir);
			
			// creating again the folder has house's image
			dir.mkdir();
			
			log.info("the crawl is started");
			for (int numPage = 1; numPage < 25; numPage++) {
				crawlData.callAPI(numPage);
				
				// make wait 2 seconds between pages
	            Thread.sleep(2000);		
			}
			log.info("the crawl is finished");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error when configuring crawler4j : " + e);
		}
	}
}
