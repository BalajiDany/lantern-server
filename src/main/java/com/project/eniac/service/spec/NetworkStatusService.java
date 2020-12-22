package com.project.eniac.service.spec;

public interface NetworkStatusService {

    String getIpAddress();

    boolean isNetworkAlive();

    boolean isNetworkAliveNow();

    void refresh();

}
