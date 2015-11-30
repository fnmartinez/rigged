package ar.edu.itba.it.sma.rigged.sarl.support;


public class MoreThanResourceComparator implements ResourceComparator {
	
	private final int min;
	
	public MoreThanResourceComparator(int min) {
		this.min = min;
	}

	@Override
	public int compare(Resource actualValue, Resource proposedValue) {
		return expectedHappiness(proposedValue) - currentHappiness(actualValue);
	}

	private int expectedHappiness(Resource proposedValue) {
		return proposedValue.getValue() - min;
	}
	
	private int currentHappiness(Resource actualValue) {
		return actualValue.getValue() - min;
	}
	
	@Override
	public String toString() {
		return "MoreThan: " + min;
	}

	@Override
	public String getTypeName() {
		return " > ";
	}

	@Override
	public int getValue() {
		return min;
	}

}
