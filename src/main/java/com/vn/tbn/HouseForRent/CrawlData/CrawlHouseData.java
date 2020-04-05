package com.vn.tbn.HouseForRent.CrawlData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.Model.House;

@Component
public class CrawlHouseData {
	
	private static final Logger log = LoggerFactory.getLogger(CrawlHouseData.class);
	
	private static final String api = "https://gghouse.co.jp/en/search/result/";
	
	/*
	 * get Data from HTML and return a list of the house of each website's page
	 * 
	 * numPage  number of pages
	 * return   list
	 * */
	public List<House> callAPI(int numPage) {
		InputStream stream = null;
		HttpURLConnection postConnection = null;
		
		try {
			log.info("Start calling api of page " + numPage);
			URL url = new URL(api);
		    postConnection = (HttpURLConnection) url.openConnection();
		    postConnection.setRequestMethod("POST");
		    postConnection.setDoOutput(true);
		    
		    String data = URLEncoder.encode("path", "UTF-8")
	                + "=" + URLEncoder.encode("en/search/result.html", "UTF-8");

	        data += "&" + URLEncoder.encode("rent_min", "UTF-8") + "=" + 35000;
	        
	        data += "&" + URLEncoder.encode("rent_max", "UTF-8") + "=" + 100000;
	        
	        data += "&" + URLEncoder.encode("p", "UTF-8") + "=" + numPage;
	        
	        data += "&" + URLEncoder.encode("gender", "UTF-8") + "=";
	        
	        postConnection.connect();
		    
	        OutputStreamWriter wr = new OutputStreamWriter(postConnection.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        wr.close();
	        
	        stream = postConnection.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
	        String result = CleanHTML(reader.readLine());
	        	        
	        return ExtractData(result);
	        
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (postConnection != null) {
            	postConnection.disconnect();
            }
        }
		return null;
	}
	
	/* Because firstly, HTML that we got is String.
	 * so we need to clean and normalize before using jsoup extract 
	 */
	public String CleanHTML(String html) {
		log.info("Start converting to HTML format");
		html = html.replace("\\n", "").replace("    ", "")
				.replace("  ", "").replace("\\", "").replace("{\"properties\":\"", "");
		
		int index = html.indexOf(">\"");
		html = html.replace(html.substring(index+1, html.length()), "");
		log.info("End converting to HTML format");
		return html;
	}
	
	/*
	 * After converting to HTML format
	 * We start to extract data from HTML
	 */
	public List<House> ExtractData(String html) {
		log.info("Start extracting to HTML format");
		Document doc = Jsoup.parse(html);
		Elements listHouse = doc.select("a");
		List<House> lstHouse = new ArrayList<House>();
		for (Element item : listHouse) {
			House house = new House();
			house.setName(item.select("dt").first().text());
			house.setPrice(item.select("p").text());
			house.setStatus(item.select("span").first().text());
			house.setUrl(item.attr("href"));
			lstHouse.add(house);
		}
		log.info("End extracting to HTML format");
		return lstHouse;
	}
}
