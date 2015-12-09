package ar.edu.itba.it.sma.rigged.api.beans;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;

import com.google.common.collect.Sets;

public class CandidatesResourceEfficiency {
	
	private final Resource resource;
	private final int step;
	private Set<CandidatesEfficiency> candidatesEfficiency = Sets.newHashSet();
	
	public CandidatesResourceEfficiency(Resource resource, int step) {
		this.resource = resource;
		this.step = step;
	}
	
	public void addCandidateChoose(CandidateAgent candidate) {
		candidatesEfficiency.add(new CandidatesEfficiency(candidate));
		for (CandidatesEfficiency ce : candidatesEfficiency) {
			if (ce.getCandidate().equals(candidate.getName())) {
				ce.addChoose();
			}
		}
	}
	
	public String getResource() {
		return resource.getName();
	}
	
	public int getStep() {
		return step;
	}
	
	public Set<CandidatesEfficiency> getCandidatesEfficiency() {
		return this.candidatesEfficiency;
	}

	public void addMissingCandidates(Set<CandidateAgent> missingCandidates) {
		for (CandidateAgent candidate : missingCandidates) {
			candidatesEfficiency.add(new CandidatesEfficiency(candidate));
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
					.append(this.resource)
					.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().equals(this.getClass())) {
			return false;
		}
		
		if (this == obj) {
			return true;
		}
		
		CandidatesResourceEfficiency other = (CandidatesResourceEfficiency)obj;
		
		return new EqualsBuilder()
					.append(this.resource, other.resource)
					.isEquals();
	}

	public static class CandidatesEfficiency {
		private final CandidateAgent candidate;
		private int chooses;
		
		public CandidatesEfficiency(CandidateAgent candidate) {
			this.candidate = candidate;
			this.chooses = 0;
		}
		
		public void addChoose() {
			this.chooses++;
		}
		
		public String getCandidate() {
			return this.candidate.getName();
		}
		
		public int getChooses() {
			return this.chooses;
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
						.append(this.candidate)
						.toHashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !obj.getClass().equals(this.getClass())) {
				return false;
			}
			
			if (this == obj) {
				return true;
			}
			
			CandidatesEfficiency other = (CandidatesEfficiency)obj;
			
			return new EqualsBuilder()
						.append(this.candidate, other.candidate)
						.isEquals();
		}

	}
}
