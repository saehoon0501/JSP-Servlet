<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

	<head>
		<title>Student Tracker App</title>
		
		<link type="text/css" rel="stylesheet" href="css/style.css">
	</head>
	
	<body>
	
		<div id="wrapper">
			<div id="header">
				<h2>FooBar Univ</h2>
			</div>
		</div>
		
		<div id="container">
			<div id="content">
				
				<input type="button" value="Add student" onclick="window.location.href='add-student-form.jsp'; return false"
					class="add-student-button"/>							
				
				<table>
					<tr>
						<th>First name</th>
						<th>Last name</th>
						<th>E-mail</th>
						<th>Action</th>
					</tr>
				
				
					<c:forEach var="tempStudent" items="${STUDENT_LIST}">
						<c:url var="updateLink" value="StudentControllerServlet">
							<c:param name="command" value="LOAD"/>	
							<c:param name="studentId" value="${tempStudent.id}"/>
						</c:url> 						
						<c:url var="deleteLink" value="StudentControllerServlet">
							<c:param name="command" value="DELETE"/>
							<c:param name="studentId" value="${tempStudent.id}"/>								
						</c:url>
						
						<tr>
							<td>${tempStudent.firstName}</td>
							<td>${tempStudent.lastName}</td>
							<td>${tempStudent.email}</td>
							<td><a href="${updateLink}">Update</a> | <a href="${deleteLink}">Delete</a></td>						
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</body>
</html>