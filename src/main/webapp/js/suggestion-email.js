let searchWrapper = document.getElementsByClassName('search-input')[0];
let emailAddInput = document.getElementById('recordEmail');
let nameAddInput = document.getElementById('recordName');
let suggBox = document.getElementsByClassName("autocom-box")[0];
let timeout = null;

function sendAjax(actualEmailInput) {
    $.ajax({
        url: "autocomplete",
        data: {
            input: actualEmailInput
        },
        success: function (readers) {
            let emails = readers.map((reader) => {
                return `<li>${reader.email}</li>`;
            });
            searchWrapper.classList.add("active");
            suggBox.innerHTML = emails.join('');
            let allList = suggBox.querySelectorAll("li");
            for (let i = 0; i < allList.length; i++) {
                allList[i].setAttribute("onclick", "select(" + readers[i] + ")");
            }
        }
    })
}

emailAddInput.onkeyup = (e) => {
    let actualEmailInput = e.target.value;
    clearTimeout(timeout);
    if (actualEmailInput.length > 0) {
        timeout = setTimeout(function () {
            console.log(actualEmailInput);
            sendAjax(actualEmailInput);
        }, 1000);
    } else {
        searchWrapper.classList.remove("active"); //hide autocomplete box
    }
    disableAddButtonIfReaderAlreadyHasThisBook();
}

nameAddInput.onkeyup = () => {
    disableAddButtonIfReaderAlreadyHasThisBook();
}

function select(reader) {
    nameAddInput.value = reader.name;
    searchWrapper.classList.remove("active");
    disableAddButtonIfReaderAlreadyHasThisBook();
}

function disableAddButtonIfReaderAlreadyHasThisBook() {
    let addRecordButton = document.getElementsByClassName('save-record')[0];
    if (document.getElementsByClassName(document.querySelector("#recordEmail").value).length !== 0
        && document.getElementsByClassName(document.querySelector("#recordName").value).length !== 0) {
        addRecordButton.setAttribute("disabled", "disabled");
    } else {
        addRecordButton.removeAttribute("disabled");
    }
}
