$(document).ready(function () {
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
                        input.text("");
                        errorElement.css('display', 'none');
                    }
                        break;
                }
            }
        });

    });

});
