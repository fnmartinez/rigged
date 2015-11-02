package ar.edu.itba.it.sma.rigged.api.utils;


import io.janusproject.Boot;
import io.janusproject.kernel.Kernel;
import io.sarl.lang.core.Agent;

import org.springframework.stereotype.Component;

import com.google.inject.Module;

import ar.edu.itba.it.sma.rigged.sarl.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.ElectoralCommitteeAgent;
import ar.edu.itba.it.sma.rigged.sarl.VoterAgent;

@Component
public class JanusManager {
	
	private static final Class<? extends Agent> ELECTORAL_COMMITTEE_AGENT_TYPE = ElectoralCommitteeAgent.class;
	private static final Class<? extends Agent> CANDIDATE_AGENT_TYPE = CandidateAgent.class;
	private static final Class<? extends Agent> VOTER_AGENT_TYPE = VoterAgent.class;	
	
	private final Kernel kernel;
	private int millisPerYear = 4000;
	private int yearsPerElection = 4;
	
	public JanusManager() throws Exception {
		Class<? extends Module> dumbModule = null;
		this.kernel = Boot.startJanus(dumbModule, ELECTORAL_COMMITTEE_AGENT_TYPE, millisPerYear, yearsPerElection);
	}

}
