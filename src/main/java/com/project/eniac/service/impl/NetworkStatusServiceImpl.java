package com.project.eniac.service.impl;

import com.project.eniac.constant.RequestHeaders;
import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.service.spec.NetworkStatusService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class NetworkStatusServiceImpl implements NetworkStatusService {

    private final HttpClientProviderService httpClientProviderService;

    private final UUID engineId = UUID.fromString("46677eaf-bb61-4061-af9e-ce29fb6fcc7a");

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
    @SneakyThrows
    public boolean isNetworkAliveNow() {

        URI uri = new URIBuilder()
                .setScheme("https").setHost("api.ipify.org")
                .build();
        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, RequestHeaders.VALUE_ACCEPT_LANGUAGE);
        request.addHeader(HttpHeaders.ACCEPT, RequestHeaders.VALUE_ACCEPT_HTML);

        String response = httpClientProviderService.makeRequest(request, engineId, true);
        isNetworkAlive = StringUtils.isNotBlank(response);
        currentIp = isNetworkAlive ? response : "NA";

        if (isNetworkAlive) log.debug("Network alive, Public IP : {}", currentIp);
        else log.warn("Network down, Please Check your internet connection");

        return isNetworkAlive;
    }

    @Override
    @Async("networkAgentTaskExecutor")
    @Scheduled(fixedRateString = "${project.eniac.configuration.network.check.rate}")
    public void refresh() {
        this.isNetworkAliveNow();
    }

}
