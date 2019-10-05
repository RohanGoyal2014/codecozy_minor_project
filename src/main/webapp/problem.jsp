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
		</c:if>
			<button id="btn_get_submissions"onclick='showSubmissions("${PROBLEM.id}")' data-toggle="collapse" data-target="#show_submissions" class="btn btn-primary"style="margin-left:25px">Show Submissions</button>
		
<div id="show_submissions" class="collapse container-fluid" style="margin-top:50px">

</div>
<script>
	function showSubmissions(pid) {
		state = $('#show_submissions').css('display');
		if(state === 'none') {
			$.post( "problem", { command: "get_submissions", problem: pid }, function(data) {
				data = JSON.parse(data);
				console.log(data);
				if(data.length === 0) {
					
					document.getElementById('show_submissions').innerHTML='<h4>No Submissions to show...</h4>';
				} else {
					time = new Date().getTime();
					var str='';
					for(var i=0;i<data.length;++i) {
						str+='<tr>';
						str+='<td>';
						diff = parseInt((time-data[i].time)/1000);
						//console.log(diff)
						min = parseInt(diff/60);
						hr = parseInt(min/60);
						days = parseInt(hr/24);
						months = parseInt(days/30);
						year = parseInt(months/12);
						if(year!==0) str+=year+' yrs ago';
						else if(months!==0) str+=months+' months ago';
						else if(days!==0) str+=days+' days ago';
						else if(hr!==0) str+=hr+' hrs ago';
						else if(min!==0) str+=min+' mins ago';
						else str+=diff+' secs ago';
						//str+=(time-data[i].time)/60000;
						str+='</td>';
						str+='<td>';
						str+=data[i].score
						str+='</td>';
						str+='<td>';
						str+='<a href="'+data[i].link+'" class="link"target="_blank">View</a>';
						str+='</td>';
						str+='</tr>'
					}
					document.getElementById('show_submissions').innerHTML='<table class="table"><tbody>'+str+'</tbody></table>';
					console.log(new Date().getTime());
					document.getElementById('btn_get_submissions').innerHTML='Hide Submissions';
					
				}
				
			});
		} else {
			document.getElementById('btn_get_submissions').innerHTML='Show Submissions';
		}
	}
</script>
			<br>
		<c:if test="${CONTEST.start<=CURRENT_TIMESTAMP && CURRENT_TIMESTAMP<=CONTEST.end }">
		
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
				<div class="container-fluid" id="submission_result"style="display:none">
					<div class="row" id="compiled_and_run_2"style="padding-top:15px;border-bottom:0.5px solid black;padding-bottom:10px">
						<div class="col-sm-1">
							<image src="public/green_tick.png"width="60px">	
						</div>
						<div class="col-md-11">
							<section style="font-size:24px;padding-top:15px">Executed</section>
						</div>
					</div>
					<div class="row" style="padding:25px">
					<h3 id="submission_score">Score:0</h3>										
					</div>
					<div class="row" style="margin-top:25px" style="display:none" id="sb_err">
						<div class="col-sm-12">							
							<textarea id="submission_error"style="font-size:20px;width:100%;border:none;background:none;resize:none;overflow-y:hidden" disabled>
							</textarea>
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						<strong>Inputs</strong>
						</div>
						<div class="col-sm-4">
						<strong>Result</strong>
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						Input 1
						</div>
						<div class="col-sm-4" id="submission_res1">
						AC
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						Input 2
						</div>
						<div class="col-sm-4" id="submission_res2">
						AC
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						Input 3
						</div>
						<div class="col-sm-4" id="submission_res3">
						AC
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						Input 4
						</div>
						<div class="col-sm-4" id="submission_res4">
						AC
						</div>
					</div>
					<div class="row" style="padding:25px">
						<div class="col-sm-8">
						Input 5
						</div>
						<div class="col-sm-4" id="submission_res5">
						AC
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
 		document.getElementById('submission_result').style.display='none';
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
 	
 	function doSubmission(pid) {
 		const code = editor.getSession().getValue();
 		const language = document.getElementById('language').value;
 		document.getElementById('evaluating').style.display='flex';
 		document.getElementById('test_result').style.display='none';
 		document.getElementById('submission_result').style.display='none';
 		document.getElementById('sb_err').style.display='none';
 		// console.log(language);
 		try {
	 		$.post( "problem", { command: "submit_code", sourceCode: code,lang: language, problem: pid }, function(data) {
	 			//console.log(data);
	 			
	 			data = JSON.parse(data);
	 			var error = '';
	 			
				//console.log(data);
				document.getElementById('evaluating').style.display='none';	
				document.getElementById('submission_result').style.display='block';
				var c=0;
				for(var i=0;i<data.length;++i) {
					curr = JSON.parse(data[i]);
					cmpFail = curr['cmpFailed'][0];
					cmpError = curr['cmpError'];
					rntError = curr['rntError'];
					output = curr['result'];
					var id=i+1;
					id='submission_res'+id;
					if(cmpFail === true) {
						error=cmpError[0];
						document.getElementById(id).innerHTML = 'CE';
					} else if(rntError == null) {
						document.getElementById(id).innerHTML = output[0];
						if(output[0]==='AC') {
							c+=1;
						}
					} else {
						document.getElementById(id).innerHTML = 'RE';
						error=rntError[0];
					}
				}
				score = 'Score:'+c*20;
				document.getElementById('submission_score').innerHTML=score;
				if(error==='') {
					//nothing
				} else {
					document.getElementById('sb_err').style.display='block';
					document.getElementById('submission_error').innerHTML = 'Error:\n'+error;
					auto_grow(document.getElementById('submission_error'));
				}
			});
 		} catch(err) {
 			console.log(err);
 		}
 	}
 	
 	

</script>
</c:if>
<c:if test="${CONTEST.end<CURRENT_TIMESTAMP }">
<br><br><br><hr>
		<button class="btn btn-primary" type="submit" onclick='downloadTestFiles("${PROBLEM.id}")'>Download Test Files</button>
		
		<script type="text/javascript" 
		src="public/jszip.min.js"></script>
		<script type="text/javascript" 
		src="public/FileSaver.min.js"></script>
		<script>
			function downloadTestFiles(id) {
				$.post( "problem", { command: "download_test_files", problem: id }, function(data) {
					data=JSON.parse(data);
					//console.log(data);
					var err=data.error[0];
					
					if(err) {
						alert('Download Error');
					} else {
						var zip = new JSZip();
						keys = Object.keys(data);
						//console.log(keys);
						for(var i=0;i<keys.length;++i) {
							if(keys[i]!=='error') {
								zip.file(keys[i]+'.txt',data[keys[i]][0]);
							}
						}
						zip.generateAsync({type:"blob"}).then(function(content) {
						    // see FileSaver.js
						    saveAs(content, "problem"+id+"_tcs.zip");
						});
					}
					
				});
			}
		</script>
</c:if>
		<br>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
	</div>
</body>
</html>