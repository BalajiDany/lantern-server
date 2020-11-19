package com.project.eniac;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.GeneralSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.service.CommonLanguageServiceImpl;
import com.project.eniac.service.CommonLocationServiceImpl;
import com.project.eniac.utils.UserAgent;

@SpringBootApplication
public class EniacApplication {

	public static void main(String... args) throws ClientProtocolException, IOException, URISyntaxException {
//		SpringApplication.run(EniacApplication.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(EniacApplication.class)
				.web(WebApplicationType.NONE).run(args);

		Map<String, GeneralSearchEngine> engines = context.getBeansOfType(GeneralSearchEngine.class);
		CommonLanguageServiceImpl lan = new CommonLanguageServiceImpl();
		CommonLocationServiceImpl loc = new CommonLocationServiceImpl();

		MainSearchEntity searchEntity = new MainSearchEntity();

		searchEntity.setQuery("shin chan");
		searchEntity.setLanguage(lan.getSupportedLanguage("en"));
		searchEntity.setLocation(loc.getSupportedLocation("DE"));

		String userAgent = UserAgent.getRandomUserAgent();

//		for(int count = 0; count < 4; count++)
		for (Entry<String, GeneralSearchEngine> engineEntry : engines.entrySet()) {

			GeneralSearchEngine engine = engineEntry.getValue();
			HttpGet request = engine.getRequest(searchEntity);

			// Common Configuration
			request.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);

			String response = makeRequest(request);
			List<GeneralSearchResultEntity> resultEntity = engine.getResponse(response);

			System.out.println(" =============== " + engine.getEngineName() + " - " + engine.getEngineCategory() + " =============== ");

			System.out.println("Total Results : " + resultEntity.size());

			resultEntity.forEach(result -> {
				System.out.println("Title\t:" + result.getTitle());
				System.out.println("URL\t:" + result.getUrl());
				System.out.println("Content\t:" + result.getContent());
				;
			});

		}
	}

	private static String makeRequest(HttpGet httpGet) {

		System.out.println(" ======================================================= ");
		System.out.println(httpGet.toString());
		for (Header header : httpGet.getAllHeaders()) {
			System.out.println(header.getName() + " : " + header.getValue());
		}
		System.out.println(" ======================================================= ");

		long startTime = System.currentTimeMillis();

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		try {
			response = httpclient.execute(httpGet);

			long stopTime = System.currentTimeMillis();
			long runTime = stopTime - startTime;
			System.out.println("Run time: " + runTime);
			HttpEntity entity = response.getEntity();

			return EntityUtils.toString(entity);
		} catch (IOException e) {
			return "";
		}
	}
}
