<%--
  User: Ryan
  Date: 24/10/2015
  Time: 12:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<article>
    <h3>File Upload</h3>

    <form method="POST" enctype="multipart/form-data" action="ProfileImage">
        File to upload: <input type="file" name="upfile"><br/>

        <br/>
        <input type="submit" value="Press"> to upload the file!
    </form>

</article>

</body>
</html>
