package com.kristex.ac.livedata.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristex.ac.livedata.dto.PlayerInfo;
import com.kristex.ac.livedata.udp.UdpListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class PlayersInfoWebSocketHandler extends TextWebSocketHandler {

	private final UdpListener udpListener;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		sessions.remove(session);
	}

	public void broadcastPlayers() {
		try {
			Collection<PlayerInfo> players = udpListener.getPlayers().values();
			String json = objectMapper.writeValueAsString(players);
			for (WebSocketSession session : sessions) {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(json));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
