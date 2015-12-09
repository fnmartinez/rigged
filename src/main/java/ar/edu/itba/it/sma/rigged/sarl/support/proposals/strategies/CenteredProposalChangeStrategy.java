package ar.edu.itba.it.sma.rigged.sarl.support.proposals.strategies;

import java.util.List;
import java.util.Random;

import ar.edu.itba.it.sma.rigged.sarl.support.proposals.Proposal;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.ProposalChangeStrategy;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;

import com.google.common.collect.Lists;

public class CenteredProposalChangeStrategy implements ProposalChangeStrategy {

	private final Proposal baseProposal;
	private final Random random;
	
	public CenteredProposalChangeStrategy(Proposal baseProposal, Random random) {
		this.baseProposal = baseProposal;
		this.random = random;
	}
	
	@Override
	public Proposal changeProposal(Proposal currentProposal, int votes) {
		List<Resource> newContent = Lists.newArrayList();
		for (Resource r : baseProposal.getContent()) {
			newContent.add(new Resource(r.getName(), (int)(random.nextGaussian() + r.getValue())));
		}
		return new Proposal(newContent);
	}

}
