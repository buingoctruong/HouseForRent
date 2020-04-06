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
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.Model.House;

@Component
public class CrawlHouseData {
			
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
		html = html.replace("\\n", "").replace("    ", "")
				.replace("  ", "").replace("\\", "").replace("{\"properties\":\"", "");
		
		int index = html.indexOf(">\"");
		html = html.replace(html.substring(index+1, html.length()), "");
		return html;
	}
	
	/*
	 * After converting to HTML format
	 * We start to extract data from HTML
	 */
	public List<House> ExtractData(String html) {
		Document doc = Jsoup.parse(html);
		Elements listHouse = doc.select("a");
		List<House> lstHouse = new ArrayList<House>();
		for (Element item : listHouse) {
			House house = new House();
			
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
			
			lstHouse.add(house);			
		}
		return lstHouse;
	}
	
	public String getHouseCode(String href) {
		int lastIndex = href.lastIndexOf("/");
		int nearLastIndex = href.lastIndexOf("/", lastIndex - 1);
		
		return href.substring(nearLastIndex+1, lastIndex);
	}
}
