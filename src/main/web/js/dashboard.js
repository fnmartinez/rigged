var setDelay = 500;
var main;
var candidatesHistory = [],
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

$(document).ready(function() {
	main = function() {
		$.ajax({
			url: "http://localhost:8080/dashboard/status"
		}).done(function(data) {
			console.log(data);
			updateAgentsStatus(data);
			drawOverview();
		}).always(function(data) {
			setTimeout(main, setDelay);
		});
	}
	main();
	drawVoters();
});

function drawOverview() {
	drawVoteIntent();
	drawOpinionShaperBias();
	drawGlobalResources();
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

function drawVoters() {
	$.ajax({
		url : "http://localhost:8080/dashboard/voters"
	}).done(function (response) {
		var votersHtml = $("#voters");
		response.voters.forEach(function (voter, index, array) {
			var voterHtml = votersHtml.append("<tr></tr>");
			voterHtml.append("<td>"+voter.name+"</td>");
			var desireHtml = voterHtml.append("<td></td>");
			voter.desire.forEach(function (desire, index, array) {
				desireHtml.append("<span>"+desire.name+desire.f+desire.value+"</span><br/>")
			});
		});
	});
}

function drawOpinionShaperBias() {

}

function drawGlobalResources() {

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
