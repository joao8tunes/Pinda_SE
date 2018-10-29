//#sbu - sunburst 
//#tmp - treemap
//#snp - snippet
//#dir - directorie
//#sct - scatterplot
//quando um cara for clicado, replica o evento pros mesmos elementos relativos em outras visualizaçãoes, assim como seus 'pais'.

var numberOfSnippets = 0, tree = '', sourceClick = null;

function setNumOfSnippets(num)
{
	numberOfSnippets = num; 
}

function loadSnippets(data)
{
  $('#snippets').removeClass('snippets_LT5');
  $('#vis_snippets').removeClass('vis_snippets_LT5');
  $('#vis_snippets').html(data);
  $('.snippet').mouseenter(function () {
  	$(this).addClass('snippet_hover');
  }).mouseleave(function () {
  	$(this).removeClass('snippet_hover');
  });
  $('.snippet').click(function () {
  	$('.snippet').removeClass('snippet_selected');
  	$(this).addClass('snippet_selected');
    setThumbnail($(this).children('span').text());
  });
  $('.snippet').dblclick(function () {
  	window.open($(this).children('span').text(), '_blank');
  });    
  $('.snippet:first').addClass('snippet_selected');
    
  if (numberOfSnippets <=5 ) {
      $('#snippets').addClass('snippets_LT5');
      $('#vis_snippets').addClass('vis_snippets_LT5');
  }
}



function loadSunburst(json)
{   
if (numberOfSnippets <= 5) {
    $('#sunburst').hide();
    return;
}
    
$('#sunburst').show();
$('#vis_sunburst').empty();

var width = $('#vis_sunburst').width(),
    height = $('#vis_sunburst').height()*0.85,
    radius = (Math.min(width, height) / 2)*0.90;

var x = d3.scale.linear()
    .range([0, 2 * Math.PI]);

var y = d3.scale.linear()
    .range([0, radius]);

var svg = d3.select("#vis_sunburst").append("svg")
    .attr("width", $('#vis_sunburst').width())
    .attr("height", $('#vis_sunburst').height())
  .append("g")
    .attr("transform", "translate(" + $('#vis_sunburst').width() / 2 + "," + ($('#vis_sunburst').height() / 2) + ")");

var partition = d3.layout.partition()
    .value(function(d) { return d['value']; });

var arc = d3.svg.arc()
    .startAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d['x']))); })
    .endAngle(function(d) { return Math.max(0, Math.min(2 * Math.PI, x(d['x'] + d['dx']))); })
    .innerRadius(function(d) { return Math.max(0, y(d['y'])); })
    .outerRadius(function(d) { return Math.max(0, y(d['y'] + d['dy'])); });

  var g = svg.selectAll("g")
      .data(partition.nodes(json))
    .enter().append("g");

  var path = g.append("path")
    .attr("d", arc)
    .style("fill", function(d) { return d['color']; })
    .style("z-index", "-1")
    .on("click", function(d) { return d['children'] ? click(d) : null; });

  var text = g.append("text")
    .attr("transform", function(d) { return "rotate(" + computeTextRotation(d) + ")"; })
    .attr("x", function(d) { return y(d['y']); })
    .attr("dx", "0") // margin - 6
    .attr("dy", ".35em") // vertical-align
    .attr("id", function(d) {return d['children'] ? 'sbu_cls_' + d['id'] : 'sbu_' + d['name'].split("_")[1];})
    .style("font-size", "85%")
    .style("z-index", "9999")
    .text(function(d) { return d['name']; });

  function click(d) {
    //console.log(sourceClick);
    if (sourceClick == null) sourceClick = 'sunburst';
    else if (sourceClick == 'sunburst') return;
      
    // fade out all text elements
    d3.select("#vis_sunburst").selectAll("path").attr("opacity", 0);
    d3.select("#vis_sunburst").selectAll("text").attr("opacity", 0);

    path.style("z-index", "-1")
      .transition()
      .attrTween("d", arcTween(d))
      .each("end", function(e, i) {
          // check if the animated element's data e lies within the visible angle span given in d
          if (e['x'] >= d['x'] && e['x'] < (d['x'] + d['dx'])) {
            // get a selection of the associated text element
            var arcText = d3.select(this.parentNode).select("text");
            
            d3.select(this.parentNode).selectAll("path").attr("opacity", 1);

            // fade in the text element and recalculate positions
            arcText.style("z-index", "9999")
              .transition().duration(150)
              .attr("opacity", 1)
              .attr("transform", function() { return "rotate(" + computeTextRotation(e) + ")"; })
              .attr("x", function(d) { return y(d['y']); });
          }
      });
      
      coordinateFromSunburst(d['cluster'], d['children'] ? d['id'] : null);
      sourceClick = null;
  }

  //d3.select(self.frameElement).style("height", height + "px");

  // Interpolate the scales!
  function arcTween(d) {
    var xd = d3.interpolate(x.domain(), [d['x'], d['x'] + d['dx']]),
        yd = d3.interpolate(y.domain(), [d['y'], 1]),
        yr = d3.interpolate(y.range(), [d['y'] ? 20 : 0, radius]);
    
    return function(d, i) {
      return i ? function(t) { return arc(d); } : function(t) { x.domain(xd(t)); y.domain(yd(t)).range(yr(t)); return arc(d); };
    };
  }

  function computeTextRotation(d) {
    return (x(d['x'] + d['dx'] / 2) - Math.PI / 2) / Math.PI * 180;
  }
}



