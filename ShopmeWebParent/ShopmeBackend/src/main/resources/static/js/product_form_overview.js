var dropdownBrands = $("#brand");
var dropdownCategories = $("#category");

$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function () {
        dropdownCategories.empty();
        getCategories();
    })

    getCategoriesForNewForm()
})

function getCategoriesForNewForm() {
    categoryIdField = $("#categoryId");
    editMode = false;

    if(categoryIdField.length) {
        editMode = true;
    }
    if(!editMode) {
        getCategories();
    }
}
function getCategories() {
    var brandId = dropdownBrands.val();
    var url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function (response) {
        $.each(response, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
    })
}

function checkUnique(form) {
    var productId = $("#id").val();
    var productName = $("#name").val();
    var csrfValue = $("input[name='_csrf']").val();

    var params = {
        id: productId,
        name: productName,
        _csrf: csrfValue
    }

    $.post(checkUniqueURL, params, function (response) {

        if(response === "OK") {
            form.submit();
        }
        else if (response === "Duplicate") {
            showModalDialog("Warning", "There is another category having the same name " + productName)
        }
        else {
            showModalDialog("Error", "Unknown response from server");
        }
    }).fail(function () {
        showModalDialog("Error", " Could not connect to server");
    });

    return false;
}
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
}

function addToggle() {
    $("#btnSubmit").attr("data-bs-toggle", "modal");
    $("#modalBody").text("You could input require field before save");
}