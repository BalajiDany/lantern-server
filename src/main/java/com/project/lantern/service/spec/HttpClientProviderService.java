package com.project.lantern.service.spec;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.UUID;

public interface HttpClientProviderService {

    String makeRequest(HttpUriRequest getRequest, UUID clientId);

    String makeRequest(HttpUriRequest getRequest, UUID clientId, boolean resetClient);

    void resetClient(UUID clientId);

}
