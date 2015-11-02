package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MetProposal extends Proposal{

	private final List<Resource> outcome = new LinkedList<Resource>();
	
	public MetProposal(List<Resource> content, List<Resource> outcome) {
		super(content);
		setMet();
	}

	public List<Resource> getOutcome() {
		return Collections.unmodifiableList(outcome);
	}
}
