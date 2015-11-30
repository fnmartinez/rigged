package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.List;

import ar.edu.itba.it.sma.rigged.sarl.agents.VoterAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.Desire;

import com.google.common.collect.Lists;

public class VotersResponse {
	
	private final List<Voter> voters = Lists.newArrayList();
	
	public VotersResponse(Iterable<VoterAgent> voters) {
		for (VoterAgent agent : voters) {
			this.voters.add(new Voter(agent));
		}
	}
	
	public List<Voter> getVoters() {
		return voters;
	}

	public static class Voter {
		private final String name;
		private final List<VoterDesire> desire = Lists.newArrayList();
		
		public Voter(VoterAgent agent) {
			this.name = agent.getName();
			for (Desire d : agent.getDesires()) {
				desire.add(new VoterDesire(d));
			}
		}
		
		public String getName() {
			return name;
		}
		
		public List<VoterDesire> getDesire() {
			return desire;
		}
	}
	
	public static class VoterDesire {
		private final String name;
		private final String f;
		private final int value;
		
		public VoterDesire(Desire d) {
			this.name = d.getResource().getName();
			this.f = d.getComparator().getTypeName();
			this.value = d.getComparator().getValue();
		}

		public String getName() {
			return name;
		}

		public String getF() {
			return f;
		}

		public int getValue() {
			return value;
		}		
	}
}
