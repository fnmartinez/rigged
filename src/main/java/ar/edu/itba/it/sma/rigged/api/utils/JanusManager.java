package ar.edu.itba.it.sma.rigged.api.utils;

import io.janusproject.Boot;
import io.janusproject.kernel.Kernel;
import io.sarl.lang.core.Agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.ElectoralCommitteeAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.EnvironmentAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.OpinionShaperAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.VoterAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.Desire;
import ar.edu.itba.it.sma.rigged.sarl.support.LessThanResourceComparator;
import ar.edu.itba.it.sma.rigged.sarl.support.MoreThanResourceComparator;
import ar.edu.itba.it.sma.rigged.sarl.support.Resource;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.Proposal;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.ProposalChangeStrategy;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.strategies.RandomProposalChangeStrategy;

import com.google.common.collect.Lists;
import com.google.inject.Module;

@Component
public class JanusManager {
	
	private static final Class<? extends Agent> ENVIRONMENT_AGENT_TYPE = EnvironmentAgent.class;
	private static final Class<? extends Agent> ELECTORAL_COMMITTEE_AGENT_TYPE = ElectoralCommitteeAgent.class;
	private static final Class<? extends Agent> CANDIDATE_AGENT_TYPE = CandidateAgent.class;
	private static final Class<? extends Agent> VOTER_AGENT_TYPE = VoterAgent.class;
	private static final Class<? extends Agent> OPINION_SHAPER_AGENT_TYPE = OpinionShaperAgent.class;	
	
	private final Set<OpinionShaperAgent> shapers = new HashSet<OpinionShaperAgent>();
	private final Kernel kernel;
	private final Random random = ThreadLocalRandom.current();
	private ElectoralCommitteeAgent electoralCommitteeAgent;
	private EnvironmentAgent environmentAgent;
	private int maxResourceQty = 100;
	private int votersQty = 50;
	private int opinionShapersQty = 3;
	private int candidatesQty = 4;
	private int millisPerMonth = 1000;
	private int yearsPerElection = 1;
	private int monthPerYear = 12;
	private int opinionInterval = 2;
	private double resourcesDispersion = 1.0;
	
	public JanusManager() throws Exception {
		Class<? extends Module> dumbModule = null;
		List<Resource> resources = generateResources();
		kernel = Boot.startJanus(dumbModule, ENVIRONMENT_AGENT_TYPE, this, millisPerMonth, resources);
		kernel.spawn(ELECTORAL_COMMITTEE_AGENT_TYPE, this, yearsPerElection * monthPerYear);
		generateOpinionShapers();
		generateVoters();
		Thread.sleep(5000);
		generateCandidates();
	}

	private void generateVoters() {
		for (int i = 0; i < votersQty; i++) {
			spawnVoter(String.format("Voter%d", i), getDesires());
		}
	}

	private void generateOpinionShapers() {
		for (int i = 0; i < opinionShapersQty; i++) {
			spawnOpinionShaper(String.format("OpinionShaper%d", i), opinionInterval * monthPerYear, getDesires());
		}
	}


	private void generateCandidates() {
		for (int i = 0; i < candidatesQty; i++) {
			List<Resource> content = Lists.newArrayList();
			for (Resource r : environmentAgent.getResources()) {
				content.add(new Resource(r.getName(), random.nextInt(maxResourceQty)));
			}
			spawnCandidate(String.format("Candidate%d", i), new Proposal(content), getProposalChangeStrategy());
		}
	}

	private RandomProposalChangeStrategy getProposalChangeStrategy() {
		return new RandomProposalChangeStrategy(environmentAgent.getResources(), (int)(maxResourceQty)/2);
	}

	private List<Resource> generateResources() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("Economía", getResourceQty()));
		resources.add(new Resource("Educación", getResourceQty()));
		resources.add(new Resource("Salud", getResourceQty()));
		resources.add(new Resource("Infraestrutura", getResourceQty()));	
		return resources;
	}
	
	private int getResourceQty() {
		return (int)((random.nextGaussian() + maxResourceQty/2) / resourcesDispersion);
	}

	private List<Desire> getDesires() {
		List<Desire> desires = Lists.newArrayList();
		for (Resource r : environmentAgent.getResources()) {
			desires.add(new Desire(r,
					random.nextBoolean() ? 
							new MoreThanResourceComparator(random.nextInt(maxResourceQty)) :
							new LessThanResourceComparator(random.nextInt(maxResourceQty))));
		}
		return desires;
	}

	public UUID spawnVoter(String voterName, List<Desire> desires) {
		UUID uuid = kernel.spawn(VOTER_AGENT_TYPE, voterName, desires);
		return uuid;
	}
	
	public UUID spawnCandidate(String candidateName, Proposal proposal, ProposalChangeStrategy proposalChangeStrategy) {
		UUID uuid = kernel.spawn(CANDIDATE_AGENT_TYPE, candidateName, proposal, proposalChangeStrategy);
		return uuid;
	}
	
	private UUID spawnOpinionShaper(String opinionShaperName, int opinionInterval, List<Desire> desires) {
		return kernel.spawn(OPINION_SHAPER_AGENT_TYPE, this, opinionShaperName, opinionInterval, desires);		
	}
	
	public void add(OpinionShaperAgent shaper) {
		shapers.add(shaper);
	}
	
	public void electoralCommittee(ElectoralCommitteeAgent electoralCommitteeAgent) {
		if (this.electoralCommitteeAgent != null) { throw new IllegalStateException("Electoral Committee already set");	}
		synchronized (this) {
			if (this.electoralCommitteeAgent != null) { throw new IllegalStateException("Electoral Committee already set");	}
			this.electoralCommitteeAgent = electoralCommitteeAgent;			
		}
	}
	
	public void environmentAgent(EnvironmentAgent environmentAgent) {
		if (this.environmentAgent != null) { throw new IllegalStateException("God Agent already set"); }
		synchronized (this) {
			if (this.environmentAgent != null) { throw new IllegalStateException("God Agent already set"); }
			this.environmentAgent = environmentAgent;
		}
	}
	
	public EnvironmentAgent environmentAgent() {
		return environmentAgent;
	}
	
	public ElectoralCommitteeAgent electoralCommittee() {
		return electoralCommitteeAgent;
	}

	public Iterable<VoterAgent> voters() {
		return this.electoralCommitteeAgent.getVoters();
	}
	
	public Iterable<CandidateAgent> candidates() {
		return this.electoralCommitteeAgent.getCandidates();
	}
}
