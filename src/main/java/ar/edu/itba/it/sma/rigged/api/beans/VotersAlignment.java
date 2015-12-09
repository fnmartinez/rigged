package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import ar.edu.itba.it.sma.rigged.sarl.agents.VoterAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.desires.Desire;
import ar.edu.itba.it.sma.rigged.utils.PoliticalPlane;

public class VotersAlignment extends Alignment {
	
	private final Map<PoliticalPlane, Set<VoterAlignment>> voters = ImmutableMap.<PoliticalPlane, Set<VoterAlignment>>builder()
			.put(PoliticalPlane.LEFT_LIBERTARIAN, Sets.<VoterAlignment>newHashSet())
			.put(PoliticalPlane.LEFT_AUTHORITARIAN, Sets.<VoterAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_LIBERTARIAN, Sets.<VoterAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_AUTHORITARIAN, Sets.<VoterAlignment>newHashSet())
			.put(PoliticalPlane.UNDECIDED, Sets.<VoterAlignment>newHashSet())
			.build();

	public VotersAlignment(int[] xRange, int[] yRange, Iterable<VoterAgent> voters) {
		super(xRange, yRange);
		for (VoterAgent voter : voters) {
			this.getAgents().get(voter.getPoliticalQuadrant()).add(new VoterAlignment(voter));
		}
	}
	
	public Map<PoliticalPlane, Set<VoterAlignment>> getAgents() {
		return voters;
	}

	public static class VoterAlignment {
		private final String politicalQuadrant;
		private int economyMedian;
		private int socialMedian;
		
		VoterAlignment(VoterAgent voter) {
			politicalQuadrant = voter.getPoliticalQuadrant().name();
			for (Desire d : voter.getDesires()) {
				if (d.getResource().getName().equals("economic")) {
					this.economyMedian = d.getComparator().getValue();
				} else {
					this.socialMedian = d.getComparator().getValue();
				}
			}
		}

		public int getEconomyMedian() {
			return economyMedian;
		}

		public int getSocialMedian() {
			return socialMedian;
		}

		public String getPoliticalQuadrant() {
			return politicalQuadrant;
		}
	}

}
