package ar.edu.itba.it.sma.rigged.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.itba.it.sma.rigged.api.beans.StatusMessage;
import ar.edu.itba.it.sma.rigged.api.beans.VoteIntentResponse;
import ar.edu.itba.it.sma.rigged.api.beans.VotersResponse;
import ar.edu.itba.it.sma.rigged.api.utils.JanusManager;
import ar.edu.itba.it.sma.rigged.sarl.agents.VoterAgent;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private JanusManager janusManager;
	
	@RequestMapping(value = "/play", method = RequestMethod.POST)
	public String play() {
		return janusManager.environmentAgent().run().toString();		
	}
	
	@RequestMapping(value = "/pause", method = RequestMethod.POST)
	public String pause() {
		return janusManager.environmentAgent().pause().toString();
	}
	
	@RequestMapping(value = "/step", method = RequestMethod.POST)
	public String step(
			@RequestParam(value="steps") Integer steps) {
		janusManager.environmentAgent().step(steps);
		return Integer.toString(janusManager.electoralCommittee().getSteps());
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public StatusMessage status() {
		return new StatusMessage(janusManager.environmentAgent().getStatus(), 
				janusManager.electoralCommittee().getStatus(),
				janusManager.electoralCommittee().getCurrentElectedCandidate(), 
				janusManager.electoralCommittee().getSteps(),
				janusManager.electoralCommittee().isOnElections());
	}
	
	@RequestMapping(value = "/vote-intent", method = RequestMethod.GET)
	public VoteIntentResponse voteIntent() {
		VoteIntentResponse intent = new VoteIntentResponse(janusManager.electoralCommittee().getSteps(), janusManager.candidates());
		for (VoterAgent voter : janusManager.voters()) {
			intent.addIntent(voter._chooseCandidate());
		}
		return intent;
	}
	
	@RequestMapping(value = "/voters")
	public VotersResponse voters() {
		VotersResponse vr = new VotersResponse(janusManager.voters());
		return vr;
	}

}
