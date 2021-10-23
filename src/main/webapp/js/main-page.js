let deleteIcons = document.getElementsByClassName("form-check-input");
let isDeleteMessageShown = false;

function selectDelete(i) {
    if (document.querySelectorAll('.form-check-input:checked').length === 1 && isDeleteMessageShown === false
        || document.querySelectorAll('.form-check-input:checked').length === 0 && isDeleteMessageShown === true) {
        document.getElementsByClassName("alert-danger")[0].classList.toggle("d-none");
        isDeleteMessageShown = !isDeleteMessageShown;
    }
}

for (let i = 0; i < deleteIcons.length; i++) {
    deleteIcons[i].addEventListener('click', () => selectDelete(i));
}
