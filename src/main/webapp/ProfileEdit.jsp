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
    <div class="Details">
        <form method="POST" action="ProfileEdit">
            <ul>
                <li>First Name: <input type="text" name="firstname"></li>
                <li>Last Name: <input type="text" name="lastname"></li>
                <li>Email: <input type="text" name="email"></li>
            </ul>
            <br/>
            <input type="submit" value="Save">
        </form>
    </div>

</body>
</html>
