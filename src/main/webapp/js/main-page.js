document.addEventListener("DOMContentLoaded", setHrefs);

function setHrefs() {
    let urlParams = new URLSearchParams(window.location.search);
    let currentPage = urlParams.get('page');
    if (currentPage === null) {
        currentPage = 1;
    }

    let prevPageButt = document.getElementsByClassName('prevPageButt')[0];
    if (prevPageButt !== undefined) {
        urlParams.set('page', Number(currentPage) - 1);
        prevPageButt.href = '/main?' + urlParams;
    }
    let nextPageButt = document.getElementsByClassName('nextPageButt')[0];
    if (nextPageButt !== undefined) {
        urlParams.set('page', Number(currentPage) + 1)
        nextPageButt.href = '/main?' + urlParams;
    }
    let pageButts = document.getElementsByClassName('pageButt');
    for (let i = 0; i < pageButts.length; i++) {
        urlParams.set('page', i+1)
        pageButts[i].href = '/main?' + urlParams;
    }
}

let deleteIcons = document.getElementsByClassName("form-check-input");
let isDeleteMessageShown = false;

function selectDelete() {
    if (document.querySelectorAll('.form-check-input:checked').length === 1 && isDeleteMessageShown === false
        || document.querySelectorAll('.form-check-input:checked').length === 0 && isDeleteMessageShown === true) {
        document.getElementsByClassName("alert-danger")[0].classList.toggle("d-none");
        isDeleteMessageShown = !isDeleteMessageShown;
    }
}

for (let i = 0; i < deleteIcons.length; i++) {
    deleteIcons[i].addEventListener('click', () => selectDelete());
}
