<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
String context = request.getContextPath();
String uri = request.getRequestURI();
String url = request.getRequestURL().toString();
int idx = url.indexOf(uri);
String baseurl = url.substring(0, idx) + context;
String usage = baseurl + "/authorization?response_type=code&client_id=1&redirect_uri=http://example.com/&scope=read+write&state=mystate";
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>useless-java-oauth-server</title>
    </head>

    <body>
        <h1>useless-java-oauth-server</h1>
        <h2>Usage</h2>
        Authorization Request:<br/>
        <a href="<%= usage %>"><%= usage %></a>
    </body>

</html>
