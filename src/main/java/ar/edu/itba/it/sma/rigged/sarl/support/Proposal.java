package ar.edu.itba.it.sma.rigged.sarl.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Proposal {
	
	private final List<Resource> content = new LinkedList<Resource>();
	private boolean met;
	
	public Proposal(List<Resource> content) {
		this.content.addAll(content);
		this.met = false;
	}
	
	public List<Resource> getContent() {
		return Collections.unmodifiableList(content);
	}
	
	public boolean isProposalMet() {
		return this.met;
	}
	
	protected void setMet() {
		this.met = true;	
	}
}
