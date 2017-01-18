<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 17.01.2017
  Time: 8:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>

<fmt:bundle basename="app-strings">
    <!DOCTYPE html>
    <html lang="${sessionScope.locale}">
    <head>
        <title><fmt:message key="error.page-title"/></title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="icon" type="image/x-icon" href="favicon.ico">
        <link rel="stylesheet" type="text/css" href="resources/css/siimple/dist/siimple.min.css">

    </head>
    <body style="height: 600px; display: table; margin-left: auto; margin-right: auto;">

    <div style="display: table-cell; vertical-align: middle; text-align: center; ">
        <h1><fmt:message key="error.overloaded-error-title"/></h1>
        <h3><fmt:message key="error.overloaded-error-text"/></h3>
    </div>

    </body>
    </html>
</fmt:bundle>