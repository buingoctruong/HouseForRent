package com.vn.tbn.HouseForRent.CrawlData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vn.tbn.HouseForRent.Model.HouseDetail;
import com.vn.tbn.HouseForRent.Repository.HouseDetailRepository;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 *  We're gonna use Crawler4j to crawl detail data of each house
 */

public class CrawlHouseDetailData extends WebCrawler {
	private static final Logger log = LoggerFactory.getLogger(CrawlHouseDetailData.class);
	
	private static final String pathImage = "src/main/resources/static/image/";
	
	private static final String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
	
	@Autowired
	HouseDetailRepository houseDetailRepository;
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
        return false;
	}
	
	@Override
	public void visit(Page page) {
		// URL of the house
	    String url = page.getWebURL().getURL();
	    log.info("URL: " + url);
	 
	    if (page.getParseData() instanceof HtmlParseData) {
	        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	        String html = htmlParseData.getHtml();
	        Document doc = Jsoup.parse(html);
	        
	        // get facilities
	        Elements facilities = doc.getElementsByClass("ico-list");
	        StringBuilder share_facility = getFacility(facilities.get(0));
	        StringBuilder room_facility = getFacility(facilities.get(1));
	        
	        // get House Code from url
	        String houseCode = getCode(url);
	        
	        // get description
	        String descriptions = doc.getElementsByClass("text-box container").text();
	        descriptions = descriptions.replaceAll(characterFilter,"");
	        	        
	        HouseDetail houseDetail = new HouseDetail();
	        houseDetail.setHouseCode(houseCode);
	        houseDetail.setDescription(descriptions);
	        houseDetail.setRoomFacility(room_facility.toString());
	        houseDetail.setShareFacility(share_facility.toString());
	        
	        log.info("Id " + houseDetail.getId());
	        
	        try {
	        	houseDetailRepository.save(houseDetail);
			} catch (Exception e) {
				// TODO: handle exception
				log.info("-------------------");
				log.info("====> " + e);
				log.info("+++++++++++++++++++");
			}
	        
	        // After saving house's detail data into DB, we're gonna download all house's image
	        Elements imgs = doc.getElementById("thumb").select("a");
	        
	        File dir = new File(pathImage + houseCode);
			dir.mkdir();
	        
	        for (Element item : imgs) {	        	
	        	try {
	        		downloadImage(item.attr("href"), houseCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
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
	 * from url, get the code
	 */
	public String getCode(String href) {
		int lastIndex = href.lastIndexOf("/");
		int nearLastIndex = href.lastIndexOf("/", lastIndex - 1);
		return href.substring(nearLastIndex+1, lastIndex);
	}
	
	/*
	 * use OkHttp to download image asynchronously
	 */
	public void downloadImage(String imageUrl, String houseCode) throws Exception {
		String imageCode = getCode(imageUrl);
	    OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder().url(imageUrl).build();
	    client.newCall(request).enqueue(new Callback() {
	        public void onFailure(Call call, IOException e) {
	            e.printStackTrace();
	        }
	 
	        public void onResponse(Call call, Response response) throws IOException {
	            if (!response.isSuccessful()) {
	                throw new IOException("Failed to download image: " + response);
	            }
	            FileOutputStream fos = new FileOutputStream(pathImage + houseCode + "/" + imageCode + ".jpg");
	            fos.write(response.body().bytes());
	            fos.close();
	        }
	    });
	}
}
