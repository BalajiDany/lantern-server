package com.project.eniac.service.spec;

public interface CommonNetworkStatusService {

	String getIpAddress();

	boolean isNetworkAlive();

	boolean isNetworkAliveNow();

	void refresh();

}
