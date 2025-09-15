package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UdpMessage {
	@JsonProperty("type")
	private UdpPayloadType type;

	@JsonProperty("data")
	private Object data; // raw data, could be TrackNode or List<CarInfo>

	private static final ObjectMapper mapper = new ObjectMapper();

	public TrackNode getTrackNode() {
		if (type == UdpPayloadType.TRACK_NODE && data != null) {
			return mapper.convertValue(data, TrackNode.class);
		}
		return null;
	}

	public List<CarInfo> getCarInfos() {
		if (type == UdpPayloadType.CARS_INFO && data != null) {
			return mapper.convertValue(data, mapper.getTypeFactory().constructCollectionType(List.class, CarInfo.class));
		}
		return null;
	}
}