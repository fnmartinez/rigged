package ar.edu.itba.it.sma.rigged.api.beans;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.AgentStatus;

public class StatusMessage {

	private final AgentStatus environmentStatus;
	private final AgentStatus electoralCommitteeStatus;
	private final CandidateAgent candidate;
	private final int steps;
	private final boolean onElections;
	
	public StatusMessage(AgentStatus environmentStatus, AgentStatus electoralCommitteeStatus, CandidateAgent candidate, int steps, boolean onElections) {
		this.environmentStatus = environmentStatus;
		this.electoralCommitteeStatus = electoralCommitteeStatus;
		this.candidate = candidate;
		this.steps = steps;
		this.onElections = onElections;
	}
	
	public AgentStatus getEnvironmentStatus() {
		return environmentStatus;
	}
	
	public AgentStatus getElectoralCommitteeStatus() {
		return electoralCommitteeStatus;
	}		
	
	public int getSteps() {
		return steps;
	}
	
	public boolean getOnElections() {
		return onElections;
	}
	
	public String getElectedCandidate() {
		return candidate == null ? null : candidate.getName();
	}
}
