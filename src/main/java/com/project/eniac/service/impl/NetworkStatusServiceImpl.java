package com.project.eniac.service.impl;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.service.spec.NetworkStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
public class NetworkStatusServiceImpl implements NetworkStatusService {

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
            request.addHeader(RequestHeaders.KEY_ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
            request.addHeader(RequestHeaders.KEY_ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

            String response = httpClientProviderService.makeRequest(request, "utils", true);
            isNetworkAlive = StringUtils.isNotBlank(response);
            currentIp = isNetworkAlive ? response : "NA";

            if (isNetworkAlive) log.info("Network alive, Public IP : {}", currentIp);
            else log.warn("Network down, Please Check your internet connection");

        } catch (URISyntaxException exception) {
            log.error("Exception on Creating URL : {}", url);
        }
        return isNetworkAlive;
    }

    @Override
    @Async("networkAgentTaskExecutor")
    @Scheduled(fixedRateString = "${project.eniac.configuration.network.check.rate}")
    public void refresh() {
        this.isNetworkAliveNow();
    }

}
