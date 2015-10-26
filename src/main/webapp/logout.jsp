<%--
  User: Ryan
  Date: 17/10/2015
  Time: 15:41
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
</head>
<body>
<%
    session.invalidate();
    response.sendRedirect("/Instagrim/index.jsp");
%>
</body>
</html>
