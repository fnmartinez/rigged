package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Comparator;

public class LessThanResourceComparator implements Comparator<Resource> {
	
	private final int max;
	
	public LessThanResourceComparator(int max) {
		this.max = max;
	}

	@Override
	public int compare(Resource actualValue, Resource proposedValue) {
		if (actualValue.getValue() > max) {
			if (proposedValue.getValue() > actualValue.getValue()) {
				return -3;
			} else {
				return -1;
			}
		} else {
			if (proposedValue.getValue() > actualValue.getValue()) {
				if (proposedValue.getValue() > max) {
					return -3;
				} else {
					return -2;
				}
			} else {
				return 1;
			}
		}
	}

	public String toString() {
		return "LessThan: " + max;
	}
}
