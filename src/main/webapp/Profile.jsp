<%--
  User: Ryan
  Date: 17/10/2015
  Time: 15:41
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="Styles.css"/>
    <title>JSP Page</title>
</head>
<body>
<nav>
    <ul>
        <li><a href="upload.jsp">Upload</a></li>
        <li><a href="Logout.jsp">Logout</a></li>
    </ul>
</nav>

<%
    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
    if (lg != null) {
        if (lg.getlogedin()) {


%>

<ul>
    <li>First Name:</li>
    <li>Last Name:</li>
    <li>Email:</li>
</ul>

<%
        } else {
            response.sendRedirect("index.jsp");
        }
    }
%>


</body>
</html>
