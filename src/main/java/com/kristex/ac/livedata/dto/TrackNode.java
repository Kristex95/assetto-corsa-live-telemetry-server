package com.kristex.ac.livedata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class
TrackNode {
	int index;
	int id;
	float direction;
	float distance;
	float x;
	float y;
	float z;
}
