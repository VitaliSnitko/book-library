function createRecord() {
    let email = document.getElementById('recordEmail').value;
    let name = document.getElementById('recordName').value;
    let period = document.getElementById('recordPeriod').value;
    let recordList = document.getElementsByClassName('list-group')[0];
    let record = '<div style="position:relative;" class="list-group-item list-group-item-action d-flex">\n' +
        '  <input type="hidden" name="email" value="' + email + '">\n' +
        '  <input type="hidden" name="name" value="' + name + '">\n' +
        '  <input type="hidden" name="period" value="' + period + '">\n' +
        '  <div style="position: absolute; right: 0" data-bs-toggle="tooltip" data-bs-placement="right"\n' +
        '       title="Record was not created. Press \'Save\' to save changes or \'Discard\' to discard">\n' +
        '    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="#ffc107"\n' +
        '         class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img"\n' +
        '         aria-label="Warning:">\n' +
        '      <path\n' +
        '          d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>\n' +
        '    </svg>\n' +
        '  </div>\n' +
        '  <div class="trash-icon" style="position: absolute; right: 12; bottom: 4" onclick="deleteRecord(this)">\n' +
        '    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash"\n' +
        '         viewBox="0 0 16 16">\n' +
        '      <path\n' +
        '          d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>\n' +
        '      <path fill-rule="evenodd"\n' +
        '            d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>\n' +
        '    </svg>\n' +
        '  </div>\n' +
        '  <div class="flex-column">\n' +
        '    <a href="#">' + name + '</a>\n' +
        '    <p><small>' + email + '</small></p>\n' +
        '    <span class="badge rounded-pill bg-info"></span>\n' +
        '  </div>\n' +
        '</div>';
    recordList.insertAdjacentHTML('afterbegin', record);

    let status = document.getElementById('editStatus');
    let arrStatus = status.value.split(' ');
    arrStatus[1] = Number(arrStatus[1]) - 1;
    status.value = arrStatus.join(' ');

    if (Number(arrStatus[1]) === 0) {
        document.getElementById('addRecordButton').setAttribute("disabled", "disabled");
        if (document.getElementById('editRecordStatus').value === 'lost' ||
            document.getElementById('editRecordStatus').value === 'returned and damaged') {
            document.getElementById('editRecordModalButton').setAttribute('disabled', 'disabled');
        }
    }
}