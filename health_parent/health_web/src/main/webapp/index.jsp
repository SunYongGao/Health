<%--
  Created by IntelliJ IDEA.
  User: Eric
  Date: 2020/8/30
  Time: 9:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--jsp中获取登陆用户名的方式--%>
${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}
</body>mai
</html>
