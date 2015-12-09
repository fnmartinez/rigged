package ar.edu.itba.it.sma.rigged.api.utils;

import io.janusproject.Boot;
import io.janusproject.kernel.Kernel;
import io.sarl.lang.core.Agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import ar.edu.itba.it.sma.rigged.sarl.agents.CandidateAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.ElectoralCommitteeAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.EnvironmentAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.OpinionShaperAgent;
import ar.edu.itba.it.sma.rigged.sarl.agents.VoterAgent;
import ar.edu.itba.it.sma.rigged.sarl.support.desires.Desire;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.Proposal;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.ProposalChangeStrategy;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.strategies.CenteredProposalChangeStrategy;
import ar.edu.itba.it.sma.rigged.sarl.support.proposals.strategies.RandomProposalChangeStrategy;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.InBetweenResourceComparator;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.Resource;
import ar.edu.itba.it.sma.rigged.sarl.support.resources.ResourceComparator;
import ar.edu.itba.it.sma.rigged.utils.ConfigurationManager;
import ar.edu.itba.it.sma.rigged.utils.PoliticalPlane;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Module;

@Component
public class JanusManager {
	
	private static final String VOTER_NAME_TEMPLATE = "Voter-%s-%d";
	private static final String OPINION_SHAPER_NAME_TEMPLATE = "OpinionShaper-%s-%d";
	private static final String CANDIDATE_NAME_TEMPLATE = "Candidate-%s-%d";
	private static final Class<? extends Agent> ENVIRONMENT_AGENT_TYPE = EnvironmentAgent.class;
	private static final Class<? extends Agent> ELECTORAL_COMMITTEE_AGENT_TYPE = ElectoralCommitteeAgent.class;
	private static final Class<? extends Agent> CANDIDATE_AGENT_TYPE = CandidateAgent.class;
	private static final Class<? extends Agent> VOTER_AGENT_TYPE = VoterAgent.class;
	private static final Class<? extends Agent> OPINION_SHAPER_AGENT_TYPE = OpinionShaperAgent.class;	
	
	private final Set<OpinionShaperAgent> shapers = new HashSet<OpinionShaperAgent>();
	private final Kernel kernel;
	private final long seed = System.currentTimeMillis();
	private final Random random = new Random(seed);
	private ElectoralCommitteeAgent electoralCommitteeAgent;
	private EnvironmentAgent environmentAgent;
	
	public JanusManager() throws Exception {
		Class<? extends Module> dumbModule = null;
		List<Resource> resources = generateResources();
		kernel = Boot.startJanus(dumbModule, ENVIRONMENT_AGENT_TYPE, this, ConfigurationManager.INSTANCE.getMillisPerMonth(), resources);
		kernel.spawn(ELECTORAL_COMMITTEE_AGENT_TYPE, this, random, ConfigurationManager.INSTANCE.getElectionsInterval());
		generateOpinionShapers();
		generateVoters();
		Thread.sleep(5000);
		generateCandidates();
	}
	
	private void generateAgent(int totalQuantity, Map<PoliticalPlane, Float> proportions, Block block) {
		for (PoliticalPlane pq : PoliticalPlane.values()) {
			for (int q = 0; q < totalQuantity * proportions.get(pq); q++) {
				block.execute(pq, q);
			}
		}
	}

	private void generateVoters() {
		generateAgent(ConfigurationManager.INSTANCE.getVotersQty(), ConfigurationManager.INSTANCE.getVotersProportion(), new Block() {
			
			@Override
			public void execute(PoliticalPlane pq, int q) {
				List<Desire> desires = Lists.newArrayList();
				ResourceComparator economicRC = new InBetweenResourceComparator((int)(pq.getEconomicDispersion() * random.nextGaussian() + pq.getEconomicMedian()), pq.getEconomicDispersion());
				ResourceComparator socialRC = new InBetweenResourceComparator((int)(pq.getSocialDispersion() * random.nextGaussian() + pq.getSocialMedian()), pq.getSocialDispersion());
				for (Resource r : environmentAgent.getResources()) {
					if (r.getName().equals("economic")) {
						desires.add(new Desire(r, economicRC));
					} else {
						desires.add(new Desire(r, socialRC));
					}
				}
				spawnVoter(String.format(VOTER_NAME_TEMPLATE, pq.name(), q), random, pq, desires);				
			}
		});
	}
	
