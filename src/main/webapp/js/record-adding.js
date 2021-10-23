function createRecord() {
    let email = document.getElementById('recordEmail').value;
    let name = document.getElementById('recordName').value;
    let period = document.getElementById('recordPeriod').value;
    let recordList = document.getElementsByClassName('list-group')[0];
    let record = '<div style="position:relative;" class="list-group-item list-group-item-action d-flex">\n' +
    '          <input type="hidden" name="email" value="'+ email +'">\n' +
    '          <input type="hidden" name="name" value="'+ name +'">\n' +
    '          <input type="hidden" name="period" value="'+ period +'">\n' +
    '          <div style="position: absolute; right: 0" data-bs-toggle="tooltip" data-bs-placement="right"\n' +
    '               title="Record was not created. Press \'Save\' to save changes or \'Discard\' to discard">\n' +
    '            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="#ffc107"\n' +
    '                 class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img"\n' +
    '                 aria-label="Warning:">\n' +
    '              <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>\n' +
    '            </svg>\n' +
    '          </div>\n' +
    '          <div class="flex-column">\n' +
    '            <a href="#">'+ name +'</a>\n' +
    '            <p><small>'+ email +'</small></p>\n' +
    '            <span class="badge rounded-pill bg-info"></span>\n' +
    '          </div>\n' +
    '        </div>';
    recordList.insertAdjacentHTML('afterbegin', record);
}