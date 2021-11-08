var uploadField = document.getElementById("cover");

uploadField.onchange = function() {
    if(this.files[0].size > 2097152){
        alert("File is too big! It should be under 2MB");
        this.value = "";
    } else if(this.files[0].type !== "image/jpeg" && this.files[0].type !== "image/png"){
        alert("You should upload either JPEG or PNG files");
        this.value = "";
    }
};