function loadTreemap(json)
{
if (numberOfSnippets <= 5) {
    $('#treemap').hide();
    return;
}
    
$('#treemap').show();    
$('#vis_treemap').empty();

var margin = {top: 20, right: 0, bottom: 0, left: 0},
    width = $('#vis_treemap').width(),
    height = $('#vis_treemap').height() - margin.top - margin.bottom,
    formatNumber = d3.format(",d"),
    transitioning;

var x = d3.scale.linear()
    .domain([0, width])
    .range([0, width]);

var y = d3.scale.linear()
    .domain([0, height])
    .range([0, height]);

var treemap = d3.layout.treemap()
    .children(function(d, depth) { return depth ? null : d._children; })
    .sort(function(a, b) { return a.value - b.value; })
    .ratio(height / width * 0.5 * (1 + Math.sqrt(5)))
    .round(false);

var svg = d3.select("#vis_treemap").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.bottom + margin.top)
    .style("margin-left", -margin.left + "px")
    .style("margin.right", -margin.right + "px")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
    .style("shape-rendering", "crispEdges");

var grandparent = svg.append("g")
    .attr("class", "grandparent");

grandparent.append("rect")
    .attr("y", -margin.top)
    .attr("width", width)
    .attr("height", margin.top)
    .style("fill", json['color']);

