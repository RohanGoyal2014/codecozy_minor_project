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
			<img src="public/coder.png"style="width:150px;margin-left:auto;margin-right:auto;display:block">
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
			    </tbody>
			  </table>
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