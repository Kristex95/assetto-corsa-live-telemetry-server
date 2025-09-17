package com.kristex.ac.livedata.config;

import com.kristex.ac.livedata.websocket.CarsInfoWebSocketHandler;
import com.kristex.ac.livedata.websocket.PlayersInfoWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private final CarsInfoWebSocketHandler carsInfoWebSocketHandler;
	private final PlayersInfoWebSocketHandler playersInfoWebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(playersInfoWebSocketHandler, "/ws/players").setAllowedOrigins("*");
		registry.addHandler(carsInfoWebSocketHandler, "/ws/cars").setAllowedOrigins("*");
	}
}
