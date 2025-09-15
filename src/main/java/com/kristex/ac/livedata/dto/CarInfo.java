package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfo {
	private int id;
	private String name;
	private Position position;
	private Velocity velocity;
	private Speed speed;
	private boolean isConnected;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Position {
		private double x;
		private double y;
		private double z;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Velocity {
		private double x;
		private double y;
		private double z;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Speed {
		private double kmh;
		private double mph;
		private double ms;
	}
}