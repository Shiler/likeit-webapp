<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 22.11.2016
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>

<script type="text/javascript" src="/resources/javascript/index.js"></script>

<fmt:bundle basename="app-strings" prefix="index.">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-2" style="padding-right: 0;">
                <div class="top-users">
                    <h4><span class="glyphicon glyphicon-heart glyph-category"></span> <fmt:message key="top-users"/>
                    </h4>
                    <c:set var="counter" value="0" scope="page"/>
                    <c:forEach items="${topUsers}" var="user">
                        <div class="top-user row">
                            <img src="resources/img/medal/medal${counter+1}.png" class="img-circle" alt="avatar1"
                                 align="left">
                            <span><a class="link" href="/profile?id=${user.id}"><yo:cutSurname>${user.fullName}</yo:cutSurname></a></span>
                            <br>
                            <span class="top-user-rate"><fmt:message key="rating"/>: ${user.totalRating}</span>
                        </div>
                        <c:set var="counter" value="${counter + 1}" scope="page"/>
                    </c:forEach>
                </div>
            </div>

            <div style="padding-left: 0;" class="col-sm-8">
                <div class="questions-block">
                    <div class="row">
                        <h4><fmt:message key="last-questions"/></h4>
                        <table id="lastQuestionsTable" class="table table-striped">
                            <tbody id="lastQuestionsTableBody">
                            <c:forEach var="item" items="${lastQuestions}">
                                <tr>
                                    <td><yo:timestampFormat timestamp="${item.createTime}" locale="${sessionScope.locale}"/></td>
                                    <td><a href="/question?id=${item.id}"><yo:cut
                                            symbols="40">${item.title}</yo:cut></a>
                                    </td>
                                    <td style="text-align: right;"><fmt:message key="by"/> ${item.creator.fullName}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>


                </div>
            </div>

            <div class="col-sm-2">
                <h4><span class="glyphicon glyphicon-signal glyph-category"></span> <fmt:message key="top-questions"/>
                </h4>
                <div class="top-questions">
                    <ul style="padding-left: 5px;">
                        <c:forEach var="item" items="${mostRatedQuestions}">
                            <li type="square"><a class="link" href="/question?id=${item.id}" style="font-size: 12px;"><yo:cut
                                    symbols="40">${item.title}</yo:cut>
                            </a> <span style="font-size: 10px;"><div class="glyphicon glyphicon-star"></div>
                                    ${item.rating}</span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

        </div>
    </div>
</fmt:bundle>


<%@include file="/WEB-INF/jspf/new_question_block.jspf" %>
<%@include file="/WEB-INF/jspf/about_block.jspf" %>

