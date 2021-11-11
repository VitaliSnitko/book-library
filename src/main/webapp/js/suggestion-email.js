let searchWrapper = document.getElementsByClassName('search-input')[0];
let emailAddInput = document.getElementById('recordEmail');
let nameAddInput = document.getElementById('recordName');
let suggBox = document.getElementsByClassName("autocom-box")[0];
let timeout = null;

emailAddInput.onkeyup = (e) => {
    let actualEmailInput = e.target.value;
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        console.log('Input Value:', emailAddInput.value);
    }, 1000);

    // let suggestionContainer = [];
    // if (actualEmailInput) {
    //     suggestionContainer = emailSuggestions.filter((emailSuggestion) => {
    //         return emailSuggestion.toLocaleLowerCase().startsWith(actualEmailInput.toLocaleLowerCase());
    //     }).map((emailSuggestion) => {
    //         return `<li>${emailSuggestion}</li>`;
    //     });
    //     searchWrapper.classList.add("active"); //show autocomplete box
    //     if (suggestionContainer.length !== 0) {
    //         showSuggestions(suggestionContainer);
    //     }
    //     let allList = suggBox.querySelectorAll("li");
    //     for (let i = 0; i < allList.length; i++) {
    //         allList[i].setAttribute("onclick", "select(this)");
    //     }
    // } else {
    //     searchWrapper.classList.remove("active"); //hide autocomplete box
    // }

    disableAddButtonIfReaderAlreadyHasThisBook();
}

nameAddInput.onkeyup = (e) => {
    disableAddButtonIfReaderAlreadyHasThisBook();
}

function select(element) {
    emailAddInput.value = element.textContent;
    nameAddInput.value = nameSuggestions[emailSuggestions.indexOf(element.textContent)];
    searchWrapper.classList.remove("active");
    disableAddButtonIfReaderAlreadyHasThisBook();
}

function disableAddButtonIfReaderAlreadyHasThisBook(actualInput) {
    let addRecordButton = document.getElementsByClassName('save-record')[0];
    if (document.getElementsByClassName(document.querySelector("#recordEmail").value).length !== 0
        && document.getElementsByClassName(document.querySelector("#recordName").value).length !== 0) {
        addRecordButton.setAttribute("disabled", "disabled");
    } else {
        addRecordButton.removeAttribute("disabled");
    }
}

function showSuggestions(list) {
    let listData;
    if (!list.length) {
        listData = `<li>${emailAddInput.value}</li>`;
    } else {
        listData = list.join('');
    }
    suggBox.innerHTML = listData;
}