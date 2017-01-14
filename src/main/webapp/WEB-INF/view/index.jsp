<%--
  Created by IntelliJ IDEA.
  User: Evgeny Yushkevich
  Date: 22.11.2016
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>


<div class="container-fluid">
    <div class="row">
        <div class="col-sm-2">
            <div class="top-users">
                <h4><span class="glyphicon glyphicon-heart glyph-category"></span> Top Users</h4>
                <div class="top-user row">
                    <img src="resources/img/avatar1.jpg" class="img-circle" alt="avatar1" align="left">
                    <span>Alina Stashuk</span>
                    <br>
                    <span class="top-user-rate">Raiting: 984</span>
                </div>
                <br>
                <div class="top-user row">
                    <img src="resources/img/avatar2.jpg" class="img-circle" alt="avatar2" align="left">
                    <span>Mike Krasniy</span>
                    <br>
                    <span class="top-user-rate">Raiting: 431</span>
                </div>
                <br>
                <div class="top-user row">
                    <img src="resources/img/avatar3.jpg" class="img-circle" alt="avatar3" align="left">
                    <span>Nikita Glytov</span>
                    <br>
                    <span class="top-user-rate">Raiting: 289</span>
                </div>
                <br>
                <div class="top-user row">
                    <img src="resources/img/avatar4.jpg" class="img-circle" alt="avatar4" align="left">
                    <span>Andrey Kolba</span>
                    <br>
                    <span class="top-user-rate">Raiting: 144</span>
                </div>
                <br>
                <div class="top-user row">
                    <img src="resources/img/avatar5.jpg" class="img-circle" alt="avatar5" align="left">
                    <span>Artemy Geist</span>
                    <br>
                    <span class="top-user-rate">Raiting: 45</span>
                </div>
            </div>
        </div>

        <div class="col-sm-8">
            <div class="questions-block">
                <div class="row">
                    <h4>Last questions</h4>
                    <table class="table table-striped">
                        <tbody>
                        <c:forEach var="item" items="${lastQuestions}">
                            <tr>
                                <td><yo:timestampFormat timestamp="${item.createTime}"/></td>
                                <td><yo:cut symbols="40">${item.title}</yo:cut></td>
                                <td>by ${item.creator.fullName}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>


            </div>
        </div>

        <div class="col-sm-2">
            <h4><span class="glyphicon glyphicon-signal glyph-category"></span> Top Questions</h4>
            <div class="top-questions">
                <ul>
                    <c:forEach var="item" items="${mostRatedQuestions}">
                        <li type="square"><a href="#"><yo:cut symbols="20">${item.title}</yo:cut></a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>

    </div>
</div>

<%@include file="/WEB-INF/jspf/new_question_block.jspf"%>
<%@include file="/WEB-INF/jspf/about_block.jspf"%>

