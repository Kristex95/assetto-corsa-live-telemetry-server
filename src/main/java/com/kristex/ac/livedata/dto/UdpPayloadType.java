package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UdpPayloadType {
	TRACK_NODE("track_node"), CARS_INFO("cars_info");

	@JsonValue
	private final String name;
}
