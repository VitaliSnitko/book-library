var uploadField = document.getElementById("cover");

uploadField.onchange = function() {
    if(this.files[0].size > 2097152){
        alert("File is too big!");
        this.value = "";
    } else if(this.files[0].type !== "image/jpeg" && this.files[0].type !== "image/png"){
        alert("You should upload JPEG or PNG files");
        this.value = "";
    }
};