<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 16.01.2017
  Time: 0:27
  To change this template use File | Settings | File Templates.
--%>

<fmt:bundle basename="app-strings" prefix="question.">
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
                <div class="questions-block">
                    <div class="row">
                        <div class="question-page">
                            <h4><strong>${question.title}</strong></h4>
                            <p class="small-info"><fmt:message key="rating"/>: <a href="">-</a> <span
                                    style="font-weight: bold;">${question.rating}</span> <a href="">+</a></p>
                            <p class="small-info"><fmt:message key="by"/> <a href="/user?id=1">${question.creator.fullName}</a></p>

                            <hr>
                            <p>${question.content}</p>
                            <p class="small-info" style="margin-bottom: 30px;"><i><fmt:message key="created"/>: <yo:timestampFormat timestamp="${question.createTime}" locale="${sessionScope.locale}"/></i></p>
                            <h4 style="margin-bottom: 20px;"><fmt:message key="answers"/>: ${fn:length(answers)}</h4>
                            <table class="table table-striped">
                                <tbody>
                                <c:forEach items="${answers}" var="item">
                                    <tr>
                                        <td class="col-sm-2">
                                            <a href="/profile?id=${item.creator.id}"><p>${item.creator.fullName}</p></a>
                                        </td>
                                        <td class="col-sm-9">
                                            <p>${item.text}</p>
                                            <hr>
                                            <p><fmt:message key="rating"/>: ${item.rating}</p>
                                            <p>at <yo:timestampFormat timestamp="${item.createTime}" locale="${sessionScope.locale}"/></p>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <form method="POST" action="question.add">
                            <textarea class="form-textarea" name="text" rows="4" placeholder="<fmt:message key="leave-answer"/>"
                                      style="margin-bottom: 5px;"></textarea>
                                <input type="submit" class="form-input" value="<fmt:message key="submit"/>">
                            </form>
                        </div>

                    </div>

                </div>
            </div>

        </div>
    </div>
</fmt:bundle>
