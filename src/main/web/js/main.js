function play() {
	$.ajax(
		{url: "http://localhost:8080/play"}
	).then(function(data) {
		alert(data);
	});
}

function pause() {
	$.ajax(
		{url: "http://localhost:8080/pause"}
	).then(function(data) {
		alert(data);
	});
}

function stepOne() {
	$.ajax(
		{url: "http://localhost:8080/step?steps=1"}
	).then(function(data) {
		alert(data);
	});
}
