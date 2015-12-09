package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.Map;
import java.util.Set;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;
import ar.edu.itba.it.sma.rigged.utils.PoliticalPlane;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class CandidatesAlignment extends Alignment {
	
	private final Map<PoliticalPlane, Set<CandidateAlignment>> candidates = ImmutableMap.<PoliticalPlane, Set<CandidateAlignment>>builder()
			.put(PoliticalPlane.LEFT_LIBERTARIAN, Sets.<CandidateAlignment>newHashSet())
			.put(PoliticalPlane.LEFT_AUTHORITARIAN, Sets.<CandidateAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_LIBERTARIAN, Sets.<CandidateAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_AUTHORITARIAN, Sets.<CandidateAlignment>newHashSet())
			.put(PoliticalPlane.UNDECIDED, Sets.<CandidateAlignment>newHashSet())
			.build();

	public CandidatesAlignment(int[] xRange, int[] yRange, Iterable<CandidateAgent> candidates) {
		super(xRange, yRange);
		for (CandidateAgent candidate : candidates) {
			this.getAgents().get(candidate.getPoliticalQuadrant()).add(new CandidateAlignment(candidate));
		}
	}
	
	@Override
	public Map<PoliticalPlane, Set<CandidateAlignment>> getAgents() {
		// TODO Auto-generated method stub
		return candidates;
	}

	public static class CandidateAlignment {
		private final String politicalQuadrant;
		private int economyMedian;
		private int socialMedian;
		
		CandidateAlignment(CandidateAgent candidate) {
			politicalQuadrant = candidate.getPoliticalQuadrant().name();
			for (Resource r : candidate.getProposal().getContent()) {
				if (r.getName().equals("economic")) {
					this.economyMedian = r.getValue();
				} else {
					this.socialMedian = r.getValue();
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
