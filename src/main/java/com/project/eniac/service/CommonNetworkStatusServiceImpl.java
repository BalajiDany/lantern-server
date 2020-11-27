package com.project.eniac.service;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.service.spec.CommonNetworkStatusService;
import com.project.eniac.service.spec.HttpClientProviderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommonNetworkStatusServiceImpl implements CommonNetworkStatusService {

	private final HttpClientProviderService httpClientProviderService;

	private String currentIp;

	private boolean isNetworkAlive;

	@PostConstruct
	public void init() {
		this.isNetworkAliveNow();
	}

	@Override
	public String getIpAddress() {
		if (ObjectUtils.isEmpty(currentIp)) isNetworkAliveNow();
		return currentIp;
	}

	@Override
	public boolean isNetworkAlive() {
		return isNetworkAlive;
	}

	@Override
	public boolean isNetworkAliveNow() {

		String url = "https://api.ipify.org/";

		try {

			URI uri = new URIBuilder(url).build();

			HttpGet request = new HttpGet(uri);
			request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, "en-US,en;q=0.9");
			request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

			String response = httpClientProviderService.makeRequest(request, "utils", true);

			isNetworkAlive = StringUtils.isNotBlank(response);
			currentIp = isNetworkAlive ? response : "NA";

			if (isNetworkAlive) log.info("Network alive, Public IP : {}", currentIp);
			else log.warn("Network down");
		} catch (URISyntaxException exception) {
			log.error("Exception on Creating URL : {}", url);
		}
		return isNetworkAlive;
	}

	@Async
	@Override
	@Scheduled(fixedRateString="${project.eniac.configuration.network.check.rate}")
	public void refresh() {
		this.isNetworkAliveNow();
	}

}
