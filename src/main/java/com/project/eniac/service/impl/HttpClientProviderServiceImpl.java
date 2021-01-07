package com.project.eniac.service.impl;

import com.project.eniac.service.spec.HttpClientProviderService;
import com.project.eniac.utils.IOUtils;
import com.project.eniac.utils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.*;

@Slf4j
public class HttpClientProviderServiceImpl implements HttpClientProviderService {

    private final Map<UUID, CloseableHttpClient> httpClientMap = new HashMap<>();

    @Value("${project.eniac.configuration.network.connection.timeout}")
    private int networkTimeOut;

    @Override
    public String makeRequest(HttpUriRequest request, UUID clientId) {
        return this.makeRequest(request, clientId, false);
    }

    @Override
    public String makeRequest(HttpUriRequest request, UUID clientId, boolean resetClient) {
        if (resetClient) this.resetClient(clientId);

        // Perform Request
        CloseableHttpClient httpclient = this.getHttpClient(clientId);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity);
        } catch (IOException | IllegalStateException exception) {
            IOUtils.closeQuietly(response);
            return StringUtils.EMPTY;
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    @Override
    public void resetClient(UUID clientId) {
        CloseableHttpClient previousClient = httpClientMap.get(clientId);
        IOUtils.closeQuietly(previousClient);

        CloseableHttpClient newClient = this.createHttpClient();
        httpClientMap.put(clientId, newClient);
    }

    private CloseableHttpClient getHttpClient(UUID clientId) {
        if (httpClientMap.containsKey(clientId)) return httpClientMap.get(clientId);

        CloseableHttpClient httpClient = this.createHttpClient();
        httpClientMap.put(clientId, httpClient);

        return httpClient;
    }

    private CloseableHttpClient createHttpClient() {

        RequestConfig configuration = RequestConfig.custom()
                .setConnectTimeout(networkTimeOut)
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();

        String randomUserAgent = UserAgent.pickRandomUserAgent();
        Header userAgent = new BasicHeader(HttpHeaders.USER_AGENT, randomUserAgent);

        List<Header> defaultHeaderList = Collections.singletonList(userAgent);
        CookieStore cookieStore = new BasicCookieStore();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(configuration)
                .setDefaultCookieStore(cookieStore)
                .setDefaultHeaders(defaultHeaderList)
                .build();
    }

}
