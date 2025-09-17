package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfo {
	private int id;
	private String name;
	private Position position;
	private boolean isConnected;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Position {
		private double x;
		private double y;
		private double z;
	}
}