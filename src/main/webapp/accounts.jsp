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
		    		<a class="nav-item nav-link" id="nav-forgot-tab" data-toggle="tab" href="#nav-forgot" role="tab" aria-controls="nav-forgot" aria-selected="false">Forgot Password</a>
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
					  				<li style="color:red">Error: ${ ERROR } </li>
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
					    	<input type="text" class="form-control" id="rg_uname" onkeyup="checkUsername(this)" name="rg_uname" required>
					  		<p id="usernames" style="display:none" align="right">...</p>
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
			  	<div class="tab-pane fade" id="nav-forgot" role="tabpanel" aria-labelledby="nav-forgot-tab">
			  		<form method="post">
			  			<br>
			  			<b>If your email exists we will send you a reset mail</b>
			  			<c:if test="${ MODE == 3 }">
				  			<h4> Email Sent. If you do not receive mail, try entering your email again or contact administrator </h4>
			  			</c:if>
			  			<input type="hidden" name="command" value="forgot">
					  	<div class="form-group">
					    	<label for="fg_email">Email</label>
					    	<input type="email" class="form-control" id="fg_email" name="fg_email" required>
					  	</div>
					  	<button class="btn btn-primary" type="submit" style="background:#FF9800;border:#FF9800">Forgot Password</button>
					</form>
			  	</div>
			</div>
		</div>
		<br><br><br><br>
		<c:choose>
			<c:when test="${ MODE == 2 }">
			    <script>
			    	function simulateClick() {
			   			document.getElementById('nav-profile-tab').click();
			    	}
			    	simulateClick();
			    			
			    </script>
			</c:when>
			<c:when test="${ MODE == 3 }">
			    <script>
			    	function simulateClick() {
			   			document.getElementById('nav-forgot-tab').click();
			    	}
			    	simulateClick();
			    			
			    </script>
			</c:when>
		</c:choose>
		<script>
			function checkUsername(obj) {
				document.getElementById('usernames').innerHTML = "Checking...";
				document.getElementById('usernames').style.display = "block";
				var text = obj.value;
				//console.log(text);
				var found = false;
				if(text.length === 0) {
					document.getElementById('usernames').style.display = "none";
				}
				$.post( "accounts", { command: "api_usernames"}, function(data) {
					//console.log(data);
					data = JSON.parse(data);
					for(var i=0;i<data.length;++i) {
						if(data[i] === text) {
							//console.log('match');
							found = true;
							break
						}
					}
					if(found === true) {
						document.getElementById('usernames').innerHTML="Not Allowed";	
					} else {
						document.getElementById('usernames').innerHTML="Allowed";
					}
				});
			}
		</script>
	</body>
</html>