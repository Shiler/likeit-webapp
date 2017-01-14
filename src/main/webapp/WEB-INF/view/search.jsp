<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 14.01.2017
  Time: 5:32
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="questions-block">
                <div class="row">
                    <h4>Found questions: ${fn:length(searchResult)}</h4>
                    <table class="table table-striped">
                        <tbody>
                            <c:forEach items="${searchResult}" var="item">
                                <tr>
                                    <td>
                                        <h4>${item.title}</h4>
                                        <p>${item.content}</p>
                                    </td>
                                    <td>
                                        by ${item.creator.fullName}
                                        <br>
                                        <div class="glyphicon glyphicon-edit"></div>
                                        <yo:timestampFormat timestamp="${item.createTime}"/>
                                        <br>
                                        <div class="glyphicon glyphicon-comment"></div>
                                        ${item.answerAmount}
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

<%@include file="/WEB-INF/jspf/new_question_block.jspf"%>
<%@include file="/WEB-INF/jspf/about_block.jspf"%>
