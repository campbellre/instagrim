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
    <style type="text/css">
        p {
            color: red;
        }
    </style>
</head>
<body>
<nav>
    <ul>
        <li><a href="upload.jsp">Upload</a></li>
        <li><a href="Logout.jsp">Logout</a></li>
            <%
    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
    if (lg != null) {
        if (lg.getlogedin()) {
            String user = (String) request.getAttribute("Userpath");

            if (user == null) {
            // This is sending to /instagrim/profile/index.jsp
                response.sendRedirect("index.jsp");
            }
            else{
                if(lg.getUsername() == request.getAttribute("Userpath")){
                %>

                    <div class="Edit">
                         <a href="ProfileEdit.jsp">Edit Profile</a>
                    </div>
                <%
                }
                %>
                <ul>
                    <li>Username: <%=request.getAttribute("Userpath").toString()%>
                    </li>
                    <li>First Name: <%=request.getAttribute("Firstname").toString()%>
                    </li>
                    <li>Last Name: <%=request.getAttribute("Lastname").toString()%>
                    </li>
                    <li>Email: <%=request.getAttribute("Email").toString()%>
                    </li>
                </ul>
            <%
            }
            %>
            <%
        } else {
            response.sendRedirect("index.jsp");
        }
    }
    else{
    %>
        <p>Error</p>
    <%
    }
    %>


</body>
</html>
