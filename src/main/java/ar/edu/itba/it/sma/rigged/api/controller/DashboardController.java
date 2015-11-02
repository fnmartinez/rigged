package ar.edu.itba.it.sma.rigged.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.sma.rigged.api.utils.JanusManager;

@RestController
public class DashboardController {
	
	@Autowired
	private JanusManager janusManager;
	
	@RequestMapping("/play")
	public String play() {
		return "play";
	}
	
	@RequestMapping("/pause")
	public String pause() {
		return "pause";
	}
	
	@RequestMapping("/step")
	public String step(
			@RequestParam(value="steps") Integer steps) {
		return String.format("Step %d", steps);
	}

}
