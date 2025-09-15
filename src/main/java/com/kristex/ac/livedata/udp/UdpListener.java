package com.kristex.ac.livedata.udp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kristex.ac.livedata.config.UdpProperties;
import com.kristex.ac.livedata.dto.TrackNode;
import com.kristex.ac.livedata.dto.UdpMessage;
import com.kristex.ac.livedata.dto.UdpPayloadType;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class UdpListener {

	private final UdpProperties udpProperties;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Getter
	private final List<TrackNode> trackNodes = new CopyOnWriteArrayList<>();

	@PostConstruct
	public void startListener() {
		final Thread listenerThread = new Thread(() -> {
			try (final DatagramSocket socket = new DatagramSocket(udpProperties.getPort())) {
				final byte[] buffer = new byte[4096];

				while (true) {
					final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					socket.receive(packet);
					final String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

					try {
						final UdpMessage udpMessage = objectMapper.readValue(json, UdpMessage.class);
						if (udpMessage.getType() == UdpPayloadType.TRACK_NODE) {
							final TrackNode trackNode = objectMapper.convertValue(udpMessage.getData(), TrackNode.class);
							if (trackNode.getIndex() == 0) {
								trackNodes.clear();
							}
							trackNodes.add(trackNode);
							System.out.println("Stored node #" + trackNode.getIndex() + " (x=" + trackNode.getX() + ")");
						}
					} catch (Exception ex) {
						System.err.println("Invalid JSON: " + json);
						ex.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		listenerThread.setDaemon(true);
		listenerThread.start();
	}
}