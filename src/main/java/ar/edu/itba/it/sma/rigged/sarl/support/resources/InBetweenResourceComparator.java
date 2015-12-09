package ar.edu.itba.it.sma.rigged.sarl.support.resources;

public class InBetweenResourceComparator implements ResourceComparator {
	
	private final ResourceComparator innerComparator;
	private final int median;
	private final int range;
	
	public InBetweenResourceComparator(int median, int range) {
		this.median = median;
		this.range = range;
		this.innerComparator = new LessThanResourceComparator(range);
	}

	@Override
	public int compare(Resource actualValue, Resource proposedValue) {
		final int normalActualValue = Math.abs(actualValue.getValue() - median);
		final int normalProposedValue = Math.abs(proposedValue.getValue() - median);
		return innerComparator.compare(new Resource("dummy", normalActualValue), new Resource("dummy", normalProposedValue));
	}

	@Override
	public String getTypeName() {
		return " ~= ";
	}

	@Override
	public int getValue() {
		return median;
	}
	
	@Override
	public String toString() {
		return "InBetween: " + median + " +/- " + range;
	}

}
