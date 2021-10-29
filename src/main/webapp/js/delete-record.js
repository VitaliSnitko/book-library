function deleteRecord(target) {
    target.parentElement.remove();

    let status = document.getElementById('editStatus');
    let arrStatus = status.value.split(' ');
    arrStatus[1] = Number(arrStatus[1]) + 1;
    status.value = arrStatus.join(' ');

    document.getElementById('addRecordButton').removeAttribute('disabled');
    document.getElementById('editRecordModalButton').removeAttribute('disabled');
}
