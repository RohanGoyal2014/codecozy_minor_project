<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Verify-Codecozy</title>
		<%@include file="ui-headings.html" %>
	</head>
	<body>
		<div class="jumbotron" style="background:#03A9F4;color:white;border-radius:0px">
			<h2>Verify Account</h2>
		</div>
		<div class="container">
			<h4> Umm, looks like your account is not verified.</h4>
			<h5> 
				Click on the send button below and we shall send you an email.
				Follow the link provided in the email to verify your account.
				Then come and click the "Refresh" button below to proceed to site. 
				<br>
			</h5>
			<form method="post">
				<input type="hidden" name="command" value="verifyAccount">
				<button type="submit" class="btn btn-primary" style="background:#FF9800;border:#FF9800">Send Verification Mail</button>
			</form><br>
			${ MESSAGE }<br>
			<br>
			<button class="btn btn-outline-primary" onclick="window.location.reload()">Refresh</button>
		</div>
		<br>
		<br><br><br>
	</body>
</html>