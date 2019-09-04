<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Home-Codecozy</title>
		<%@include file="ui-headings.html" %>
	</head>
	<body>
		<div class="container" style="margin-top:30vh">
			<img src="public/logo.png"style="margin-left:auto;margin-right:auto;display:block">
			<div class="text-center">
				<button class="btn btn-warning btn-lg" onclick="redirect()">Enter</button>
			</div>
		</div> 		
		<script>
			function redirect() {
				window.location.replace("accounts");
			}
		</script>
	</body>
</html>