	private void generateOpinionShapers() {
		generateAgent(ConfigurationManager.INSTANCE.getOpinionShapersQty(), ConfigurationManager.INSTANCE.getOpinionShapersProportion(), new Block() {
			
			@Override
			public void execute(PoliticalPlane pq, int q) {
				List<Desire> desires = Lists.newArrayList();
				ResourceComparator economicRC = new InBetweenResourceComparator((int)(random.nextGaussian() + pq.getEconomicMedian()), pq.getEconomicDispersion());
				ResourceComparator socialRC = new InBetweenResourceComparator((int)(random.nextGaussian() + pq.getSocialMedian()), pq.getSocialDispersion());
				for (Resource r : environmentAgent.getResources()) {
					if (r.getName().equals("economic")) {
						desires.add(new Desire(r, economicRC));
					} else {
						desires.add(new Desire(r, socialRC));
					}
				}
				spawnOpinionShaper(String.format(OPINION_SHAPER_NAME_TEMPLATE, pq.name(), q), ConfigurationManager.INSTANCE.getOpinionInterval(), pq, desires);				
			}
		});
	}


	private void generateCandidates() {
		generateAgent(ConfigurationManager.INSTANCE.getCandidatesQty(), ConfigurationManager.INSTANCE.getCandidatesProportion(), new Block() {
			@Override
			public void execute(PoliticalPlane politicalQuadrant, int index) {
				List<Resource> content = ImmutableList.of(
						new Resource("social", getResourceQty(politicalQuadrant.getSocialMedian(), politicalQuadrant.getSocialDispersion())),
						new Resource("economic", getResourceQty(politicalQuadrant.getEconomicMedian(), politicalQuadrant.getEconomicDispersion())));
				Proposal p = new Proposal(content);
				ProposalChangeStrategy pcs = index == 0 ?
						new RandomProposalChangeStrategy(content, random, ConfigurationManager.INSTANCE.getResourceRangeMax()) :
						new CenteredProposalChangeStrategy(p, random);
				spawnCandidate(String.format(CANDIDATE_NAME_TEMPLATE, politicalQuadrant.name(), index), p, pcs, politicalQuadrant);
			}
		});
	}

	private List<Resource> generateResources() {
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("social", getResourceQty(PoliticalPlane.UNDECIDED.getSocialMedian(), PoliticalPlane.UNDECIDED.getSocialDispersion())));
		resources.add(new Resource("economic", getResourceQty(PoliticalPlane.UNDECIDED.getEconomicMedian(), PoliticalPlane.UNDECIDED.getEconomicDispersion())));
		return resources;
	}
	
	private int getResourceQty(int median, int dispersion) {
		return (int)((random.nextGaussian() + median) / dispersion);
	}

	public UUID spawnVoter(String voterName, Random random, PoliticalPlane politicalPlane, List<Desire> desires) {
		UUID uuid = kernel.spawn(VOTER_AGENT_TYPE, voterName, random, politicalPlane, desires);
		return uuid;
	}
	
	public UUID spawnCandidate(String candidateName, Proposal proposal, ProposalChangeStrategy proposalChangeStrategy, PoliticalPlane politicalQuadrant) {
		UUID uuid = kernel.spawn(CANDIDATE_AGENT_TYPE, candidateName, proposal, proposalChangeStrategy, politicalQuadrant);
		return uuid;
	}
	
	private UUID spawnOpinionShaper(String opinionShaperName, int opinionInterval, PoliticalPlane politicalPlane, List<Desire> desires) {
		return kernel.spawn(OPINION_SHAPER_AGENT_TYPE, this, opinionShaperName, opinionInterval, politicalPlane, desires);		
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
	
	public Iterable<OpinionShaperAgent> opinionShapers() {
		return this.shapers;
	}

	public long randomSeed() {
		return seed;
	}
	
	private static interface Block {

		void execute(PoliticalPlane politicalQuadrant, int index);
		
	}
}
