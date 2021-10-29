let searchWrapper = document.getElementsByClassName('search-input')[0];
let emailAddInput = document.getElementById('recordEmail');
let nameAddInput = document.getElementById('recordName');
let suggBox = document.getElementsByClassName("autocom-box")[0];

emailAddInput.onkeyup = (e) => {
    let actualInput = e.target.value;
    let suggestionContainer = [];
    if (actualInput) {
        suggestionContainer = emailSuggestions.filter((emailSuggestion) => {
            return emailSuggestion.toLocaleLowerCase().startsWith(actualInput.toLocaleLowerCase());
        }).map((emailSuggestion) => {
            return `<li>${emailSuggestion}</li>`;
        });
        searchWrapper.classList.add("active"); //show autocomplete box
        if (suggestionContainer.length !== 0) {
            showSuggestions(suggestionContainer);
        }
        let allList = suggBox.querySelectorAll("li");
        for (let i = 0; i < allList.length; i++) {
            allList[i].setAttribute("onclick", "select(this)");
        }
    } else {
        searchWrapper.classList.remove("active"); //hide autocomplete box
    }
}

function select(element) {
    emailAddInput.value = element.textContent;
    nameAddInput.value = nameSuggestions[emailSuggestions.indexOf(element.textContent)];
    searchWrapper.classList.remove("active");
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