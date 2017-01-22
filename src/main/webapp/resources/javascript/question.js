$(document).ready(function () {

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
                        $(answerRow).append(answerCol);
                        $(table).append(answerRow);

                    }
                        break;
                }
            }
        });

    });

});
