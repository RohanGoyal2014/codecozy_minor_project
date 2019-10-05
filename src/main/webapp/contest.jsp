<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contest-Codecozy</title>
<%@include file="ui-headings.html" %>
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	<div class="container-fluid">
	
	<button type="button" class="btn btn-link" style="font-size:20px" onclick="window.location.href='dashboard'">&lt;Go Back</button>
		<br>
	
		<div class="jumbotron">
		<h2><b>${CONTEST.name }</b></h2>
		<a class="btn-link" href="leaderboard?id=${CONTEST.id }"target="_blank">View Leaderboard</a>
		</div>
	<c:if test="${ CONTEST.start<=CURRENT_TIMESTAMP }">
			<div style="position:fixed;bottom:0;right:0;padding:15px;background:#EEEEFF;border:5px solid black"><h4 id="time_rem"></h4>
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
		<c:if test="${ CONTEST.end < CURRENT_TIMESTAMP }">
			<a href="${ CONTEST.editorialLink }" target="_blank" class="btn-link"><h5>Editorial</h5></a>
		</c:if>
	
		<c:if test="${CONTEST.start <= CURRENT_TIMESTAMP }">
			<br>
			<table class="table">
			<c:forEach var="problem" items="${ PROBLEMS }">
				<tr>
					<td>${ problem.name }</td>
					<td><a href="problem?id=${ problem.id }" class="btn-link">Solve</a></td>
				</tr>
			</c:forEach>
			</table>
		</c:if>
		<br>
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
	</div>
</body>
</html>