<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Verified-Codecozy</title>
		<%@include file="ui-headings.html" %>
	</head>
	<body>
		<div class="container" style="margin-top:30vh">
			<h2>You have been verified!!</h2>
				<button class="btn btn-warning btn-lg" onclick="redirect()">Click to Move to Website</button>
		</div>	
		<script>
			function redirect() {
				window.location.replace("accounts");
			}
		</script>
	</body>
</html>