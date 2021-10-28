let searchWrapper = document.getElementsByClassName('search-input')[0];
let emailAddInput = document.getElementById('recordEmail');
let nameAddInput = document.getElementById('recordName');
let suggBox = document.getElementsByClassName("autocom-box")[0];
emailAddInput.onkeyup = (e) => {
    let actualInput = e.target.value;
    let emptyArray = [];
    if (actualInput) {
        emptyArray = emailSuggestions.filter((data) => {
            return data.toLocaleLowerCase().startsWith(actualInput.toLocaleLowerCase());
        }).map((data) => {
            return `<li>${data}</li>`;
        });
        searchWrapper.classList.add("active"); //show autocomplete box
        showSuggestions(emptyArray);
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