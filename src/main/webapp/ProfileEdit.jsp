<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.UserDetails" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.lib.Default" %>
<%--
  User: Ryan
  Date: 20/10/2015
  Time: 23:39
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="Styles.css"/>
    <title>Profile Edit</title>

</head>
<body>
    <%
        LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        if(lg != null)
        {
            UserDetails ud = (UserDetails) session.getAttribute("UserDetails");
            if(ud != null)
            {

    %>

    <div class="Details">
        <form method="POST" action="Profile">
            <ul>
                <li>First Name: <input type="text" name="firstname" value=<%=ud.getFirstname()%>></li>
                <li>Last Name: <input type="text" name="lastname" value=<%=ud.getLastname()%>></li>
                <li>Email: <input type="text" name="email" value="<%=ud.getFirstEmail()%>"></li>
            </ul>
            <br/>
            <input type="submit" value="Save">
        </form>
    </div>

    <a href="<%=Default.URL_ROOT%>/UploadProfilePic.jsp">Upload a Profile Picture</a>

    <%
            }
            else{
                response.sendRedirect("index.jsp");
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    %>

</body>
<footer>
    <ul>
        <li class="footer"><a href=<%=Default.URL_ROOT%>>Home</a></li>
    </ul>
</footer>
</html>
