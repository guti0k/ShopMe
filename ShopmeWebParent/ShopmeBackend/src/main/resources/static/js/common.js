$(document).ready(function () {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })

    customizeDropDownMenu();
})

function customizeDropDownMenu() {
    $(".dropdown .icon-user").click(function () {
        location.href = this.href;
    })
    $(".dropdown").hover(
        function () {
            $(this).find(".dropdown-menu").first().stop(true, true).delay(250).slideDown();
    },
        function () {
            $(this).find(".dropdown-menu").first().stop(true, true).delay(100).slideUp();
    })
}


