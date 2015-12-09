package ar.edu.itba.it.sma.rigged.utils;

public enum PoliticalPlane {
	LEFT_LIBERTARIAN,
	LEFT_AUTHORITARIAN,
	RIGHT_LIBERTARIAN,
	RIGHT_AUTHORITARIAN,
	UNDECIDED;
	
	private int economicMedian;
	private int economicDispersion;
	private int socialMedian;
	private int socialDispersion;
	
	void init(int economicMedian, int socialMedian) {
		this.economicMedian = economicMedian;
		this.economicDispersion = economicMedian / 4;
		this.socialMedian = socialMedian;
		this.socialDispersion = socialMedian / 4;
	}
	
	public int getEconomicMedian() {
		return economicMedian;
	}
	
	public int getEconomicDispersion() {
		return economicDispersion;
	}

	public int getSocialMedian() {
		return socialMedian;
	}

	public int getSocialDispersion() {
		return socialDispersion;
	}
}