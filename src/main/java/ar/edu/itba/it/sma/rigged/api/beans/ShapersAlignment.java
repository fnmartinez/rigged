package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.Map;
import java.util.Set;

import ar.edu.itba.it.sma.rigged.sarl.agents.OpinionShaperAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.desires.Desire;
import ar.edu.itba.it.sma.rigged.utils.PoliticalPlane;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class ShapersAlignment extends Alignment {
	
	private final Map<PoliticalPlane, Set<OpinionShaperAlignment>> shapers = ImmutableMap.<PoliticalPlane, Set<OpinionShaperAlignment>>builder()
			.put(PoliticalPlane.LEFT_LIBERTARIAN, Sets.<OpinionShaperAlignment>newHashSet())
			.put(PoliticalPlane.LEFT_AUTHORITARIAN, Sets.<OpinionShaperAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_LIBERTARIAN, Sets.<OpinionShaperAlignment>newHashSet())
			.put(PoliticalPlane.RIGHT_AUTHORITARIAN, Sets.<OpinionShaperAlignment>newHashSet())
			.put(PoliticalPlane.UNDECIDED, Sets.<OpinionShaperAlignment>newHashSet())
			.build();

	public ShapersAlignment(int[] xRange, int[] yRange, Iterable<OpinionShaperAgent> shapers) {
		super(xRange, yRange);
		for (OpinionShaperAgent shaper : shapers) {
			this.getAgents().get(shaper.getPoliticalQuadrant()).add(new OpinionShaperAlignment(shaper));
		}
	}
	
	public Map<PoliticalPlane, Set<OpinionShaperAlignment>> getAgents() {
		return this.shapers;
	}

	public static class OpinionShaperAlignment {
		private final String politicalQuadrant;
		private int economyMedian;
		private int socialMedian;
		
		OpinionShaperAlignment(OpinionShaperAgent shaper) {
			politicalQuadrant = shaper.getPoliticalQuadrant().name();
			for (Desire d : shaper.getDesires()) {
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
