package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Comparator;

public class MoreThanResourceComparator implements Comparator<Resource>{
	
	private final int min;
	
	public MoreThanResourceComparator(int min) {
		this.min = min;
	}

	@Override
	public int compare(Resource actualValue, Resource proposedValue) {
		if (actualValue.getValue() < min) {
			if (proposedValue.getValue() < actualValue.getValue()) {
				return -3;
			} else {
				return -1;
			}
		} else {
			if (proposedValue.getValue() < actualValue.getValue()) {
				if (proposedValue.getValue() < min) {
					return -3;
				} else {
					return -2;
				}
			} else {
				return 1;
			}
		}
	}
	
	@Override
	public String toString() {
		return "MoreThan: " + min;
	}

}
