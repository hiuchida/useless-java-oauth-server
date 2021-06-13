<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%
String client_name = (String) request.getAttribute("client_name");
List<String> scopes = (List<String>) request.getAttribute("scopes");
%>
<!DOCTYPE html>
<html>
	<head>
        <meta charset="UTF-8" />
		<title>Authorization Page</title>
	</head>
	<body class="font">
		<h2>Client Application</h2>
		<%= client_name %>

		<h2>Requested Permissions</h2>
		<% if (scopes != null) { %>
		<ol>
			<% for (String scope : scopes) { %>
			<li><%= scope %></li>
			<% } %>
		</ol>
		<% } %>

		<h2>Approve?</h2>
			<form method="post" action="decision">
				<input  type="text"     name="login_id" placeholder="Login ID"><br>
				<input  type="password" name="password" placeholder="Password"><br>
				<button type="submit" name="approved" value="true">Approve</button>
				<button type="submit" name="denied"   value="true">Deny</button>
			</form>
	</body>
</html>
