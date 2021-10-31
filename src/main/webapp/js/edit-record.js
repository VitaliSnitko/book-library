let a = document.querySelectorAll('a[data-bs-target="#editModal"]');

let recordIdInput = document.getElementById('editRecordId');
let emailEditInput = document.getElementById('editRecordEmail');
let nameEditInput = document.getElementById('editRecordName');
let borrowDateInput = document.getElementById('editRecordBorrowDate');
let periodInput = document.getElementById('editRecordPeriod');

for (let i = 0; i < a.length; i++) {
    a[i].addEventListener('click', (e) => setInputs(e));
}

function setInputs(e) {
    recordIdInput.value = e.target.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.value;
    emailEditInput.value = e.target.nextElementSibling.firstElementChild.textContent;
    nameEditInput.value = e.target.textContent;
    borrowDateInput.value = e.target.previousElementSibling.previousElementSibling.previousElementSibling.value;
    periodInput.value = e.target.previousElementSibling.value;
}

let statusModalInput = document.getElementById('editRecordStatus');

let editRecordButton = document.getElementsByClassName("save-record")[1];

editRecordButton.onclick = function () {
    let record = document.querySelector('input[name="recordId"][value="' + recordIdInput.value + '"]');
    let recordStatus = record.previousElementSibling;

    let status = document.getElementById('editStatus');
    let arrStatus = status.value.split(' ');
    if (arrStatus[0] === 'Unavailable') {
        arrStatus = ['Available', 0, 'out', 'of', document.getElementById('editTotalAmount').value];
    }

    if (statusModalInput.value === 'returned' && recordStatus.value === 'borrowed') {
        arrStatus[1] = Number(arrStatus[1]) + 1;
    } else if ((statusModalInput.value === 'lost' || statusModalInput.value === 'returned and damaged') && recordStatus.value === 'borrowed') {
        arrStatus[4] = Number(arrStatus[4]) - 1;
    } else if (statusModalInput.value === 'returned' && (recordStatus.value === 'lost' || recordStatus.value === 'returned and damaged')) {
        arrStatus[1] = Number(arrStatus[1]) + 1;
        arrStatus[4] = Number(arrStatus[4]) + 1;
    } else if ((statusModalInput.value === 'lost' || statusModalInput.value === 'returned and damaged') && recordStatus.value === 'returned') {
        arrStatus[1] = Number(arrStatus[1]) - 1;
        arrStatus[4] = Number(arrStatus[4]) - 1;
    }

    if (recordStatus.value === 'borrowed') {
        let icon = '<div style="position: absolute; right: 8px" data-bs-toggle="tooltip" data-bs-placement="right" title="Record was not updated. Press \'Save\' to save changes or \'Discard\' to discard"><svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="#2e7bf3" class="bi bi-info-circle" viewBox="0 0 16 16">\n' +
            '  <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>\n' +
            '</svg></div>'
        record.parentElement.insertAdjacentHTML('afterbegin', icon);
    }

    recordStatus.value = statusModalInput.value;
    status.value = arrStatus.join(' ');

    if (Number(arrStatus[1]) === 0) {
        document.getElementById('addRecordButton').setAttribute("disabled", "disabled");
    } else {
        document.getElementById('addRecordButton').removeAttribute("disabled");
    }
}

statusModalInput.onchange = function () {
    let status = document.querySelector('input[name="recordId"][value="' + recordIdInput.value + '"]').previousElementSibling;
    if (Number(document.getElementById('editStatus').value.split(' ')[1]) === 0 &&
        status.value === 'returned' &&
        (statusModalInput.value === 'lost' || statusModalInput.value === 'returned and damaged')) {
        document.getElementById('editRecordModalButton').setAttribute('disabled', 'disabled');
    } else {
        document.getElementById('editRecordModalButton').removeAttribute('disabled');
    }
}




