$(document).ready(function () {
    $("#btnCancel").on("click", function () {
        window.location = moduleURL;
    })
    $("#fileImage").on("change", function () {
        showImageThumbnail(this);
    })
})

function checkFileSize(fileInput) {
    fileSize = fileInput.files[0].size;
    if(fileSize > 1024*1024) {
        fileInput.setCustomValidity("You must choose an image less than 1MB!");
        fileInput.reportValidity();

        return false;
    }
    else {
        fileInput.setCustomValidity("");

        return true;
    }
}
function showImageThumbnail(fileImage) {
    var file = fileImage.files[0];
    var fileReader = new FileReader()

    fileReader.onload = function (ev) {

        // console.log(ev.target)
        $("#thumbnail").attr("src", ev.target.result)
    }
    fileReader.readAsDataURL(file)
}

