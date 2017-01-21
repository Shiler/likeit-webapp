/**
 * Created by Evgeny Yushkevich on 20.01.2017.
 */

$(document).ready(function () {
    showLastQuestions();
    setInterval('showLastQuestions()', 3000);
});

function showLastQuestions() {
    $.ajax({
        url: "/getLastQuestions",
        cache: false,
        type: 'get',
        dataType: 'json',
        async: true,
        success: function (data) {
            switch (data.response) {
                case 'error': {
                    $("#lastQuestionsTableBody tr").remove();
                    var parent = $("#lastQuestionsTableBody");
                    var row = document.createElement('tr');
                    $(row).html(data.error);
                    $(parent).append(row);
                }
                    break;
                case 'success': {
                    $("#lastQuestionsTableBody tr").remove();

                    var parent = $("#lastQuestionsTableBody");
                    for (var i = 0; i < data.questions.length; i++) {
                        var id = data.questions[i].id;
                        var createTime = data.questions[i].createTime;
                        var title = data.questions[i].title;
                        var creator = data.questions[i].creator;

                        if (title.length > 40) {
                            title = title.slice(0, 40) + '...';
                        }

                        var row = document.createElement('tr');

                        var createTimeCol = document.createElement('td');
                        $(createTimeCol).html(createTime);

                        var titleCol = document.createElement('td');
                        var titleLink = document.createElement('a');
                        $(titleLink).attr('href', '/question?id=' + id);
                        $(titleLink).html(title);
                        $(titleCol).append(titleLink);

                        var creatorCol = document.createElement('td');
                        $(creatorCol).html(creator);

                        $(row).append(createTimeCol);
                        $(row).append(titleCol);
                        $(row).append(creatorCol);

                        $(parent).append(row);
                    }
                    break;
                }
            }
        }
    });
}
