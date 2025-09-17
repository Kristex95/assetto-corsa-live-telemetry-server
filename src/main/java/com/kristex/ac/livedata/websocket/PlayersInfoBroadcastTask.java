package com.kristex.ac.livedata.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayersInfoBroadcastTask {

	private final PlayersInfoWebSocketHandler playersInfoWebSocketHandler;

	@Scheduled(fixedRate = 200) // every 200 ms
	public void broadcast() {
		playersInfoWebSocketHandler.broadcastPlayers();
	}
}
