<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 16.01.2017
  Time: 0:27
  To change this template use File | Settings | File Templates.
--%>

<script type="text/javascript" src="/resources/javascript/question.js"></script>
<link rel="stylesheet" href="/resources/css/star.css">

<c:if test="${sessionScope.USER ne null}">
    <c:set var="user" value="${sessionScope.USER}"/>
</c:if>

<fmt:bundle basename="app-strings" prefix="question.">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <div class="questions-block">
                    <div class="row">
                        <div class="question-page">
                            <h4><strong>${question.title}</strong></h4>
                            <p class="small-info"><fmt:message key="rating"/>: <span id="ratingField"
                                                                                     style="font-weight: bold;">${question.rating} </span><a
                                    class="<c:if test="${liked eq true}">liked </c:if> like-icon"
                                    id="likeLink" href="/like?id=${question.id}"><span
                                    class="glyphicon glyphicon-heart"></span></a></p>
                            <p class="small-info"><fmt:message key="by"/> <a
                                    href="/profile?id=${question.creator.id}">${question.creator.fullName}</a></p>

                            <hr>
                            <p>${question.content}</p>
                            <p class="small-info" style="margin-bottom: 30px;"><i><fmt:message key="created"/>:
                                <yo:timestampFormat timestamp="${question.createTime}"
                                                    locale="${sessionScope.locale}"/></i></p>
                            <h4 style="margin-bottom: 20px;"><fmt:message key="answers"/>: <span
                                    id="answerCount">${fn:length(answers)}</span></h4>
                            <table class="table table-striped" id="answersTable">
                                <tbody>
                                <c:forEach items="${answers}" var="item">
                                    <tr>
                                        <td class="col-sm-2">
                                            <a href="/profile?id=${item.creator.id}"><p>${item.creator.fullName}</p></a>
                                        </td>
                                        <td class="col-sm-9">
                                            <p>${item.text}</p>
                                            <hr>
                                            <c:set var="rate" value="${fn:substringBefore(item.rating, '.')}"/>

                                            <form class="ratingsForm<c:if test="${(user ne null) && (user.id eq item.creator.id)}">-disabled</c:if>"
                                                  id="ratingsForm-${item.id}">
                                                <div class="stars">
                                                    <c:choose>
                                                        <c:when test="${(user ne null) && (user.id eq item.creator.id)}">
                                                            <c:forEach var="i" begin="1" end="5">
                                                                <input type="radio"
                                                                       <c:if test="${rate eq i}">checked</c:if> disabled
                                                                       name="star" class="star-${i}-foreign"
                                                                       id="star-${i}-${item.id}">
                                                                <label class="foreign-star star-${i}-foreign"
                                                                       for="star-${i}-${item.id}">${i}</label>
                                                            </c:forEach>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="i" begin="1" end="5">
                                                                <input type="radio"
                                                                       <c:if test="${rate eq i}">checked</c:if>
                                                                       name="star-${item.id}"
                                                                       class="star-${i}"
                                                                       id="star-${i}-${item.id}">
                                                                <label class="star-${i}"
                                                                       for="star-${i}-${item.id}">${i}</label>
                                                            </c:forEach>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <span></span>
                                                </div>
                                                <span class="answerRating"><span
                                                        id="rating-${item.id}">${item.rating}</span> (<span
                                                        id="voteCount-${item.id}">${item.voteCount}</span> <fmt:message
                                                        key="answer-votes"/>)</span>
                                            </form>

                                            <p><yo:timestampFormat timestamp="${item.createTime}"
                                                                   locale="${sessionScope.locale}"/></p>

                                            <c:if test="${(user ne null) && (user.id eq item.creator.id)}">
                                                <p><a href=""><span class="glyphicon glyphicon-pencil"></span></a>
                                                    <a href=""><span class="glyphicon glyphicon-remove"></span></a></p>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <form method="POST" action="answer.add" id="answerForm">
                            <textarea class="form-textarea" name="text" id="answer-input" rows="4"
                                      placeholder="<fmt:message key="leave-answer"/>"
                                      style="margin-bottom: 5px;"></textarea>
                                <input style="display: none;" type="text" name="question_id" value="${question.id}">
                                <input type="submit" id="submitQuestion" class="form-input"
                                       value="<fmt:message key="submit"/>">
                            </form>
                            <div id="errorAlert" style="display: none;" class="alert alert-error">This is an error
                                alert.
                            </div>
                        </div>

                    </div>

                </div>
            </div>

        </div>
    </div>
</fmt:bundle>
