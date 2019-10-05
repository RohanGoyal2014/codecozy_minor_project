<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
	
		<meta charset="UTF-8">
		<title>Profiles-Codecozy</title>
		<%@include file="ui-headings.html" %>
		
	</head>
	<body>
		<div class="jumbotron" style="background:#03A9F4;color:white;border-radius:0px">
			<h2>Profiles</h2>
		</div>
		
		<div class="container">
		<button type="button" class="btn btn-link" style="font-size:20px" onclick="window.location.href='dashboard'">&lt;Go Back</button>
		<br>
			<img src="public/coder.png"style="width:150px;margin-left:auto;margin-right:auto;display:block">
			<br><br>
			<table class="table table-bordered">
			    <tbody>
			      <tr>
			        <td><b>Name</b></td>
			        <td>${ USER.fname } ${ USER.lname }</td>
			      </tr>
			      <tr>
			        <td><b>Email</b></td>
			        <td>${USER.email }</td>
			      </tr>
			      <tr>
			        <td><b>Username</b></td>
			        <td>${USER.username }</td>
			      </tr>
			      <tr>
			        <td><b>Gender</b></td>
			        <td id="gender" onload="edit()">${USER.gender }</td>
			      </tr>
			      <tr>
			      	<td colspan="2">
			      	<b>Submissions: </b>
			      	<p id="participated_contests">
			      		<c:choose>
				      		<c:when test="${CONTESTS_PARTICIPATED.isEmpty() }">
				      			No Participation...
				      		</c:when>
				      		<c:otherwise>
				      			<c:forEach var="cp" items="${CONTESTS_PARTICIPATED.entrySet() }">
				      			
				      				<a href="contests?id=${cp.getKey() }">Contest ${cp.getKey() } &nbsp;&nbsp; </a>
				      				
				      			</c:forEach> 
				      		</c:otherwise>
			      		</c:choose>
			      	</p>
			      	</td>
			      </tr>
			    </tbody>
			  </table>
			  <div id="plot"></div>
			  <button id="btn_plot" style="display:none"onclick='plot(${CONTESTS_PARTICIPATED.keySet()},${CONTESTS_PARTICIPATED.values() })'>Load</button>
			  <script>
			  document.onload = $('#btn_plot').click();
			  function plot(keys,values) {
				  console.log(keys,values);
				  var trace1 = {
						  x: keys,
						  y: values,
						  type: 'scatter',
						};
				  var layout = {
						  title: 'Performance',
						  xaxis: {
						    title: 'Contest IDs'
						  },
						  yaxis: {
						    title: 'Score'
						  }
						};
					  var data = [trace1];
					  console.log(data);
		
					  Plotly.newPlot('plot', data, layout, {showSendToCloud: true});  
			  }
			  
			  </script>
		</div>
		<br><br><br><br>
		<script>
			function edit() {  
				var gen=document.getElementById('gender').innerHTML;
				//console.log(gen);
				if(gen==="m") {
					document.getElementById('gender').innerHTML="Male";
				} else if(gen=="f") {
					document.getElementById('gender').innerHTML="Female";
				}
			}
			edit();
		</script>
		

	</body>
</html>