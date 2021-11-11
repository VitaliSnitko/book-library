(function () {
    'use strict'
    let recordForm = document.getElementsByClassName("record-form")[0];
    recordForm.addEventListener('submit', function (event) {
        if (!recordForm.checkValidity()) {
            recordForm.classList.add('was-validated')
            event.preventDefault()
            event.stopImmediatePropagation()
        } else {
            createRecord();
            $('#addModal').modal('hide');
            recordForm.reset();
            recordForm.classList.remove("was-validated")
        }
    }, false)
})()