package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Comparator;

public class Desire {
	
	private final Resource resource;
	private final Comparator<Resource> comparator;
	
	public Desire(Resource resource, Comparator<Resource> comparator) {
		this.resource = resource;
		this.comparator = comparator;
	}
	
	public int calculateScore(Proposal p) {
		for (Resource r : p.getContent()) {
			if (r.getName().equals(resource.getName())) {
				return comparator.compare(r, resource);
			}
		}
		return 0;
	}
}
