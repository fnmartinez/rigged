package ar.edu.itba.it.sma.rigged.sarl.support.proposals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ar.edu.itba.it.sma.rigged.sarl.support.Resource;

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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
					.append("content", content)
					.append("met", met)
					.build();
	}
}
