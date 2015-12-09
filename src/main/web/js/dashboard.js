var setDelay = 500;
var main;
var candidatesHistory = new Array(),
	voteIntentKeyAccessor = function(voteIntent) { return voteIntent.step; },
	voteIntentValueAccessor = function(voteIntent) { return voteIntent.votes; },
	voteIntentCategoryAccessor = function(candidateIntentHistory) { return candidateIntentHistory.candidate; },
	voteIntentValuesAccessor = function(candidateIntentHistory) { return candidateIntentHistory.history; }
;
var voteIntentChart = new LineChart("vote-intent",
		voteIntentKeyAccessor,
		voteIntentValueAccessor,
		voteIntentCategoryAccessor,
		voteIntentValuesAccessor,
		BIG_CHART);

var alignmentCategoryAccessor = function(alignment) { return alignment.politicalQuadrant; },
	alignmentValuesAccessor = function(alignment) { return alignment.values; },
	alignmentValueAccessor = function(alignmentValue) { return alignmentValue.socialMedian; },
	alignmentKeyAccessor = function(alignmentValue) { return alignmentValue.economyMedian; };

var votersAlignmentChart = new PlaneChart("voters-alignment",
		alignmentKeyAccessor,
		alignmentValueAccessor,
		MEDIUM_CHART);
var shapersAlignmentChart = new PlaneChart("shapers-alignment",
		alignmentKeyAccessor,
		alignmentValueAccessor,
		MEDIUM_CHART);
var candidatesAlignmentChart = new PlaneChart("candidates-alignment",
		alignmentKeyAccessor,
		alignmentValueAccessor,
		MEDIUM_CHART);

$(document).ready(function() {
	main = function() {
		$.ajax({
			url: "http://localhost:8080/dashboard/status"
		}).done(function(data) {
			//console.log(data);
			updateAgentsStatus(data);
			drawOverview();
		}).always(function(data) {
			setTimeout(main, setDelay);
		});
	}
	main();
	//drawVoters();
});

function drawOverview() {
	drawVoteIntent();
	drawVotersAlignment();
	drawOpinionShapresAlignment();
	drawCandidatesAlignment();
}

function drawVoteIntent() {
	$.ajax({
		url: "http://localhost:8080/dashboard/vote-intent",
	}).done(function(response) {
		if (voteIntentChart.categories.length == 0) {
			var categories = [];

			response.intents.forEach(function (candidateIntent, index, array) {
				categories.push(candidateIntent.candidate);
			});
			voteIntentChart.init(categories);
		}
		response.intents.forEach(function (candidateIntent, index, array) {
			var candidateIntentHistory = candidatesHistory
				.find(function (_candidateIntent, _index, _array) { return candidateIntent.candidate == _candidateIntent.candidate; });
			if (candidateIntentHistory == undefined) {
				candidateIntentHistory = new CandidateIntentHistory(candidateIntent.candidate);
				candidatesHistory.push(candidateIntentHistory);
			}
			var voteIntent = candidateIntentHistory.history
				.find(function (_voteIntent, _index, _array) { return _voteIntent.step == response.steps; });
			if (voteIntent == undefined) {
				voteIntent = new VoteIntent(candidateIntent.proposal, response.steps, candidateIntent.votes);
				candidateIntentHistory.history.push(voteIntent);
			}
			voteIntentChart.draw(candidateIntentHistory);
		});
	});
}

function drawVotersAlignment() {
	$.ajax({
		url: "http://localhost:8080/dashboard/voters-alignment"
	}).done(function (response) {
		if (votersAlignmentChart.categories.length == 0) {
			var categories = [];
			for (politicalQuadrant in response.agents) {
				categories.push(politicalQuadrant);
			}
			votersAlignmentChart.init(categories, response.xdomain, response.ydomain);
		}
		for (politicalQuadrant in response.agents) {
			votersAlignmentChart.draw(politicalQuadrant, response.agents[politicalQuadrant]);
		}
	});
}

function drawOpinionShapresAlignment() {
	$.ajax({
		url: "http://localhost:8080/dashboard/shapers-alignment"
	}).done(function (response) {
		if (shapersAlignmentChart.categories.length == 0) {
			var categories = [];
			for (politicalQuadrant in response.agents) {
				categories.push(politicalQuadrant);
			}
			shapersAlignmentChart.init(categories, response.xdomain, response.ydomain);
		}
		for (politicalQuadrant in response.agents) {
			shapersAlignmentChart.draw(politicalQuadrant, response.agents[politicalQuadrant]);
		}
	});
}

function drawCandidatesAlignment() {
	$.ajax({
		url: "http://localhost:8080/dashboard/candidates-alignment"
	}).done(function (response) {
		if (candidatesAlignmentChart.categories.length == 0) {
			var categories = [];
			for (politicalQuadrant in response.agents) {
				categories.push(politicalQuadrant);
			}
			candidatesAlignmentChart.init(categories, response.xdomain, response.ydomain);
		}
		for (politicalQuadrant in response.agents) {
			candidatesAlignmentChart.draw(politicalQuadrant, response.agents[politicalQuadrant]);
		}
	});
}

function fetchCandidatesTendency() {
	$.ajax({
		url: "http://localhost:8080/dashboard/candidates",
		method: "POST"
	})
}

function play() {
	$.ajax({
		url: "http://localhost:8080/dashboard/play",
		method: "POST"
	});
}

function pause() {
	$.ajax({
		url: "http://localhost:8080/dashboard/pause",
		method: "POST"
	});
}

function stepOne() {
	$.ajax({
		url: "http://localhost:8080/dashboard/step?steps=1",
		method: "POST"
	});
}

function updateAgentsStatus(data) {
	updateEnvironmentStatus(data.environmentStatus);
	updateElectoralCommitteeStatus(data.electoralCommitteeStatus);
	updateElectionsStatus(data.onElections);
	updateMonthCount(data.steps);
	updateCurrentElectedCandidate(data.electedCandidate);
	updateSeed(data.seed);
}

function updateEnvironmentStatus(environmentStatus) {
	var labelClass = "label ";
	switch (environmentStatus) {
		case "PAUSED":
			labelClass += "label-warning";
			break;
		case "RUNNING":
			labelClass += "label-success";
			break;
		case "INSTANTIATED":
			labelClass += "label-info";
			break;
		default:
			labelClass += "label-default";
	}
	$("#environmentAgentStatus").removeClass().addClass(labelClass);
}

function updateElectoralCommitteeStatus(electoralCommitteeStatus) {
	var labelClass = "label ";
	switch (electoralCommitteeStatus) {
		case "INSTANTIATED":
			labelClass += "label-success";
			break;
		default:
			labelClass += "label-default";
	}
	$("#electoralCommitteeStatus").removeClass().addClass(labelClass);
}

function updateElectionsStatus(onElections) {
	var labelClass = "label ";
	var badgeText = "Unknown";
	if (onElections) {
		labelClass += "label-success";
		badgeText = "Yes";
	} else {
		labelClass += "label-default";
		badgeText = "No";
	}
	$("#electionStatusLabel").removeClass().addClass(labelClass);
	$("#electionStatusBadge").text(badgeText);
}

function updateMonthCount(monthCount) {
	$("#monthCountBadge").text(monthCount);
}

function updateCurrentElectedCandidate(electedCandidateName) {
	if (electedCandidateName == null) {
		$("#currentElectedCandidateBadge").text('none');
	} else {
		$("#currentElectedCandidateBadge").text(electedCandidateName);
	}
}

function updateSeed(seed) {
	$("#seedBadge").text(seed);
}
