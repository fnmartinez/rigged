package ar.edu.itba.it.sma.rigged.sarl.support.desires;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ar.edu.itba.it.sma.rigged.sarl.support.proposals.Proposal;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.ResourceComparator;

public class Desire {
	
	private final Resource resource;
	private final ResourceComparator comparator;
	
	public Resource getResource() {
		return resource;
	}
	
	public ResourceComparator getComparator() {
		return comparator;
	}
	
	public Desire(Resource resource, ResourceComparator comparator) {
		this.resource = resource;
		this.comparator = comparator;
	}
	
	public int calculateScore(Proposal p) {
		for (Resource r : p.getContent()) {
			if (r.getName().equals(resource.getName())) {
				return comparator.compare(resource, r);
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("resource", resource)
				.append("comparator", comparator)
				.build();
	}
}
