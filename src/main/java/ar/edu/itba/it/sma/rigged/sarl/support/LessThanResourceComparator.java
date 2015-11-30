package ar.edu.itba.it.sma.rigged.sarl.support;


public class LessThanResourceComparator implements ResourceComparator {
	
	private final int max;
	
	public LessThanResourceComparator(int max) {
		this.max = max;
	}

	@Override
	public int compare(Resource actualValue, Resource proposedValue) {
		return expectedHappiness(proposedValue) - currentHappiness(actualValue);
	}

	private int expectedHappiness(Resource proposedValue) {
		return max - proposedValue.getValue();
	}
	
	private int currentHappiness(Resource actualValue) {
		return max - actualValue.getValue();
	}

	public String toString() {
		return "LessThan: " + max;
	}

	@Override
	public String getTypeName() {
		return " < ";
	}

	@Override
	public int getValue() {
		return max;
	}
}
