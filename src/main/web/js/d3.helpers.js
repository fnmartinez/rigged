var SMALL_CHART = {
	width : 300,
	height : 200,
	margin : {
		top : 10,
		bottom : 10,
		left : 10,
		right : 10
	}
};
var MEDIUM_CHART = {
	width : 500,
	height : 300,
	margin : {
			top : 15,
			bottom : 15,
			left : 15,
			right : 15
		}
};
var BIG_CHART = {
	width : 640,
	height : 480,
	margin : {
		top : 20,
		bottom : 20,
		left : 20,
		right : 80
	}
};

var LineChart = function(domParentId, xFn, yFn, categoryAccessor, valuesAccessor, chartSize, animationTime = 500) {
	this.chart = null;
	this.chartSize = chartSize;
	this.parentId = domParentId;
	this.xFn = xFn;
	this.yFn = yFn;
	this.categoryAccessor = categoryAccessor;
	this.valuesAccessor = valuesAccessor;
	this.animationTime = animationTime;
	this.xScale = d3.scale.linear()
  		.range([this.chartSize.margin.left, this.chartSize.width - this.chartSize.margin.right]);
	this.yScale = d3.scale.linear()
		.range([this.chartSize.height - this.chartSize.margin.top, this.chartSize.margin.bottom]);
	this.xAxis = d3.svg.axis()
		.scale(this.xScale)
		.orient("bottom");
	this.yAxis = d3.svg.axis()
		.scale(this.yScale)
		.orient("left");
	this.lineGen = d3.svg.line()
		.x(function(d) { return this.xScale(this.xFn(d)); })
		.y(function(d) { return this.yScale(this.yFn(d)); })
		.interpolate("linear");
	this.color = d3.scale.category10();
	this.categories = [];
	this.chartName = this.parentId + "-chart";
}

LineChart.prototype.init = function(categories) {
	this.categories = categories;
	var parent = d3.select("#" + this.parentId)
		.append("svg")
		.attr("class", this.chartName)
		.attr("width", this.chartSize.width)
		.attr("height", this.chartSize.height);
	parent.append("svg:g")
		.attr("class","axis x-axis")
		.attr("transform", "translate(0," + (this.chartSize.height - this.chartSize.margin.bottom) + ")")
		.call(this.xAxis);
	parent.append("svg:g")
		.attr("class","axis y-axis")
		.attr("transform", "translate(" + this.chartSize.margin.left + ",0)")
		.call(this.yAxis);
	this.chart = d3.select("." + this.chartName);
	this.color.domain(d3.keys(this.categories))
	var that = this;
	for (var i = 0; i < this.categories.length; i++) {
		this.initLine(this.categories[i], i, this.categories);
	}
}

LineChart.prototype.initLine = function(category, index, categories) {
	this.chart.append("g")
		.attr("class", category);
	var g = this.chart.select("." + category);
	g.append("path")
		.attr("class", "line " + this.getCategoryLineName(category))
		.attr("stroke", this.color(category))
		.attr("fill", "none");
	g.append("text")
		.attr("class", "category-text " + this.getCategoryTextName(category))
		.text(category);
}

LineChart.prototype.draw = function(data) {
	this.xScale.domain(d3.extent(this.valuesAccessor(data), this.xFn));
	this.yScale.domain(d3.extent(this.valuesAccessor(data), this.yFn));
	this.chart.transition().select("." + this.getCategoryLineName(this.categoryAccessor(data)))
		 .duration(this.animationTime)
		 .attr("d", this.lineGen(this.valuesAccessor(data)));
	this.chart.transition().select("x-axis")
		 .duration(this.animationTime)
		 .call(this.xAxis);
	this.chart.transition().select("y-axis")
		 .duration(this.animationTime)
		 .call(this.yAxis);
	var values = this.valuesAccessor(data);
	this.chart.transition().select("." + this.getCategoryTextName(this.categoryAccessor(data)))
		 .duration(this.animationTime)
		 .attr("transform",
		 	"translate(" + this.xScale(this.xFn(values[values.length - 1])) +
							"," + this.yScale(this.yFn(values[values.length - 1])) +")");
}

LineChart.prototype.getCategoryLineName = function(category) {
	return this.chartName + "-" + category + "-line";
}

LineChart.prototype.getCategoryTextName = function(category) {
	return this.chartName + "-" + category + "-text";
}


function drawPie(elementId, innerRadius, outerRadius, data, valueAccesor, keyAccesor) {
	var pie = d3.layout.pie()
				.value(valueAccesor);
	var slices = pie(data);

	var svg = d3.select("svg#" + elementId);
	var arc = d3.svg.arc()
				.innerRadius(innerRadius)
				.outerRadius(outerRadius);
	var color = d3.scale.category10();

	var g = svg.append('g')
				.attr('transform', 'translate(200, 50)')

	g.selectAll('path.slice')
		.data(slices)
		.enter()
		.append('path')
		.transition()
		.duration(100)
		.attr('class', 'slice')
		.attr('d', arc)
		.attr('fill', function(d) {	return color(keyAccesor(d.data)); });

	svg.append('g')
	  .attr('class', 'legend')
		.selectAll('text')
		.data(slices)
		  .enter()
			.append('text')
			  .text(function(d) { return '• ' + keyAccesor(d.data); })
			  .attr('fill', function(d) { return color(keyAccesor(d.data)); })
			  .attr('y', function(d, i) { return 20 * (i + 1); });
}
