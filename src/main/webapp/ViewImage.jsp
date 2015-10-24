<%--
  User: Ryan
  Date: 24/10/2015
  Time: 15:06
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>></title>
</head>
<body>
    <img src="/Instagrim/Image/<%=request.getAttribute("image_url")%>" />

    <form  method="POST" action="Comment">
        <textarea name="CommentBody" placeholder="Enter a Comment..."></textarea>
        <br />
        <input type="submit" value="Save" />
    </form>

</body>
</html>
