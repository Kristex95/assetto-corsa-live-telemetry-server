package com.kristex.ac.livedata.websocket;

import com.kristex.ac.livedata.dto.CarInfo;
import com.kristex.ac.livedata.udp.UdpListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class CarsInfoWebSocketHandler extends TextWebSocketHandler {

	private final UdpListener udpListener;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	public void broadcastCars() {
		List<CarInfo> carsSnapshot = new ArrayList<>(udpListener.getCars());
		if (carsSnapshot.isEmpty()) return;

		try {
			String message = objectMapper.writeValueAsString(carsSnapshot);
			TextMessage textMessage = new TextMessage(message);
			for (WebSocketSession session : sessions) {
				if (session.isOpen()) {
					System.out.println("message: " + message);
					session.sendMessage(textMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
