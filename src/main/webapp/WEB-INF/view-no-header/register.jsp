<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 16.01.2017
  Time: 4:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>

<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
    <fmt:bundle basename="app-strings">
        <title><fmt:message key="register.page-title"/></title>
    </fmt:bundle>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <link rel="stylesheet" type="text/css" href="resources/css/siimple/dist/siimple.min.css">

</head>
<body>

<fmt:bundle basename="app-strings" prefix="register.">

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <div class="go-back">
                    <a href="/index">
                        <div class="glyphicon glyphicon-chevron-left"></div>
                        <fmt:message key="back"/>
                    </a>
                    <fmt:message key="or"/>
                    <a href="/login">
                        <fmt:message key="to-login"/>
                        <div class="glyphicon glyphicon-chevron-right"></div>
                    </a>
                </div>
            </div>
        </div>
        <br>
        <br>
        <br>
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="registration">
                    <h3><fmt:message key="registration"/></h3>
                    <form class="form-auto" action="register.do" method="POST">
                        <p class="left-margin">E-mail</p>
                        <input type="email" class="form-input" name="email" required placeholder="example@example.com">
                        <p class="left-margin"><fmt:message key="user-name"/></p>
                        <input type="text" pattern="[a-zA-Z0-9]{6,30}" name="username" required class="form-input"
                               placeholder="user123">
                        <p class="left-margin"><fmt:message key="full-name"/></p>
                            <input type="text" pattern="[a-zA-Zа-яА-Я0-9\s]{6,30}" name="fullName" required class="form-input"
                               placeholder="Ivan Ivanov">
                        <p class="left-margin"><fmt:message key="age"/></p>
                        <input type="text" pattern="[0-9]{1,3}" name="age" required class="form-input" placeholder="20">
                        <p class="left-margin"><fmt:message key="password"/></p>
                        <input type="password" pattern="[a-zA-Z0-9]{6,30}" name="password" required class="form-input"
                               placeholder="your password">
                        <c:choose>
                            <c:when test="${param.error=='required_fault'}">
                                <p style="color: red; margin-left: 7px;"><fmt:message key="empty-fields-error"/></p>
                            </c:when>
                            <c:when test="${param.error=='invalid_parameters'}">
                                <p style="color: red; margin-left: 7px;"><fmt:message key="invalid-parameters-error"/></p>
                            </c:when>
                            <c:when test="${param.error=='user_already_exists'}">
                                <p style="color: red; margin-left: 7px;"><fmt:message key="user-already-exists-error"/></p>
                            </c:when>
                        </c:choose>
                        <input type="submit" class="form-input" name="submit" value="<fmt:message key="register-button"/>">
                    </form>
                </div>
            </div>
        </div>
    </div>
</fmt:bundle>


</body>
</html>