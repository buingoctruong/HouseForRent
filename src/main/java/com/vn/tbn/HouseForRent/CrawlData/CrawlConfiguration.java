package com.vn.tbn.HouseForRent.CrawlData;

import java.io.File;

import org.springframework.stereotype.Component;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Component
public class CrawlConfiguration {
	public void configuration(String url) throws Exception {
		File crawlStorage = new File("src/test/resources/crawler");
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
		
		int numCrawlers = 1;
		 
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer= new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
				
		controller.addSeed(url);
		 
		CrawlController.WebCrawlerFactory<CrawlHouseDetailData> factory = CrawlHouseDetailData::new;
		 
		controller.start(factory, numCrawlers);
	}
}
