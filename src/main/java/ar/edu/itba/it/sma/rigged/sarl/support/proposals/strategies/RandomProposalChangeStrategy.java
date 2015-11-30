package ar.edu.itba.it.sma.rigged.sarl.support.proposals.strategies;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ar.edu.itba.it.sma.rigged.sarl.support.Resource;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.Proposal;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.ProposalChangeStrategy;

public class RandomProposalChangeStrategy implements ProposalChangeStrategy {

	private final Set<Resource> resources = Sets.newHashSet();
	private final int meanResourceQty;
	private final Random random;
	
	public RandomProposalChangeStrategy(Collection<Resource> resources, int meanResourceQty) {
		this.resources.addAll(resources);
		this.meanResourceQty = meanResourceQty;
		this.random = new Random(System.currentTimeMillis());
	}
	@Override
	public Proposal changeProposal(Proposal currentProposal, int votes) {
		List<Resource> content = Lists.newArrayList();
		for (Resource r : resources) {
			content.add(new Resource(r.getName(), (int)(random.nextGaussian() + meanResourceQty)));
		}
		return new Proposal(content);
	}

}
