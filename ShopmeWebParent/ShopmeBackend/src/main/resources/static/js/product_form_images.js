
$(document).ready(function () {

    // $("#extraImage1").on("change", function () {
    //     if(!checkFileSize(this)) {
    //         return;
    //     }
    //     showExtraImageThumbnail(this);
    // })

    $("input[name='extraImage']").each(function (index) {

        $(this).change(function () {
            if(!checkFileSize(this)) {
                return
            }

            showExtraImageThumbnail(this, index);
        })
    })

    $("input[name='linkRemoveExtraImage']").each(function (index) {
        $(this).click(function (){
            removeExtraImage(index)
        })
    })
})

function showExtraImageThumbnail(fileImage, index) {

    if(!checkFileSize(fileImage)) {
        return
    }

    var file = fileImage.files[0];
    var fileReader = new FileReader()

    fileReader.onload = function (ev) {

        // console.log(ev.target)
        $("#extraThumbnail" + index).attr("src", ev.target.result)
    }
    fileReader.readAsDataURL(file)

    var extraThumbnail = $(`#extraThumbnail${index + 1}`);
    if(extraThumbnail[0]) {
        return;
    }

    addNextExtraImageSection(index + 1)
}

function addNextExtraImageSection(index) {
    html = `
          <div class="col border rounded-1 m-3 p-3" id="extraImage${index}">
                <div id="extraImageHeader${index}">
                    <label class="ms-4">Extra Image #${index + 1}</label>
                </div>
                <div>
                    <img style="width: 200px" id="extraThumbnail${index}" alt="Extra images" class="img-thumbnail my-3" src="${defaultImageThumbnail}">
                </div>

                <div>
                    <input type="file" name="extraImage" accept="image/png, image/jpeg" onchange="showExtraImageThumbnail(this, ${index})">
                </div>
          </div>
    `;

    htmlLinkRemove = `
        <a class="my-3 fas fa-times-circle fa-2xl link-dark float-end" title="Remove this image" href="javascript:removeExtraImage(${index-1})"></a>
    `
    $("#extraImageHeader" + (index-1)).append(htmlLinkRemove);
    $("#productImages").append(html);
}

function removeExtraImage(index) {
    $("#extraImage" + index).remove();
}