<%--
  User: Ryan
  Date: 17/10/2015
  Time: 15:41
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.lib.Default" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.UserDetails" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="Styles.css"/>
    <title>Profile</title>
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
        <li><a href="<%=Default.URL_ROOT%>/logout">Logout</a></li>
    </ul>
</nav>
<%
    LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
    if (lg != null) {
        String user = (String) session.getAttribute("UserPath");

        if (user == null) {
            // This is sending to /instagrim/profile/index.jsp
            response.sendRedirect("index.jsp");
        } else {
            UserDetails ud = (UserDetails) session.getAttribute("UserDetails");

            if (ud == null) {
                response.sendRedirect("index.jsp");
            }

            if (lg.getUsername().equals(user)) {
            %>

            <div class="Edit">
                <a href="<%=Default.URL_ROOT%>/ProfileEdit/<%=lg.getUsername()%>">Edit Profile</a>
            </div>
            <%
            }
            if (ud.getProfilepicUUID().equals("")) {
            %>
                <img src="<%=Default.URL_ROOT%>/user.png"/>
            <%
            } else {
            %>
                <img src="<%=Default.URL_ROOT%>/Image/<%=ud.getProfilepicUUID()%>"/>
            <% } %>
                <ul>
                    <li>Username: <%=user%>
                    </li>
                    <li>First Name: <%=(ud != null) ? ud.getFirstname() : ""%>
                    </li>
                    <li>Last Name: <%=ud != null ? ud.getLastname() : ""%>
                    </li>
                    <li>Email: <%=ud != null ? ud.getFirstEmail() : ""%>
                    </li>
                </ul>
<%
    }
    }
%>

</body>
<footer>
    <ul>
        <li class="footer"><a href=<%=Default.URL_ROOT%>>Home</a></li>
    </ul>
</footer>
</html>
