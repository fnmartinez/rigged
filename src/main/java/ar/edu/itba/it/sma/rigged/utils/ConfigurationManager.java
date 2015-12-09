package ar.edu.itba.it.sma.rigged.utils;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum ConfigurationManager {
	INSTANCE;
	
	private final int[] resourceRange = new int[] {0, 100};
	private final int votersQty = 100;
	private final Map<PoliticalPlane, Float> votersProportion = ImmutableMap.<PoliticalPlane, Float>builder()
				.put(PoliticalPlane.LEFT_LIBERTARIAN, .33f)
				.put(PoliticalPlane.LEFT_AUTHORITARIAN, .33f)
				.put(PoliticalPlane.RIGHT_LIBERTARIAN, 0f)
				.put(PoliticalPlane.RIGHT_AUTHORITARIAN, 0f)
				.put(PoliticalPlane.UNDECIDED, .34f)
				.build();
	private final int opinionShapersQty = 4;
	private final Map<PoliticalPlane, Float>  opinionShapersProportion = ImmutableMap.<PoliticalPlane, Float>builder()
			.put(PoliticalPlane.LEFT_LIBERTARIAN, .33f)
			.put(PoliticalPlane.LEFT_AUTHORITARIAN, .33f)
			.put(PoliticalPlane.RIGHT_LIBERTARIAN, 0f)
			.put(PoliticalPlane.RIGHT_AUTHORITARIAN, 0f)
			.put(PoliticalPlane.UNDECIDED, .34f)
			.build();
	private final int candidatesQty = 2;
	private final Map<PoliticalPlane, Float>  candidatesProportion = ImmutableMap.<PoliticalPlane, Float>builder()
			.put(PoliticalPlane.LEFT_LIBERTARIAN, .5f)
			.put(PoliticalPlane.LEFT_AUTHORITARIAN, 0f)
			.put(PoliticalPlane.RIGHT_LIBERTARIAN, 0f)
			.put(PoliticalPlane.RIGHT_AUTHORITARIAN, 0f)
			.put(PoliticalPlane.UNDECIDED, .5f)
			.build();
	private final int millisPerMonth = 1000;
	private final int electionsInterval = 5;
	private final int opinionInterval = 2;
	private final long systemSeed = System.currentTimeMillis();
	
	private ConfigurationManager() {
		PoliticalPlane.LEFT_LIBERTARIAN.init(getResourceRangeMid() / 2, 
			getResourceRangeMid() / 2);
		PoliticalPlane.LEFT_AUTHORITARIAN.init(getResourceRangeMid() / 2, 
				getResourceRangeMid() / 2 + getResourceRangeMid());
		PoliticalPlane.RIGHT_LIBERTARIAN.init(getResourceRangeMid() / 2 + getResourceRangeMid(), 
				getResourceRangeMid() / 2);
		PoliticalPlane.RIGHT_AUTHORITARIAN.init(getResourceRangeMid() / 2 + getResourceRangeMid(), 
				getResourceRangeMid() / 2 + getResourceRangeMid());
		PoliticalPlane.UNDECIDED.init(getResourceRangeMid(), getResourceRangeMid());
	}
	
	public int[] getResourceRange() {
		return resourceRange;
	}
	
	public int getResourceRangeMax() {
		return resourceRange[1];
	}
	
	public int getResourceRangeMin() {
		return resourceRange[0];
	}
	
	public int getResourceRangeMid() {
		return (getResourceRangeMax() - getResourceRangeMin()) / 2;
	}
	
	public int getVotersQty() {
		return votersQty;
	}
	
	public Map<PoliticalPlane, Float>  getVotersProportion() {
		return votersProportion;
	}
	
	public int getOpinionShapersQty() {
		return opinionShapersQty;
	}
	
	public Map<PoliticalPlane, Float>  getOpinionShapersProportion() {
		return opinionShapersProportion;
	}
	
	public int getCandidatesQty() {
		return candidatesQty;
	}
	
	public Map<PoliticalPlane, Float>  getCandidatesProportion() {
		return candidatesProportion;
	}
	
	public int getElectionsInterval() {
		return electionsInterval;
	}
	
	public int getOpinionInterval() {
		return opinionInterval;
	}

	public int getMillisPerMonth() {
		return millisPerMonth;
	}

	public long getSystemSeed() {
		return systemSeed;
	}
}
