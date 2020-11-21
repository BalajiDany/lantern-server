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

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.engine.TorrentSearchEngine;
import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.service.CommonLanguageServiceImpl;
import com.project.eniac.service.CommonLocationServiceImpl;
import com.project.eniac.types.EngineResultType;
import com.project.eniac.utils.UserAgent;

@SpringBootApplication
public class EniacApplication {

	// Run this command
	// mvn clean install eclipse:clean eclipse:eclipse -DdownloadJavadocs=true

	public static void main(String... args) throws ClientProtocolException, IOException, URISyntaxException {
//		SpringApplication.run(EniacApplication.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(EniacApplication.class)
				.web(WebApplicationType.NONE).run(args);

		 Map<String, TorrentSearchEngine> engines = context.getBeansOfType(TorrentSearchEngine.class);
		CommonLanguageServiceImpl lan = new CommonLanguageServiceImpl();
		CommonLocationServiceImpl loc = new CommonLocationServiceImpl();

		long startTime = System.currentTimeMillis();

//		Arrays.asList(
//				"ghhlfghjftutyityjfghjdfgjdtryuertjhcvbsdrfgher"
//			)
//			.stream()
//			.parallel().forEach(query ->  performanceSearch(engines, lan, loc, query));
//		performanceSearch(engines, lan, loc, "shin chan");
//		performanceSearch(engines, lan, loc, "windows 10");
//		performanceSearch(engines, lan, loc, "capital");
		performanceSearch(engines, lan, loc, "Mookuthi Amman");
//		performanceSearch(engines, lan, loc, "bowel company");
//		performanceSearch(engines, lan, loc, "cartoon network");

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.println("OverAll Grand Run Time : " + runTime);

	}

	public static void performanceSearch(Map<String, TorrentSearchEngine> engines, CommonLanguageServiceImpl lan, CommonLocationServiceImpl loc, String searchText) {

		long startTime = System.currentTimeMillis();

		performSearchOne(engines, lan, loc, searchText);

		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.println("OverAll Time for " + searchText + " : " + runTime);
	}

	public static void performSearchOne(Map<String, TorrentSearchEngine> engines, CommonLanguageServiceImpl lan, CommonLocationServiceImpl loc, String searchText) {

		MainSearchEntity searchEntity = new MainSearchEntity();

		searchEntity.setQuery(searchText);
		searchEntity.setLanguage(lan.getSupportedLanguage("en"));
		searchEntity.setLocation(loc.getSupportedLocation("EN"));

		String userAgent = UserAgent.getRandomUserAgent();

//		for(int count = 0; count < 4; count++)
		for (Entry<String, TorrentSearchEngine> engineEntry : engines.entrySet()) {

			TorrentSearchEngine engine = engineEntry.getValue();
			HttpGet request = engine.getRequest(searchEntity);

			// Common Configuration
			request.addHeader(RequestHeaders.KEY_USER_AGENT, userAgent);


//			long startTime = System.currentTimeMillis();

			String response = makeRequest(request);

//			long stopTime = System.currentTimeMillis();
//			long runTime = stopTime - startTime;
//			System.out.println("Request Time : " + runTime);


			SearchResultEntity<TorrentSearchResultEntity> searchResultEntity = engine.getResponse(response);

			EngineResultType resultType = searchResultEntity.getEngineResultType();

			if (resultType == EngineResultType.FOUND_SEARCH_RESULT) {
				List<TorrentSearchResultEntity> resultEntity = searchResultEntity.getSearchResult();

				System.out.println(" =============== " + searchResultEntity.getEngineName() + " - " + searchResultEntity.getEngineType() + " =============== ");

				System.out.println("Total Results : " + resultEntity.size());

				resultEntity.forEach(result -> {
					System.out.println("Torrent Name\t:" + result.getTorrentName());
					System.out.println("Torrent Size\t:" + result.getTorrentSize());
					System.out.println("Seeders\t:" + result.getSeeders());
					System.out.println("Leechers\t:" + result.getLeechers());
				});

			} else {
				System.out.println("No Result Reason : " + resultType);
			}


		}
	}

	private static String makeRequest(HttpGet httpGet) {

		System.out.println(" ======================================================= ");
		System.out.println(httpGet.toString());
		for (Header header : httpGet.getAllHeaders()) {
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
