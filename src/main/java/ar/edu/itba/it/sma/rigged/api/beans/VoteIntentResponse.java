package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;

public class VoteIntentResponse {
	
	private final int steps;
	
	private final Map<CandidateAgent, Integer> intents = new HashMap<>();
	
	public VoteIntentResponse(int steps, Iterable<CandidateAgent> candidates) {
		this.steps = steps;
		for (CandidateAgent candidate : candidates) {
			intents.put(candidate, 0);
		}
	}
	
	public void addIntent(CandidateAgent candidate) {
		Integer previousVotes = intents.put(candidate, 1);
		if (previousVotes != null) {
			intents.put(candidate, previousVotes + 1);
		}
	}
	
	public Set<VoteIntent> getIntents() {
		Set<VoteIntent> intents = new HashSet<>();
		for (Entry<CandidateAgent, Integer> entry : this.intents.entrySet()) {
			intents.add(new VoteIntent(entry.getKey().getName(), entry.getValue(), entry.getKey().getProposal().getContent()));
		}
		return intents;
	}
	
	public int getSteps() {
		return steps;
	}
	
	public static class VoteIntent {
		private final String candidateName;
		private final int votes;
		private final List<Resource> proposal;
		
		public VoteIntent(String candidateName, int votes, List<Resource> proposal) {
			this.candidateName = candidateName;
			this.votes = votes;
			this.proposal = proposal;
		}
		
		public String getCandidate() {
			return candidateName;
		}
		
		public int getVotes() {
			return votes;
		}
		
		public List<Resource> getProposal() {
			return proposal;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !this.getClass().equals(obj.getClass())) { return false; }
			if (obj == this) { return true; }
			
			VoteIntent other = (VoteIntent) obj;
			return new EqualsBuilder()
						.append(candidateName, other.candidateName)
						.isEquals();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
						.append(candidateName)
						.toHashCode();
		}
	}
}
