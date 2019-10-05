<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leaderboard-Codecozy</title>
<%@include file="ui-headings.html" %>
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	<div class="container-fluid">
	
	<button type="button" class="btn btn-link" style="font-size:20px" onclick="window.location.href='contests?id=${CONTEST}'">&lt;Go Back</button>
		<br>
		<br>
		<h2 id="your_rank">Your Rank: N/A</h2>
		<h3 id="your_score">Score: 0</h3>
		<a class="btn-link" href="#"onclick="reloadLeaderboard('${CONTEST}')"id="refresh">Refresh</a>
		<table class="table">
			<thead>
				<tr>
					<th>Rank</th>
					<th>User</th>
					<th>Score</th>
				</tr>
			</thead>
			<tbody id="board">
				
			</tbody>
		</table>
	
		<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
	</div>
	<script>
		document.onload = $('#refresh').click();
		function reloadLeaderboard(cid) {
			console.log('reloading');
			$.post( "leaderboard", { command: "get_ranklist", contest: cid }, function(data) {
				data = JSON.parse(data);
				console.log(data);
				user = data.first;
				data = data.second;
				if(data.length === 0) {
					document.getElementById('board').innerHTML='<h4>No Participation So Far...</h4>';
				} else {
					var s='';
					var done=false;
					for(var i=0;i<data.length;++i) {
						if(data[i].first.email === user) {
							document.getElementById('your_rank').innerHTML='Your Rank: '+(i+1);
							document.getElementById('your_score').innerHTML='Your Score: '+data[i].second.first;
							done=true;
						}
						s+='<tr>';
						s+='<td>';
						s+=(i+1);
						s+='</td>';
						s+='<td>';
						s+='<a href="profile?user='+data[i].first.username+'"target="_blank" class="btn-link">';
						s+=data[i].first.username;
						s+='</a>';
						s+='</td>';
						s+='<td>';
						s+=data[i].second.first;
						s+='</td>';
						s+='</tr>';
					}
					document.getElementById('board').innerHTML=s;
					if(!done) {
						document.getElementById('your_rank').innerHTML='Your Rank: '+N/A;
						document.getElementById('your_score').innerHTML='Your Score: '+0;
					}
				}
				
				
			});
		}
	</script>
</body>
</html>