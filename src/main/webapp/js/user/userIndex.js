

$(function(){
    var hc = $(window).height();

    $(".background-div").css(
        'height',hc
    );

    $(".center-content").css(
        'height',hc*0.29
    );

    $(".center-other").css(
        'height',hc*0.07
    );

    $(".center-img img").css(
        'height',hc*0.1
    );
});

function returnMethod() {
    window.location.href="/toiletCat/gameRoom/gameRoom.html";
}

function toUserAddress() {
    window.location.href="/toiletCat/userAddress/userAddress.html?userNo=";
}

function toUserCatch() {
    window.location.href="/toiletCat/userCatch/userCatch.html?userNo=";
}