grandparent.append("text")
    .attr("x", 6)
    .attr("y", 6 - margin.top)
    .attr("dy", ".75em");

  initialize(json);
  accumulate(json);
  layout(json);
  display(json);

  function initialize(root) {
    root['x'] = root['y'] = 0;
    root['dx'] = width;
    root['dy'] = height;
    root['depth'] = 0;
  }

  // Aggregate the values for internal nodes. This is normally done by the
  // treemap layout, but not here because of our custom implementation.
  // We also take a snapshot of the original children (_children) to avoid
  // the children being overwritten when when layout is computed.
  function accumulate(d) {
    return (d['_children'] = d['children']) ? d['value'] = d['children'].reduce(function(p, v) { return p + accumulate(v); }, 0) : d['value'];
  }

  // Compute the treemap layout recursively such that each group of siblings
  // uses the same size (1×1) rather than the dimensions of the parent cell.
  // This optimizes the layout for the current zoom state. Note that a wrapper
  // object is created for the parent node for each group of siblings so that
  // the parent’s dimensions are not discarded as we recurse. Since each group
  // of sibling was laid out in 1×1, we must rescale to fit using absolute
  // coordinates. This lets us use a viewport to zoom.
  function layout(d) {
    if (d['_children']) {
      treemap.nodes({_children: d['_children']});
      d['_children'].forEach(function(c) {
        c['x'] = d['x'] + c['x'] * d['dx'];
        c['y'] = d['y'] + c['y'] * d['dy'];
        c['dx'] *= d['dx'];
        c['dy'] *= d['dy'];
        c['parent'] = d;
        layout(c);
      });
    }
  }

  function display(d) {
  	grandparent
        .datum(d['parent'])
        .on("click", transition)
      .select("text")
        .text(name(d));

    var g1 = svg.insert("g", ".grandparent")
        .datum(d)
        .attr("class", "depth");

    var g = g1.selectAll("g")
        .data(d['_children'])
      .enter().append("g");

    g.filter(function(d) { return d['_children']; })
        .classed("children", true)
        .on("click", transition);

    g.selectAll(".child")
        .data(function(d) { return d['_children'] || [d]; })
      .enter().append("rect")
        .attr("class", "child")
        .call(rect);

    g.append("rect")
        .attr("class", "parent")
        .call(rect)
        .style("fill", function(d) { return d['color']; })
      .append("title")
        .text(function(d) { return formatNumber(d['value']); });

    g.append("text")
    	.attr("id", function(d) {return d['_children'] ? 'tmp_cls_' + d['id'] : 'tmp_' + d['name'].split("_")[1];})
        .attr("dy", ".75em")
        .text(function(d) { return d['name']; })
        .call(text);

    function transition(d) {
      //console.log(sourceClick);
      if (sourceClick == null) sourceClick = 'treemap';
      else if (sourceClick == 'treemap') return;    
        
      if (transitioning || !d) {
          sourceClick = null; 
          return;
      }
      
      transitioning = true;

      var g2 = display(d),
          t1 = g1.transition().duration(300),
          t2 = g2.transition().duration(300);

      // Update the domain only after entering new elements.
      x.domain([d['x'], d['x'] + d['dx']]);
      y.domain([d['y'], d['y'] + d['dy']]);

      // Enable anti-aliasing during the transition.
      svg.style("shape-rendering", null);

      // Draw child nodes on top of parent nodes.
      svg.selectAll(".depth").sort(function(a, b) { return a.depth - b.depth; });

      // Fade-in entering text.
      g2.selectAll("text").style("fill-opacity", 0);

      // Transition to the new view.
      t1.selectAll("text").call(text).style("fill-opacity", 0);
      t2.selectAll("text").call(text).style("fill-opacity", 1);
      t1.selectAll("rect").call(rect);
      t2.selectAll("rect").call(rect);

      // Remove the old node when the transition is finished.
      t1.remove().each("end", function() {
        svg.style("shape-rendering", "crispEdges");
        transitioning = false;
      });
        
      coordinateFromTreemap(d['cluster'], d['_children'] ? d['id'] : null);  
      sourceClick = null;
    }

    return g;
  }

  function text(text) {
    text.attr("x", function(d) { return x(d['x']) + 6; })
        .attr("y", function(d) { return y(d['y']) + 6; });
  }

  function rect(rect) {
    rect.attr("x", function(d) { return x(d['x']); })
        .attr("y", function(d) { return y(d['y']); })
        .attr("width", function(d) { return x(d['x'] + d['dx']) - x(d['x']); })
        .attr("height", function(d) { return y(d['y'] + d['dy']) - y(d['y']); });
  }

  function name(d) {
    return d['parent'] ? name(d['parent']) + "." + d['name'] : d['name'];
  }
}



