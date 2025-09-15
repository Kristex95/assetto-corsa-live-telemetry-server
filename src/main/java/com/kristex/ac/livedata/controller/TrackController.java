package com.kristex.ac.livedata.controller;

import com.kristex.ac.livedata.dto.CarInfo;
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
		final List<TrackNode> nodes = udpListener.getTrackNodes();
		final List<CarInfo> cars = udpListener.getCars();
		model.addAttribute("nodes", nodes);
		model.addAttribute("cars", cars);
		return "track";
	}
}