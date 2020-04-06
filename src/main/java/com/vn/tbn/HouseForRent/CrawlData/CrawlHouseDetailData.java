package com.vn.tbn.HouseForRent.CrawlData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlHouseDetailData extends WebCrawler {
	private static final Logger log = LoggerFactory.getLogger(CrawlHouseDetailData.class);
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
        return false;
	}
	
	@Override
	public void visit(Page page) {
	    String url = page.getWebURL().getURL();
	    log.info("URL: " + url);
	 
	    if (page.getParseData() instanceof HtmlParseData) {
	        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	        String html = htmlParseData.getHtml();
	        Document doc = Jsoup.parse(html);
	        
	        Elements items = doc.getElementsByClass("text-box container");
	        log.info("---------------------------------------------------------");
	        for (Element item : items) {
	        	log.info("=====> : " + item.text());
	        }
	        
	        log.info("---------------------------------------------------------");
	 
	    }
	}
}