function loadScatterplot(json)
{
if (numberOfSnippets <= 5) {
    $('#scatterplot').hide();
    return;
}
    
$('#scatterplot').show();    
$('#vis_scatterplot').empty();

var margin = {top: 20, right: 20, bottom: 30, left: 40},
    width = $('#vis_scatterplot').width() - margin.left - margin.right,
    height = $('#vis_scatterplot').height() - margin.top - margin.bottom;

var x = d3.scale.linear()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var svg = d3.select("#vis_scatterplot").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  // add the tooltip area to the webpage
  var tooltip = d3.select("body").append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);    

  //Dimensões máximas do gráfico:
  x.domain(d3.extent(json['cluster'], function(d) { return d.x; })).nice();
  y.domain(d3.extent(json['cluster'], function(d) { return d.y; })).nice();

  //Eixo x:
  /*svg.append("g")
      .attr("class", "x axis-scatterplot")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
    .append("text")
      .attr("class", "label")
      .attr("x", width)
      .attr("y", -6)
      .style("text-anchor", "end")
      .text("Eixo x");*/

  //Eixo y:
  /*svg.append("g")
      .attr("class", "y axis-scatterplot")
      .call(yAxis)
    .append("text")
      .attr("class", "label")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Eixo y");*/

    //Pontos:
    var dots = svg.selectAll(".dot-scatterplot");
    
    for (var i = 0; i < 5; i++) {
    	dots.data(json['children'][i]['cluster'])
        .enter().append("circle")
        .attr("class", "dot-scatterplot")
        .attr("id", function(d) {return d['children'] ? 'sct_cls_' + d['id'] : 'sct_' + d['name'].split("_")[1];})
        .attr("r", 3.5)
        .attr("cx", function(d) { return x(d.x); })
        .attr("cy", function(d) { return y(d.y); })
        .style("fill", function(d) { 
          var legend = svg.append("g")  
                .attr("class", "legend")
                .attr("transform", "translate(0," + ((i*20)-20)  + ")");

          legend.append("rect")
              .attr("x", width)
              .attr("width", 18)
              .attr("height", 18)
              .style("fill", d.color);

          legend.append("text")
              .attr("x", width - 6)
              .attr("y", 9)
              .attr("dy", ".35em")
              .style("text-anchor", "end")
              .text('Cluster ' + (i+1));  

          return d.color; 
        })
        .on("mouseover", function(d) {
            tooltip.style("opacity", 1.0);
            tooltip.html(d["name"])
                 .style("left", (x(d.x) + $(window).width()*0.505) + "px")
                 .style("top", (y(d.y) + $(window).height()*0.12) + "px");
        })
        .on("mouseout", function(d) {
            tooltip.style("opacity", 0);
        });
    }

    //var color = d3.scale.linear().domain([0, 1, 2, 3, 4]).range(colors);
    
    dots.each(function(d,i) {
      d.jitter = xscale(d.x) + random_jitter();
    });
    dots.attr("cx", function(d,i) { return d.jitter });
}

function loadThumbnail(link)
{
    $('#thumbnail').removeClass('thumbnail_LT5');
    $('#vis_thumbnail').removeClass('vis_thumbnail_LT5');
    $('#iframe_thumbnail').removeClass('iframe_thumbnail_LT5');
    
    $('#vis_thumbnail').html('<iframe id="iframe_thumbnail" sandbox="allow-forms allow-pointer-lock allow-popups allow-same-origin allow-scripts" src="' + link + '" frameborder="0"></iframe>');
    
    if (numberOfSnippets <=5 ) {
        $('#thumbnail').addClass('thumbnail_LT5');
        $('#vis_thumbnail').addClass('vis_thumbnail_LT5');
        $('#iframe_thumbnail').addClass('iframe_thumbnail_LT5');
    }
}

function setThumbnail(link) {
    $('#vis_thumbnail').html('<iframe id="iframe_thumbnail" src="' + link + '" frameborder="0"></iframe>');
}

