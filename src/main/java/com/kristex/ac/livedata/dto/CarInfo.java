package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfo {
	private int id;
	private String name;
	private Position position;
	private boolean isConnected;
	@JsonProperty("best_lap")
	private Long bestLap;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Position {
		private double x;
		private double y;
		private double z;
	}
}