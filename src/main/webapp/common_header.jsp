<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <nav class="navbar navbar-expand-md navbar-light bg-light fixed-top">
    <a href="#" class="navbar-brand">
        <img src="public/logo.png" height="60">
    </a>
    <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarCollapse">
        <div class="navbar-nav">
            <a href="dashboard" class="nav-item nav-link">Contests</a>
            <a href="profile" class="nav-item nav-link">My Profile</a>
            <a href="search" class="nav-item nav-link">Search</a>
        </div>
        <div class="navbar-nav ml-auto">
        	<form method="post" action="accounts" id="logoutForm">
        		<input type="hidden" name="command" value="logout">
        	</form>
            <a href="#" class="nav-item nav-link" onclick="document.getElementById('logoutForm').submit()">Logout</a>
        </div>
    </div>
</nav>