<%@ page import="uk.ac.dundee.computing.aec.instagrim.lib.CommentWrapper" %>
<%@ page import="java.util.Iterator" %>
<%--
  User: Ryan
  Date: 24/10/2015
  Time: 15:06
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <img src="/Instagrim/Image/<%=request.getAttribute("image_url")%>" />

    <form  method="POST" action="<%=request.getAttribute("image_url")%>">
        <textarea name="CommentBody" placeholder="Enter a Comment..."></textarea>
        <br />
        <input type="submit" value="Save" />
    </form>

    <p>Comments: </p>
    <ul>
        <%
            Iterator<CommentWrapper> itr = (Iterator<CommentWrapper>) request.getAttribute("commentsi");
            if (itr != null) {
                while (itr.hasNext()) {
                    CommentWrapper c = itr.next();
        %>
            <li>
                <b>
                    <%=c.getUser()%>
                </b>
                <p><%=c.getComment()%></p>
            </li>
        <%
                }
            }
        %>
        </ul>
    </body>
</html>
