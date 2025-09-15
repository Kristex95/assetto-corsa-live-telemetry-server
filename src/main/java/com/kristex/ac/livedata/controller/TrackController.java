package com.kristex.ac.livedata.controller;

import com.kristex.ac.livedata.dto.TrackNode;
import com.kristex.ac.livedata.udp.UdpListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TrackController {

	private final UdpListener udpListener;

	@GetMapping("/track")
	public String track(Model model) {
		List<TrackNode> nodes = udpListener.getTrackNodes();
		model.addAttribute("nodes", nodes);
		return "track";
	}
}