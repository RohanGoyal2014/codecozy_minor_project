<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard-Codecozy</title>
<%@include file="ui-headings.html" %>
</head>
<body>
	<%@include file="common_header.jsp" %>
	<br><br><br><br><br><br><br>
	<div class="container-fluid">
	
		<c:if test="${IS_ADMIN == true }">
			<button class="btn btn-primary" style="background:#03A9F4;border:#03A9F4" onclick="window.location.href='admin'">Admin Panel</button>
		</c:if>
	
		<br><br>
		
		<ul class="nav nav-tabs">
		  <li class="nav-item">
		    <a class="nav-link active" data-toggle="tab" href="#home">Ongoing Contests</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" data-toggle="tab" href="#menu1">Upcoming Contests</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" data-toggle="tab" href="#menu2">Past Contests</a>
		  </li>
		</ul>
		
		<!-- Tab panes -->
		<div class="tab-content">
		  <div class="tab-pane container-fluid active" id="home">
		  <br>
		  	<c:choose>
					<c:when test="${ ONGOING_CONTEST_LIST.size() == 0 }">
						No Contests to show...
					</c:when>
					<c:otherwise>
						<ol>
							<c:forEach var="pc" items="${ ONGOING_CONTEST_LIST }">
								<li>
									<h5>${ pc.name }</h5>
									<b>Start Time:</b> ${ pc.startDateTime }
								</li>	
								<a href="contests/id=${ pc.id }" style="color:#03A9F4">Enter</a>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>
		  </div>
		  <div class="tab-pane container-fluid fade" id="menu1">
		  	<br>
		  		<c:choose>
					<c:when test="${ UPCOMING_CONTEST_LIST.size() == 0 }">
						No Contests to show...
					</c:when>
					<c:otherwise>
						<ol>
							<c:forEach var="pc" items="${ UPCOMING_CONTEST_LIST }">
								<li>
									<h5>${ pc.name }</h5>
									<b>Start Time:</b> ${ pc.startDateTime }
								</li>	
								<a href="contests/id=${ pc.id }" style="color:#03A9F4">Enter</a>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>
		  
		  </div>
		  <div class="tab-pane container-fluid fade" id="menu2">
		  		<br>
				<c:choose>
					<c:when test="${ PAST_CONTEST_LIST.size() == 0 }">
						No Contests to show...
					</c:when>
					<c:otherwise>
						<ol>
							<c:forEach var="pc" items="${ PAST_CONTEST_LIST }">
								<li>
									<h5>${ pc.name }</h5>
									<b>Start Time:</b> ${ pc.startDateTime }
								</li>	
								<a href="contests/id=${ pc.id }" style="color:#03A9F4">Enter</a>
							</c:forEach>
						</ol>
					</c:otherwise>
				</c:choose>
		  
		  </div>
		</div>
		
	</div>
</body>
</html>