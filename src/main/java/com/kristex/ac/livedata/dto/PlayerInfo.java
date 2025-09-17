package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerInfo {
	private int id;
	@JsonProperty("player_name")
	private String playerName;
	@JsonProperty("car_name")
	private String carName;
	@JsonProperty("best_lap")
	private String bestLap;
	@JsonProperty("current_lap")
	private String currentLap;
	@JsonProperty("last_lap")
	private String lastLap;
	@JsonProperty("in_pit")
	private boolean isInPit;
	@JsonProperty("in_box")
	private boolean isInBox;
	@JsonProperty("is_connected")
	private boolean isConnected;
	@JsonProperty("leaderboard_pos")
	private Integer leaderboardPosition;
	@JsonProperty("realtime_leaderboard_pos")
	private Integer realTimeLeaderBoardPosition;
}
