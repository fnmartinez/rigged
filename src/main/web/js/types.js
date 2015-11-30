var CandidateIntentHistory = function(candidateName) {
	this.candidate = candidateName;
	this.history = [];
}

var VoteIntent = function(proposal, step, votes) {
	this.proposal = proposal;
	this.step = step;
	this.votes = votes;
}
