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
			<nav>
				<div class="nav nav-tabs" id="nav-tab" role="tablist">
		    		<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true">Login</a>
		    		<a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false">Register</a>
		    	</div>

			</nav>
			<div class="tab-content" id="nav-tabContent">
			  	<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
			  		<form method="post">
			  			<br>
			  			<c:if test="${ MODE == 1 }">
				  			<ul>
				  				<li> Error: ${ ERROR }</li> 
				  			</ul>
			  			</c:if>
			  			<input type="hidden" name="command" value="login">
					  	<div class="form-group">
					    	<label for="lg_email">Email</label>
					    	<input type="email" class="form-control" id="lg_email" name="lg_email" required>
					  	</div>
					  	<div class="form-group">
					    	<label for="lg_password">Password</label>
					    	<input type="password" class="form-control" id="lg_password" name="lg_password" required>
					  	</div>
					  	<button class="btn btn-primary" type="submit" style="background:#FF9800;border:#FF9800">Login</button>
					</form>
			  	</div>
			  	<div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
			  		<form method="post">
			  			<br>
			 			<strong>
			  				All fields are required
			  			</strong>
			  			<br>
			  			<br>
			  			<c:if test="${ MODE == 2 }">
				  			<c:if test="${ERROR != null }">
					  			<ul>
					  				<li style="color:white">Error: ${ ERROR } </li>
					  			</ul>
			  				</c:if>
			  				<h4> ${MESSAGE}</h4>
					  	</c:if>
			  			
			  			<br>
			  			<input type="hidden" name="command" value="register">
					  	<div class="form-group">
					    	<label for="rg_fname">First Name</label>
					    	<input type="text" class="form-control" id="rg_fname" name="rg_fname" required>
					  	</div>
					  	<div class="form-group">
					    	<label for="rg_lname">Last Name</label>
					    	<input type="text" class="form-control" id="rg_lname" name="rg_lname" required>
					  	</div>
					  	<div class="form-group">
						  	<label for="rg_gender">Gender:</label>
						  	<select class="form-control" id="rg_gender" name="rg_gender">
						    	<option value="m">Male</option>
							    <option value="f">Female</option>
						  	</select>
						</div>
					  	<div class="form-group">
					    	<label for="rg_email">Email</label>
					    	<input type="email" class="form-control" id="rg_email" name="rg_email" required>
					  	</div>
					  	<div class="form-group">
					    	<label for="rg_uname">Preferred Username</label>
					    	<input type="text" class="form-control" id="rg_uname" name="rg_uname" required>
					  	</div>
					  	<div class="form-group">
					    	<label for="rg_password">Password</label>
					    	<input type="password" class="form-control" id="rg_password" name="rg_password" required>
					  	</div>
					  	<div class="form-group">
					    	<label for="rg_cpassword">Confirm Password</label>
					    	<input type="password" class="form-control" id="rg_cpassword" name="rg_cpassword" required>
					  	</div>
					  	
					  		<button class="btn btn-primary" type="submit" style="background:#FF9800;border:#FF9800">Register</button>
					</form>
			  	</div>
			</div>
		</div>
		<c:if test="${ MODE == 2 }">
		    <script>
		    	function simulateClick() {
		   			document.getElementById('nav-profile-tab').click();
		    	}
		    	simulateClick();
		    			
		    </script>
		</c:if>
	</body>
</html>