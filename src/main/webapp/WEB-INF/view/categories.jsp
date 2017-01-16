<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 15.01.2017
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="app-strings" prefix="categories.">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-2">
                <div class="categories">
                    <h4><fmt:message key="categories"/>:</h4>
                    <ul>
                        <c:choose>
                            <c:when test="${sessionScope.locale=='ru'}">
                                <li type="square"><a href="/categories">Все</a></li>
                            </c:when>
                            <c:otherwise>
                                <li type="square"><a href="/categories">All</a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach items="${categories}" var="item">
                            <c:choose>
                                <c:when test="${sessionScope.locale=='ru'}">
                                    <li type="square"><a href="/categories?id=${item.id}">${item.nameRu}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li type="square"><a href="/categories?id=${item.id}">${item.name}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="col-sm-10">
                <div class="questions-block">
                    <div class="row">
                        <c:choose>
                            <c:when test="${sessionScope.locale=='ru'}">
                                <h4>Вопросы в <i>${category.nameRu}</i></h4>
                            </c:when>
                            <c:otherwise>
                                <h4>Questions in <i>${category.name}</i></h4>
                            </c:otherwise>
                        </c:choose>

                        <table class="table table-striped">
                            <tbody>
                            <c:forEach items="${questions}" var="item">
                                <tr>
                                    <td>
                                        <a href="/question?id=${item.id}"><h4>${item.title}</h4></a>
                                        <p>${item.content}</p>
                                    </td>
                                    <td>
                                        <fmt:message key="by"/> <a href="/user?id=${item.creator.id}">${item.creator.fullName}</a>
                                        <br>
                                        <div class="glyphicon glyphicon-edit"></div>
                                        <yo:timestampFormat timestamp="${item.createTime}"/>
                                        <br>
                                        <div class="glyphicon glyphicon-comment"></div>
                                            ${item.answerAmount}
                                        <br>
                                        <div class="glyphicon glyphicon-list"></div>
                                        <c:choose>
                                            <c:when test="${sessionScope.locale=='ru'}">
                                                <a href="/categories?id=${item.questionType.id}">${item.questionType.nameRu}</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/categories?id=${item.questionType.id}">${item.questionType.name}</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <br>
                                        <div class="glyphicon glyphicon-heart"></div>
                                        <c:choose>
                                            <c:when test="${item.rating>=0}">
                                                <span style="color: #27ae60">${item.rating}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #c0392b">${item.rating}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>

        </div>
    </div>

</fmt:bundle>


<%@include file="/WEB-INF/jspf/new_question_block.jspf" %>
<%@include file="/WEB-INF/jspf/about_block.jspf" %>