function loadDirectories(data)
{
    if (numberOfSnippets <= 5) {
        $('#directories').hide();
        return;
    }

  $('#directories').show();        
	tree = '';
	createDirectoriesTree(data);
  $('#vis_directories').jstree("destroy");
  $('#vis_directories').html(tree);
  $('#vis_directories').jstree();
	$('#vis_directories i').click();
    $('.jstree-anchor').dblclick(function () {
        coordinateFromDirectories($(this).attr('id').split('_')[2]);
    });
}

function createDirectoriesTree(data)
{	
	if (data['_children'] != null) {
		tree += "<ul><li id='dir_cls_" + data['id'] + "' data-jstree='{\"icon\" : \"_import/img/folder-icon.png\"}'>" + data['name'];
		
		for (var i = 0; i < data['_children'].length; i++) {
			createDirectoriesTree(data['_children'][i]);
		}
	
		tree += '</li></ul>';
	}
	else {
		tree += "<ul><li id='dir_" + data['name'].split("_")[1] + "' data-jstree='{\"icon\" : \"_import/img/earth-icon.png\"}'>" + data['name'] + "</li></ul>";
	}
}

function coordinateFromTreemap(d, clusterId)
{
    var ids = [], size = 10;
	
	for (var i = 0; i < d.length; i++) ids[i] = d[i]['name'].split('_')[1];
	$('.snippet').hide();
	$('.dot-scatterplot').attr("r", 3.5);
	
	if (numberOfSnippets == d.length) size = 3.5;
	
	//Coordenando as visualizações:
	for (var i = 0; i < ids.length; i++) {
		$('#snp_' + ids[i]).show();    //Coordenando a visualização Snippets.
		//$('#sbu_' + ids[i]).click();    //Coordenando a visualização Sunburst.
		$('#sct_' + ids[i]).attr("r", size);    //Coordenando a visualização Scatterplot.
	}
	
    if (clusterId != null) {
        $("#vis_directories").jstree("deselect_all", true);
        $("#vis_directories").jstree("close_all", "#dir_cls_0");
        $("#vis_directories").jstree("select_node", "#dir_cls_" + clusterId);
        $("#vis_directories").jstree("open_node", "#dir_cls_" + clusterId);
        
        $("#sbu_cls_" + clusterId).prev().simulate('click');
    }
}

function coordinateFromSunburst(d, clusterId)
{
    var ids = [], size = 10;
	
    if (d == null) return;
        
	for (var i = 0; i < d.length; i++) ids[i] = d[i]['name'].split('_')[1];
	$('.snippet').hide();
	$('.dot-scatterplot').attr("r", 3.5);
	
	if (numberOfSnippets == d.length) size = 3.5;
	
	//Coordenando as visualizações:
	for (var i = 0; i < ids.length; i++) {
		$('#snp_' + ids[i]).show();    //Coordenando a visualização Snippets.
		//$('#sbu_' + ids[i]).click();    //Coordenando a visualização Sunburst.
		$('#sct_' + ids[i]).attr("r", size);    //Coordenando a visualização Scatterplot.
	}
	
    if (clusterId != null) {
        $("#vis_directories").jstree("deselect_all", true);
        $("#vis_directories").jstree("close_all", "#dir_cls_0");
        $("#vis_directories").jstree("select_node", "#dir_cls_" + clusterId);
        $("#vis_directories").jstree("open_node", "#dir_cls_" + clusterId);
        
        if ($("#tmp_cls_" + clusterId).css("fill-opacity") == null) $(".grandparent rect").simulate('click');
            
        $("#tmp_cls_" + clusterId).parent().simulate('click');
    }
}

function coordinateFromDirectories(clusterId)
{   
	if (clusterId != null) {
        $("#sbu_cls_" + clusterId).prev().simulate('click');
        if ($("#tmp_cls_" + clusterId).css("fill-opacity") == null) $(".grandparent rect").simulate('click');
        $("#tmp_cls_" + clusterId).parent().simulate('click');
    }
}