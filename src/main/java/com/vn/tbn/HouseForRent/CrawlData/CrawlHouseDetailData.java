package com.vn.tbn.HouseForRent.CrawlData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.vn.tbn.HouseForRent.Model.HouseDetail;

/*
 *  We're gonna use crawl detail data of each house
 */

@Component
public class CrawlHouseDetailData {		
	private static final String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
	
	public HouseDetail callHouseURL(String url) {
		HttpURLConnection getConnection = null;
		try {
			URL houseRequest = new URL(url);
		    String readLine = null;
		    getConnection = (HttpURLConnection) houseRequest.openConnection();
		    getConnection.setRequestMethod("GET");
		    
		    BufferedReader reader = new BufferedReader(
		            new InputStreamReader(getConnection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = reader.readLine()) != null) {
	            response.append(readLine);
	        }
	        reader.close();
	        
	        return ExtractData(response.toString());
	        
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (getConnection != null) {
				getConnection.disconnect();
            }
		}
		return null;
	}
	
	public HouseDetail ExtractData(String html) throws Exception {
		Document doc = Jsoup.parse(html);
		
		// get facilities
        Elements facilities = doc.getElementsByClass("ico-list");
        StringBuilder share_facility = getFacility(facilities.get(0));
        StringBuilder room_facility = getFacility(facilities.get(1));
        
        // get description
        String descriptions = doc.getElementsByClass("text-box container").text();
        descriptions = descriptions.replaceAll(characterFilter,"");
        	        	        
        HouseDetail houseDetail = new HouseDetail();
        houseDetail.setDescription(descriptions);
        houseDetail.setRoomFacility(room_facility.toString());
        houseDetail.setShareFacility(share_facility.toString());
        
        Elements imgs = doc.getElementById("thumb").select("a");
        houseDetail.setLinkImage(getLinks(imgs).toString());
        
        return houseDetail;
	}
	
	/*
	 * get all facilities
	 */
	public StringBuilder getFacility(Element facilities) {
		StringBuilder facility = new StringBuilder();
		String prefix = "";
		for (Element temp : facilities.select("span")) {
			facility.append(prefix);
			prefix = ",";
			facility.append(temp.text());
        }
		return facility;
	}
	
	/*
	 * get all links
	 */
	public StringBuilder getLinks(Elements imgs) {
		StringBuilder links = new StringBuilder();
		String prefix = "";
		for (Element item : imgs) {
			links.append(prefix);
			prefix = "|";
			links.append(item.attr("href"));
        }
		return links;
	}
}