package ar.edu.itba.it.sma.rigged.sarl.support.proposals;


public interface ProposalChangeStrategy {

	public Proposal changeProposal(Proposal currentProposal, int votes);
	
}
