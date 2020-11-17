package com.project.eniac;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.engine.google.GoogleGeneralSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.utils.UserAgent;

@SpringBootApplication
public class EniacApplication {

	public static void main(String... args) throws ClientProtocolException, IOException, URISyntaxException {
//		SpringApplication.run(EniacApplication.class, args);

		GeneralSearchEngine searchEngine = new GoogleGeneralSearchEngine();
		MainSearchEntity searchEntity = new MainSearchEntity();

		searchEntity.setQuery("windows 10");
		searchEntity.setLanguage("en");
		searchEntity.setLocation("US");

		HttpGet getRequest = searchEngine.getRequest(searchEntity);
		getRequest.addHeader(RequestHeaders.KEY_USER_AGENT, UserAgent.getRandomUserAgent());

		String response = makeRequest(getRequest);
		
		System.out.println(response);
		
		GeneralSearchResultEntity responseEntity = searchEngine.getResponse(response);
		
		System.out.println(responseEntity.getEngineName());
		System.out.println(responseEntity.getEngineCategory());
	}

	private static String makeRequest(HttpGet httpGet) {
		
		System.out.println(" ======================================================= ");
		System.out.println(httpGet.toString());
		for(Header header : httpGet.getAllHeaders()) {
			System.out.println(header.getName() + " : " + header.getValue());
		}
		System.out.println(" ======================================================= ");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (IOException e) {
			return "";
		}	
	}
}
