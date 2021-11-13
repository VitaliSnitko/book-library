document.getElementById('editBookSendAjax').onclick = function () {
    sendAjaxPost();
    return false;
}

function showToast() {
    new bootstrap.Toast(document.querySelector('.toast')).show();
}

function sendAjaxPost() {
    let data = $('#editBookForm').serialize();
    $.ajax({
        type: "post",
        url: "edit",
        data: data,
        success: function (resp) {
            showToast();
        }
    });
}