package com.kristex.ac.livedata.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarsInfoBroadcastTask {

	private final CarsInfoWebSocketHandler carsInfoWebSocketHandler;

	@Scheduled(fixedRate = 10) // every 100ms
	public void broadcast() {
		carsInfoWebSocketHandler.broadcastCars();
	}
}
