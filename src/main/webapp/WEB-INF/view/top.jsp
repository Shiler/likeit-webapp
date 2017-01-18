<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 15.01.2017
  Time: 17:48
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="app-strings" prefix="top.">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <div class="questions-block">
                    <div class="row">
                        <h4><fmt:message key="title"/></h4>
                        <table class="table table-striped">
                            <tbody>
                            <c:forEach items="${topQuestions}" var="item">
                                <tr>
                                    <td class="col-sm-9">
                                        <a href="/question?id=${item.id}"><h4>${item.title}</h4></a>
                                        <p>${item.content}</p>
                                    </td>
                                    <td class="col-sm-2">
                                        <fmt:message key="by"/> <a href="/user?id=${item.creator.id}">${item.creator.fullName}</a>
                                        <br>
                                        <div class="glyphicon glyphicon-comment"></div>
                                        <fmt:message key="answer-amount"/>: ${item.answerAmount}
                                        <br>
                                        <div class="glyphicon glyphicon-heart"></div>
                                        <c:choose>
                                            <c:when test="${item.rating>=0}">
                                                <span style="color: #27ae60"><fmt:message key="rating"/>: ${item.rating}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #c0392b"><fmt:message key="rating"/>: ${item.rating}</span>
                                            </c:otherwise>
                                        </c:choose>
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
                                        <div class="glyphicon glyphicon-edit"></div>
                                        <yo:timestampFormat timestamp="${item.createTime}" locale="${sessionScope.locale}"/>
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
