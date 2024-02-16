$(document).ready(function () {
    $("a[id='linkRemoveDetail']").each(function (index) {
        $(this).click(function () {
            removeProductDetailById(index)
        })
    })
})

function addNextDetailsSection() {

    allDetail = $("[id^='itemDetail']");
    previousDetail = allDetail.last();
    previousDetailId = previousDetail.attr("id");

    countDetail = Number(previousDetailId.substring(10)) + 1;

    htmlDetail = `
         <div class="row my-4" id="itemDetail${countDetail}">
            <div class="col-sm-3 d-flex">
                <label class="my-2 mx-4">Name:</label>
                <input type="text" class="form-control" name="detailNames" maxlength="255">
            </div>
            <div class="col-sm-3 d-flex">
                <label class="my-2 mx-4">Value:</label>
                <input type="text" class="form-control" name="detailValues" maxlength="255">
            </div>
        </div>
    `;
    $("#productDetails").append(htmlDetail)

    htmlLinkRemove = `
        <div class="col-sm-1">
            <a class="fas fa-times-circle fa-2x link-dark " title="Remove this details" href="javascript:removeDetailById('${previousDetailId}')"></a>
        </div>
    `
    previousDetail.append(htmlLinkRemove);
    $("input[name='detailNames']").last().focus();
}

function removeDetailById(id) {
    $("#" + id).remove();
}
function removeProductDetailById(index) {
    $("#itemDetail" + index).remove();
}