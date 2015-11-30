package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Comparator;

public interface ResourceComparator extends Comparator<Resource> {
	public String getTypeName();
	public int getValue();
}
