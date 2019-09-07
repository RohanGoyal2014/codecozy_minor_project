<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<style>
	.myInputClass {
		border-radius:0px;
		padding:10px;
		width: 100%;
		font-size:24px;
		outline:none;
		border:0.5px solid #EEEEEE;
	}
</style>
<meta charset="UTF-8">
<title>Search-Codecozy</title>
<%@include file="ui-headings.html" %>
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	<div class="container-fluid">
	
		<input type="text" class="myInputClass" placeholder="Search Usernames" onkeyup="showSuggestions(this)">
		<div id="suggestions"class="container-fluid"style="border:0.5px solid #EEEEEE;padding:0px;font-size:24px">
		</div>
		
	</div>
	<script>
		function showSuggestions(obj) {
			document.getElementById('suggestions').innerHTML = "<p style=\"padding-left:10px\">Loading...</p>";
			var text = obj.value;
			console.log(text.length);
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
					document.getElementById('suggestions').innerHTML = "<p style=\"padding-left:10px\">No Matches Found!</p>";
				} else {
					document.getElementById('suggestions').innerHTML="";
					var last="";
					for(var i=0;i<matches.length;++i) {
						last=last+
							"<div class=\"container-fluid\"style=\"padding:10px;border:0.1px solid #EEEEEE;cursor:pointer;\" onMouseOut=\"this.style.background='#FFFFFF'\" onMouseOver=\"this.style.background='#EEEEEE'\" onclick=\"window.location.href='profile/"+ matches[i] +"'\">"+ matches[i] +"</div>";
						document.getElementById('suggestions').innerHTML=last;
					}
				}
			});
		}
		
	</script>
</body>
</html>