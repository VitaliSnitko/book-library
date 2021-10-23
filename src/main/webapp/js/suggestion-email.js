let searchWrapper = document.getElementsByClassName('search-input')[0];
let inputBox = document.getElementById('recordEmail');
let suggBox = document.getElementsByClassName("autocom-box")[0];
inputBox.onkeyup = (e) => {
    let userData = e.target.value;
    let emptyArray = [];
    if (userData) {
        emptyArray = suggestions.filter((data) => {
            return data.toLocaleLowerCase().startsWith(userData.toLocaleLowerCase());
        });
        emptyArray = emptyArray.map((data) => {
            return data = `<li>${data}</li>`;
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
    inputBox.value = element.textContent;
    searchWrapper.classList.remove("active");
}

function showSuggestions(list) {
    let listData;
    if (!list.length) {
        listData = `<li>${inputBox.value}</li>`;
    } else {
        listData = list.join('');
    }
    suggBox.innerHTML = listData;
}