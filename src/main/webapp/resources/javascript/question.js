$(document).ready(function () {

    $('.stars input').click(function () {
        var idAttr = $(this).attr('id').split('-');
        var answerId = idAttr.pop();
        var rate = idAttr.pop();
        $.ajax({

            url: '/answer.rate?answerId='+answerId+'&rate='+rate,
            type: "GET",
            dataType: 'json',
            success: function (data) {
                switch (data.result) {
                    case 'success': {
                        $(this).removeAttr("checked");
                        var rating = data.newRating;
                        var voteCount = data.newVoteCount;
                        var checkedStar = Math.floor(rating, 1);
                        $('#star-'+checkedStar+'-'+answerId).attr('checked');
                        $('#rating-'+answerId+'').text(rating);
                        $('#voteCount-'+answerId+'').text(voteCount);
                    }
                        break;

                    case 'false': {

                    }
                        break;
                }
            }

        });
    });

    $('#deleteAnswerLink').click(function (e) {
        e.preventDefault();
        $.ajax({
            url: $('#deleteAnswerLink').attr('href'),
            type: "GET",
            dataType: 'json',
            success: function (data) {
                switch (data.result) {
                    case 'success': {
                        var answerIdSelector = '#answer-' + $('#deleteAnswerLink').attr('href').replace('/answer.delete?id=', '');
                        $(answerIdSelector).remove();
                        $('#answerCount').text(data.newAnswerCount);
                    }
                        break;

                    case 'false': {

                    }
                        break;
                }
            }

        });
    });

    $('#editAnswerLink').click(function (e) {
        e.preventDefault();
        answerId = $('#editAnswerLink').attr('href').replace('/answer.edit?id=', '');
        var answerText = document.getElementById("answerText").innerText;
        console.log(answerText);
        var tempAnswerParagraph = $('#answerTextBlock').find('p');
        $('#answerTextBlock').find('p').remove();
        var answerEditTextarea = $('<textarea class="form-textarea" rows="5" name="text"></textarea>');
        $(answerEditTextarea).val(answerText);
        var answerEditConfirm = $('<input class="btn btn-small" type="button" value="OK"/>');
        var answerEditDeny = $('<input class="btn btn-small" type="button" value="cancel"/>');

        $(answerEditDeny).click(function () {
            $(answerEditTextarea).remove();
            $(answerEditConfirm).remove();
            $(answerEditDeny).remove();
            $('#answerTextBlock').find('br').remove();
            $('#answerTextBlock').append(tempAnswerParagraph);
        });

        $(answerEditConfirm).click(function () {
            $.ajax({
                url: $('#editAnswerLink').attr('href') + '&text='+answerEditTextarea.val(),
                type: "GET",
                dataType: 'json',
                success: function (data) {
                    if (data.result == 'success') {
                        $(tempAnswerParagraph).text(answerEditTextarea.val());
                    }
                    $(answerEditTextarea).remove();
                    $(answerEditConfirm).remove();
                    $(answerEditDeny).remove();
                    $('#answerTextBlock').find('br').remove();
                    $('#answerTextBlock').append(tempAnswerParagraph);
                }
            });
        });

        $('#answerTextBlock').append(answerEditTextarea);
        $('#answerTextBlock').append($('<br>'));
        $('#answerTextBlock').append(answerEditConfirm);
        $('#answerTextBlock').append(answerEditDeny);
    });

    $('#likeLink').click(function (e) {
        e.preventDefault();
        $.ajax({
            url: $('#likeLink').attr('href'),
            type: "GET",
            dataType: 'json',
            success: function (data) {
                switch (data.result) {
                    case 'liked': {
                        $('#likeLink').addClass('liked');
                        $('#ratingField').html(data.newRating + ' ');
                    }
                        break;

                    case 'disliked': {
                        $('#likeLink').removeClass('liked');
                        $('#ratingField').html(data.newRating + ' ');
                    }
                        break;
                }
            }

        });

    });

    $('#answerForm').submit(function (e) {
        e.preventDefault();
        $.ajax({

            url: '/answer.add',
            type: 'post',
            dataType: 'json',
            async: true,
            data: $('form#answerForm').serialize(),

            success: function (data) {
                switch (data.response) {
                    case 'error': {
                        var errorElement = $('#errorAlert');
                        errorElement.css('display', 'block');
                        errorElement.text(data.error);
                    }
                        break;
                    case 'success': {
                        var errorElement = $('#errorAlert');
                        var input = $('#answer-input');
                        $(input).val("");
                        errorElement.css('display', 'none');

                        var profileId = data.userId;
                        var fullName = data.fullName;
                        var text = data.text;
                        var rating = data.rating;
                        var createTime = data.createTime;

                        var table = $('#answersTable').find('tbody');
                        var answerRow = document.createElement('tr');
                        var profileCol = document.createElement('td');
                        $(profileCol).attr('class', 'col-sm-2');
                        var profileLink = document.createElement('a');
                        $(profileLink).attr('href', '/profile?id=' + profileId);
                        $(profileLink).text(fullName);
                        $(profileCol).append(profileLink);
                        $(answerRow).append(profileCol);
                        $(table).append(answerRow);

                        var answerCol = document.createElement('td');
                        $(answerCol).attr('class', 'col-sm-9');
                        var answerText = document.createElement('p');
                        $(answerText).html(text);
                        $(answerCol).append(answerText);
                        $(answerCol).append(document.createElement('hr'));
                        var answerRating = document.createElement('p');
                        $(answerRating).html(rating);
                        $(answerCol).append(answerRating);
                        var answerCreateTime = document.createElement('p');
                        $(answerCreateTime).html(createTime);
                        $(answerCol).append(answerCreateTime);

                        var editDelete = document.createElement('p');
                        var editLink = document.createElement('a');
                        $(editLink).attr('id', 'editAnswerLink');
                        $(editLink).attr('href', '/answer.edit?id='+data.answerId);
                        var editSpan = document.createElement('span');
                        $(editSpan).attr('class', 'glyphicon glyphicon-pencil');
                        $(editLink).append(editSpan);
                        $(editDelete.append(editLink));
                        $(editDelete).append(' ');
                        var deleteLink = document.createElement('a');
                        $(deleteLink).attr('id', 'deleteAnswerLink');
                        $(deleteLink).attr('href', '/answer.delete?id='+data.answerId);
                        var deleteSpan = document.createElement('span');
                        $(deleteSpan).attr('class', 'glyphicon glyphicon-remove');
                        $(deleteLink).append(deleteSpan);
                        $(editDelete.append(deleteLink));

                        $(answerCol).append(editDelete);
                        $(answerRow).append(answerCol);
                        $(table).append(answerRow);

                        $('#answerCount').text(data.newAnswerCount);
                    }
                        break;
                }
            }
        });

    });

});

