package com.vn.tbn.HouseForRent.CrawlData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.Model.House;
import com.vn.tbn.HouseForRent.Repository.HouseRepository;

@Component
public class CrawlHouseData {
	
	// API of the website
	private static final String api = "https://gghouse.co.jp/en/search/result/";
	
	@Autowired
	HouseRepository houseRepository;
	
	@Autowired
	CrawlConfiguration crawlConfiguration;
			
	/*
	 * get house's data from HTML and save in tables
	 * Because pages of the website use js to render data, so I have to call API and get data
	 * 
	 * numPage  number of pages
	 * */
	public void callAPI(int numPage) {
		InputStream stream = null;
		HttpURLConnection postConnection = null;
		
		try {
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
	        	        
	        ExtractData(result);
	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (postConnection != null) {
            	postConnection.disconnect();
            }
        }
	}
	
	/* firstly HTML that we receive is String.
	 * so we need to clean, normalize and convert it to HTML format
	 * After that using jsoup extract 
	 */
	public String CleanHTML(String html) {
		html = html.replace("\\n", "").replace("    ", "")
				.replace("  ", "").replace("\\", "").replace("{\"properties\":\"", "");
		
		int index = html.indexOf(">\"");
		html = html.replace(html.substring(index+1, html.length()), "");
		return html;
	}
	
	/*
	 * After converting to HTML format
	 * We start to extract data from HTML
	 * We'll get house's Code, Url, Name, Price, Status and Type
	 */
	public void ExtractData(String html) throws Exception {
		Document doc = Jsoup.parse(html);
		Elements listHouse = doc.select("a");
		for (Element item : listHouse) {
			House house = new House();
			
			// the URL of the house
			String href = item.attr("href");
			
			house.setCode(getHouseCode(href));
			
			house.setUrl(href);
			house.setName(item.select("dt").first().text());
			house.setPrice(item.select("p").text());
			
			Elements statusType = item.select("span");
			
			house.setStatus(statusType.first().text());
			
			if (statusType.size() > 1) {
				house.setType(1);
			} else {
				house.setType(0);
			}
			
			houseRepository.save(house);
			
			// After saving house's data into House table, we're gonna save its detail data into House_Detail
			crawlConfiguration.configuration(href);
		}
	}
	
	/*
	 * from house's Url, we're gonna get house's Code
	 */
	public String getHouseCode(String href) {
		int lastIndex = href.lastIndexOf("/");
		int nearLastIndex = href.lastIndexOf("/", lastIndex - 1);
		
		return href.substring(nearLastIndex+1, lastIndex);
	}
}
