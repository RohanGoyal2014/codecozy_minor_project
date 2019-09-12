<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin-Codecozy</title>
<%@include file="ui-headings.html" %>
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	
	<div class="container">
	
		<button type="button" class="btn btn-link" style="font-size:20px" onclick="window.location.href='dashboard'">&lt;Go Back</button>
		<br><br>
		
		<ul class="nav nav-tabs">
			<li class="nav-item">
		    	<a class="nav-link active" data-toggle="tab" href="#home">Add Contest</a>
		  	</li>
		  	<li class="nav-item">
		    	<a class="nav-link" data-toggle="tab" href="#remove" id="remove_tab">Remove Contest</a>
		  	</li>
		  	<c:if test="${ IS_SUPERADMIN }">
				<li class="nav-item">
			    	<a class="nav-link" data-toggle="tab" href="#menu1" id="manage_admin_tab">Manage Admins</a>
			  	</li>
		  	</c:if>
		</ul>
		
		<!-- Tab panes -->
		<div class="tab-content">
		  	<div class="tab-pane container-fluid active" id="home">
			  	<br>
			  	<c:if test="${MODE ==1 }">
			  		<c:if test="${ERROR!=null }">
						<ul>
							<li style="color:red">${ ERROR }</li>
						</ul>
					</c:if>
					<h4>${ MESSAGE }</h4>
			  	</c:if>
			  	<br>
			  	<form method="post"enctype="multipart/form-data">
			  		<input type="hidden" name="command" value="add_contest">
			  		<div class="form-group">
						<label for="ct_name">Name</label>
						<input type="text" class="form-control" id="ct_name" name="ct_name" required>
					</div>
					<div class="form-group">
						<label for="ct_start">Start</label>
						<input type="datetime-local" class="form-control" id="ct_start" name="ct_start" required>
					</div>
					<div class="form-group">
						<label for="ct_end">End</label>
						<input type="datetime-local" class="form-control" id="ct_end" name="ct_end" required>
					</div>
					<div class="form-group">
						<label for="ct_editorial">Editorial Drive/Dropbox Link</label>
						<input type="text" class="form-control" id="ct_editorial" name="ct_editorial" required>
					</div>
					<div style="margin-top:30px;">
						<div class="form-group">
							<h4>PROBLEM 1</h4>
							<label for="pb_name_1">Problem Name</label>
							<input type="text" class="form-control" id="pb_name_1" name="pb_name_1" required>
						</div>
						<div class="form-group">
							<label for="pb_link_1">Problem Drive/Dropbox Link</label>
							<input type="text" class="form-control" id="pb_link_1" name="pb_link_1" required>
						</div>
						<div class="form-group">
							<label>Upload exactly 5 I/O Files<br>
							Input Files must be named as input_1.txt input_2.txt ... uptil input_5.txt and<br>
							Output Files must be named as output_1.txt,output_2.txt ... uptil output_5.txt respectively
							</label>
							<input type="file" name="input1" style="display:none" onchange="showUploadedFiles(1,1)" id="input1" multiple required>
							<input type="file" name="output1" style="display:none" id="output1" onchange="showUploadedFiles(1,2)" multiple required>
							<div class="row">
								<div class="col">
									<button type="button" class="btn btn-outline-primary btn-lg btn-block" onclick="triggerInputUpload(1)">Upload Inputs</button>
									<p id="files11"></p>
								</div>
								<div class="col">
									<button type="button" class="btn btn-outline-primary btn-lg btn-block" onclick="triggerOutputUpload(1)">Upload Outputs</button>
									<p id="files12"></p>
								</div>
							</div>
						</div>
					</div>
					<div style="margin-top:30px;">
						<div class="form-group">
							<h4>PROBLEM 2</h4>
							<label for="pb_name_2">Problem Name</label>
							<input type="text" class="form-control" id="pb_name_2" name="pb_name_2" required>
						</div>
						<div class="form-group">
							<label for="pb_link_2">Problem Drive/Dropbox Link</label>
							<input type="text" class="form-control" id="pb_link_2" name="pb_link_2" required>
						</div>
						<div class="form-group">
							<label>Upload exactly 5 I/O Files<br>
							Input Files must be named as input_1.txt input_2.txt ... uptil input_5.txt and<br>
							Output Files must be named as output_1.txt,output_2.txt ... uptil output_5.txt respectively
							</label>
							<input type="file" name="input2" style="display:none" onchange="showUploadedFiles(2,1)" id="input2" multiple required>
							<input type="file" name="output2" style="display:none" onchange="showUploadedFiles(2,2)"id="output2" multiple required>
							<div class="row">
								<div class="col">
									<button type="button" class="btn btn-outline-primary btn-lg btn-block" onclick="triggerInputUpload(2)">Upload Inputs</button>
									<p id="files21"></p>
								</div>
								<div class="col">
									<button type="button" class="btn btn-outline-primary btn-lg btn-block" onclick="triggerOutputUpload(2)">Upload Outputs</button>
									<p id="files22"></p>
								</div>
							</div>
						</div>
					</div>
					<br><br>
					<button type="submit" class="btn btn-primary btn-lg btn-block">Create</button>
			  	</form>
			  	<br>
		  </div>
			<div class="tab-pane container-fluid fade" id="remove">
				<br>
		  
		  		<c:if test="${MODE ==2 }">
		  			<c:if test="${ERROR!=null }">
						<ul>
							<li style="color:red">${ ERROR }</li>
						</ul>
					</c:if>
					<h4>${ MESSAGE }</h4>
					<br>
		  		</c:if>
		  
		  		<c:choose>
					<c:when test="${ CONTESTS.size() == 0 }">
						No Contests to show...
					</c:when>
					<c:otherwise>
						<form method="post"name="del_form">
							<input type="hidden" name="command" value="remove_contest">
							<input type="hidden" id="ct_to_delete" name="ct" value="">
						</form>
						<ol>
							<c:forEach var="pc" items="${ CONTESTS }">
								<li>
									<h5>${ pc.name }</h5>
									<b>Start Time:</b> ${ pc.startDateTime }
								</li>	
								<a href="#" style="color:#03A9F4" onclick="deleteContest(${pc.id})">Delete</a>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>
		  		<br>
		  </div>
		  <c:if test="${ IS_SUPERADMIN }">
		  	<div class="tab-pane container-fluid fade" id="menu1">
		  		<br>
		  
		  		<c:if test="${MODE ==3 }">
		  			<c:if test="${ERROR!=null }">
						<ul>
							<li style="color:red">${ ERROR }</li>
						</ul>
					</c:if>
					<h4>${ MESSAGE }</h4>
					<br>
		  		</c:if>
		  		<br>
		  		<form method="post" name="add_admin_form">
					<input type="hidden" name="command" value="add_admin">
					<p id="suggestions"></p>
					<br>
					<input type="text" class="form-control" name="username" id="username" placeholder = "Enter Username" onkeyup="showSuggestions(this)" required>
					<br>
					<button class="btn btn-primary">Make Admin</button>
				</form>
				<br>
				<hr>
				<br>
		  		
		  		<c:choose>
					<c:when test="${ ADMINS.size() == 0 }">
						No Admins as of now...
					</c:when>
					<c:otherwise>
						<form method="post"name="rem_admin_form">
							<input type="hidden" name="command" value="remove_admin">
							<input type="hidden" id="admin_to_delete" name="admin" value="">
						</form>
						<ol>
							<c:forEach var="pc" items="${ ADMINS }">
								<li>
									<h5>${ pc.fname } ${ pc.lname }</h5>
									<b>Username:</b> ${ pc.username } <br>
									<b>Email:</b> ${ pc.email }									
								</li>	
								<a href="#" style="color:#03A9F4" onclick="removeAdmin('${ pc.username }')">Remove</a>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>
		  	
		  	</div>
		  </c:if>
		  
		</div>
		
	</div>
	<script>
		function triggerInputUpload(c) {
			var id = '#input'+c;
			$(id).click();
		}
		function triggerOutputUpload(c) {
			var id='#output'+c;
			$(id).click();
		}
		function showUploadedFiles(problem, io) {
			var id='';
			if(io=== 1) {
				id+="input";
			} else {
				id+="output";
			}
			var pxy='files'+problem+io;
			id+=problem;
			var files = document.getElementById(id).files;
			if(files.length === 0) {
				document.getElementById(pxy).innerHTML="<b>No Files Chosen</b>";
			} else {
				lst="<b>Uploaded Files</b><ul>"
				for(var i=0;i<files.length;++i) {
					lst+="<li>"+files[i].name+"</li>"
				}
				document.getElementById(pxy).innerHTML=lst;
			}
		}
		function deleteContest(id) {
			document.del_form.ct.value = id;
			//console.log(document.del_form.ct.value);
			document.del_form.submit();
		}
		
		function removeAdmin(username) {
			document.rem_admin_form.admin.value = username;
			document.rem_admin_form.submit();
		}
		
		function showSuggestions(obj) {
			document.getElementById('suggestions').innerHTML = "Loading...";
			var text = obj.value;
			//console.log(text.length);
			if(text.length === 0) {
				document.getElementById('suggestions').innerHTML = "";
				return;
			}
			$.post( "accounts", { command: "api_usernames"}, function(data) {
				console.log(data);
				var matches = [];
				data = JSON.parse(data);
				for(var i=0;i<data.length;++i) {
					if(data[i].indexOf(text)==0) {
						matches.push(data[i]);
					}
				}
				if(matches.length === 0) {
					//console.log(text.length);
					document.getElementById('suggestions').innerHTML = "No such users...";
				} else {
					document.getElementById('suggestions').innerHTML="<b>Suggestions:</b>"+matches;
				}
			});
		}
		
	</script>
	<c:choose>
			<c:when test="${ MODE == 2 }">
			    <script>
			    	function simulateClick() {
			   			document.getElementById('remove_tab').click();
			    	}
			    	simulateClick();
			    			
			    </script>
			</c:when>
			<c:when test="${ MODE == 3 }">
			    <script>
			    	function simulateClick() {
			   			document.getElementById('manage_admin_tab').click();
			    	}
			    	simulateClick();
			    			
			    </script>
			</c:when>
		</c:choose>
</body>
</html>