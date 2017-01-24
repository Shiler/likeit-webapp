<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 23.01.2017
  Time: 7:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:bundle basename="app-strings" prefix="profile.">

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-4">
                <div class="profile-icon">
                    <span class="glyphicon glyphicon-user"></span>
                </div>

            </div>
            <div class="col-sm-8">
                <div class="questions-block">
                    <div class="row">
                        <div class="question-page">
                            <h4><strong>${profile.fullName}</strong></h4>
                            <p class="small-info"><fmt:message key="age"/>: <span
                                    style="font-weight: bold;">${profile.age}</span></p>
                            <hr>
                            <p><fmt:message key="rating"/>: <b>${profile.totalRating}</b></p>
                            <p><fmt:message key="questions"/>: <b>${questionCount}</b></p>
                            <p><fmt:message key="answers"/>: <b>${answerCount}</b></p>
                            <p class="small-info" style="margin-bottom: 30px;"><i><fmt:message key="joined"/>:
                                <yo:timestampFormat timestamp="${profile.createTime}"
                                                    locale="${sessionScope.locale}"/></i></p>


                        </div>

                    </div>

                </div>
            </div>

        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="questions-block">
                    <h4 style="text-align: center; margin-bottom: 20px;"><fmt:message key="show-questions"/></h4>
                    <table class="table table-striped">
                        <tbody>
                        <c:forEach items="${userQuestions}" var="item">
                            <tr>
                                <td class="col-sm-9">
                                    <a href="/question?id=${item.id}"><h4>${item.title}</h4></a>
                                    <p>${item.content}</p>
                                </td>
                                <td class="col-sm-2">
                                    <div class="glyphicon glyphicon-comment"></div>
                                    <fmt:message key="question.answers"/>: ${item.answerAmount}
                                    <br>
                                    <div class="glyphicon glyphicon-heart"></div>
                                    <c:choose>
                                        <c:when test="${item.rating>=0}">
                                            <span style="color: #27ae60"><fmt:message
                                                    key="rating"/>: ${item.rating}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #c0392b"><fmt:message
                                                    key="rating"/>: ${item.rating}</span>
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

</fmt:bundle>