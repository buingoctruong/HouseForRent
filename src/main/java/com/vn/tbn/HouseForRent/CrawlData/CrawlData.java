package com.vn.tbn.HouseForRent.CrawlData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrawlData {
private static final Logger log = LoggerFactory.getLogger(CrawlData.class);
	
	private static final String api = "https://gghouse.co.jp/en/search/result/";
	
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
	        String result = reader.readLine().replace("\\n", "").replace("    ", "")
	        		.replace("  ", "").replace("\\", "");
	        
	        ExtractData(result);
	        
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (postConnection != null) {
            	postConnection.disconnect();
            }
        }
	}
	
	public void ExtractData(String html) {
		Document doc = Jsoup.parse(html);
		Element link = doc.select("a").first();
		log.info("====> : " + link);
	}
}
