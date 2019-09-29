<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contest-Codecozy</title>
<%@include file="ui-headings.html" %>
<style>
#editor {
        margin: 0;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
    }
</style>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	<div class="container-fluid">
	
	<button type="button" class="btn btn-link" style="font-size:20px" onclick="window.location.href='contests?id=${CONTEST.id}'">&lt;Go Back</button>
		<br>
	
		<div class="jumbotron">
		<h2><b>${CONTEST.name }</b></h2>
		<h5>${ PROBLEM.name }</h5>
		</div>
	<c:if test="${ CONTEST.start<=CURRENT_TIMESTAMP }">
			<div style="position:fixed;z-index:11111;bottom:0;right:0;padding:15px;background:#EEEEFF;border:5px solid black"><h4 id="time_rem"></h4>
			</div>
			<script>
				function setTime(countDownDate) {
					
					
					var x = setInterval(function() {

						  // Get today's date and time
						  var now = new Date().getTime();
							//console.log(now);
						  // Find the distance between now and the count down date
						  var distance = countDownDate - now;

						  // Time calculations for days, hours, minutes and seconds
						  var days = Math.floor(distance / (1000 * 60 * 60 * 24));
						  var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
						  var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
						  var seconds = Math.floor((distance % (1000 * 60)) / 1000);

						  // Display the result in the element with id="demo"
						  document.getElementById("time_rem").innerHTML ="<b>Time Left: </b>" + days + "d " + hours + "h "
						  + minutes + "m " + seconds + "s";

						  // If the count down is finished, write some text 
						  if (distance < 0) {
						    clearInterval(x);
						    document.getElementById("time_rem").innerHTML = "ENDED";
						  }
						}, 1000);
					
					
				}
				setTime(${ CONTEST.end});
			</script>
		</c:if>
		<br>
	
		<c:if test="${CONTEST.start <= CURRENT_TIMESTAMP }">
			<a href="${ PROBLEM.link }" class="btn-link" target="_blank">View Problem</a>
			<br>
		</c:if>
		<c:if test="${CONTEST.start<=CURRENT_TIMESTAMP && CURRENT_TIMESTAMP<=CONTEST.end }">
		</c:if>
			<br>
			<select id="language">
				<option value="c_c++">C++</option>
				<option value="java">Java</option>
				<option value="python">Python</option>
			</select>
			<div id="editor"style="width:100%;height:60vh"></div>
			<script src="public/ace.js" type="text/javascript" charset="utf-8"></script>
			<textarea id="custom-input" rows="7"style="width:100%" placeholder="Custom Input Here"></textarea>
			
			<button class="btn btn-success" onclick='doCompilation()' type="submit" style="background:#8BC34A;border:0px;margin-top:10px">Compile & Run</button>
			<button class="btn btn-primary" onclick='doSubmission("${PROBLEM.id}")' type="submit" style="border:0px;margin-top:10px">Submit</button>
			
			<div class="container-fluid"style="margin-top:15px;background:#EEEEEE;">
				<div class="row" id="evaluating" style="display:none">
					<div class="col-sm-1">
						<image src="public/loading.webp"width="80px">	
					</div>
					<div class="col-md-11">
						<section style="font-size:30px;padding-top:15px">Running...</section>
					</div>
				</div>
				<div class="container-fluid" id="test_result"style="display:none;">
					<div class="row" id="compiled_and_run"style="padding-top:15px;border-bottom:0.5px solid black;padding-bottom:10px">
						<div class="col-sm-1">
							<image src="public/green_tick.png"width="60px">	
						</div>
						<div class="col-md-11">
							<section style="font-size:24px;padding-top:15px">Executed</section>
						</div>
					</div>
					<div class="row" style="margin-top:25px">
						<div class="col-sm-6">
							<h3>Memory</h3>
							<p id="test_memory" style="font-size:20px">
							</p>
						</div>
						<div class="col-sm-6">
							<h3>Time</h3>
							<p id="test_time"style="font-size:20px">
							</p>
						</div>
					</div>
					<div class="row" style="margin-top:25px">
						<div class="col-sm-12">
							<h3>Output</h3>
							
							<textarea id="test_output"style="font-size:20px;width:100%;border:none;background:none;resize:none;overflow-y:hidden" disabled>
							</textarea>
						</div>
					</div>
				</div>
			</div>
	
<script>
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.session.setMode("ace/mode/c_cpp");
	document.getElementById('editor').style.fontSize='18px';
	editor.resize();
	$('#language').change(function(){
	    editor.session.setMode("ace/theme/"+document.getElementById('language').value);
	});
	
	function auto_grow(o) {
		  o.style.height = "1px";
		  o.style.height = (25+o.scrollHeight)+"px";
	}
	
 	function doCompilation() {
 		const customInput = document.getElementById('custom-input').value;
 		const code = editor.getSession().getValue();
 		const language = document.getElementById('language').value;
 		document.getElementById('evaluating').style.display='flex';
 		document.getElementById('test_result').style.display='none';
 		// console.log(language);
 		$.post( "problem", { command: "compile_code", sourceCode: code, stdin: customInput,lang: language }, function(data) {
			data = JSON.parse(data);
			console.log(data);
			if(data.memory==="") data.memory='--';
			if(data.time==="") data.time='--';
			document.getElementById('evaluating').style.display='none';	
			document.getElementById('test_result').style.display='block';
			document.getElementById('test_time').innerHTML=data.time;
			document.getElementById('test_memory').innerHTML=data.memory;
			if(data.compResult==="S") {
				if(data.rntError==="") {
					document.getElementById('test_output').value=data.output;
				} else {
					document.getElementById('test_output').value='Runtime Error\n'+data.rntError;
				}
			} else {
				document.getElementById('test_output').value='Compilation Error\n'+data.cmpError;
			}
			auto_grow(document.getElementById('test_output'));
			
		});
 	}
 	
 	

</script>
		<br>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
	</div>
</body>
</html>