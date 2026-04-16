package com.kristex.ac.livedata.controller;

import com.kristex.ac.livedata.dto.CarInfo;
import com.kristex.ac.livedata.dto.TrackNode;
import com.kristex.ac.livedata.udp.UdpListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TrackController {

	private final UdpListener udpListener;

	@Value("${server.port:8080}")
	private String port;

	@GetMapping("/track")
	public String track(Model model) {
		final List<TrackNode> nodes = udpListener.getTrackNodes();
		final Map<Integer,CarInfo> cars = udpListener.getCars();
		final Integer splitsCount = udpListener.getPlayers().get(0).getLastSplits().size();

		model.addAttribute("nodes", nodes);
		model.addAttribute("cars", cars);
		model.addAttribute("splitsCount", splitsCount);
		model.addAttribute("port", port);
		return "track";
	}
}