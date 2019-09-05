<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Accounts-Codecozy</title>
		<%@include file="ui-headings.html" %>
	</head>
	<body>
		<div class="jumbotron" style="background:#03A9F4;color:white;border-radius:0px">
			<h2>Accounts</h2>
		</div>
		<div class="container">
			<h2>Reset Password</h2>
			<form method="post">
			  	<br>
			  	<c:if test="${MESSAGE!=null }">
			  		<h4>${ MESSAGE }</h4>
			  		<br>
			  		<button class="btn btn-warning" style="background:#FF9800;border:#FF9800" onclick="window.location.replace('accounts')">Go To Login Page</button>
				</c:if>
			  	<ul>
				  	<c:if test="${ERROR!=null }">
				  		<li style="color:red">Error: ${ ERROR }</li>
				  	</c:if>
			  	</ul>
			  	<input type="hidden" name="command" value="resetPassword">
			  	<input type="hidden" name="user" value="${ USER }">
				<div class="form-group">
				    <label for="rst_password">Password</label>
				   	<input type="password" class="form-control" id="rst_password" name="rst_password" required>
				</div>
				<div class="form-group">
				    <label for="rst_cpassword">Confirm Password</label>
				    <input type="password" class="form-control" id="rst_cpassword" name="rst_cpassword" required>
				</div>
				<button class="btn btn-primary btn-lg" type="submit" style="background:#FF9800;border:#FF9800">Reset Password</button>
			</form>
		</div>
		<br><br><br><br>
	</body>
</html>