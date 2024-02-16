
function clearFilter() {
    window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
    entityId = link.attr("entityId");
    $("#modalBody").text("Are you sure want to delete this " + entityName + " ID : "  + entityId + " ?");
    $("#yesBtn").attr("href", link.attr("href"));
}