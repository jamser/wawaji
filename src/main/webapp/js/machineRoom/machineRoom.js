// 每页数据数
var pageSize = 0;

var nowPage = 1;

var showUrl = "/toiletCat/api/machine/getAllMachineByPage.action";

var width = $(window).width() / 2 - 20;

var userNo = "";

var bannerType = 0;

$(function() {
    var url = "/toiletCat/api/machine/getMachineTotalCountAndPageSize.action";

    // 获取banner图
    getBannerByType(bannerType);

    // 获得数据量
    getTotalCountAndPageSize(url);

    // 获得数据
    getAllMachineByPage(nowPage);

    // 自动登陆
    userAutoLogin();
});

// 用户自动登陆
function userAutoLogin() {
    $.ajax({
        url:"/toiletCat/api/user/autoLogin.action",
        type:"POST",
        async:false,
        success:function(data) {
            // 转换数据
            if(typeof(data) == "string") {
                data = eval("("+data+")");
            }

            // 判断是否成功
            if(data["is_success"] != "success") {
                toiletCatMsg(data["result"], null);
                return;
            }

            var result = data["result"];

            // 判断是否成功获取用户信息
            if(result == "fail") {
                console.info(result);
                return;
            }

            if(typeof(result) == "string") {
                result = eval("("+result+")");
            }
            userNo = result["userNo"];

        }
    });
}


// 用户登陆或注册
function userLoginOrRegister() {
    $.ajax({
        url:"/toiletCat/api/user/registerOrLoginUser.action",
        type:"POST",
        async:false,
        data:{
            mobileNO:mobileNo,
            ticket:ticket
        },
        success:function(data) {
            // 转换数据
            if(typeof(data) == "string") {
                data = eval("("+data+")");
            }

            // 判断是否成功
            if(data["is_success"] != "success") {
                toiletCatMsg(data["result"], null);
                return;
            }

            var result = data["result"];

            if(result == "验证码错误,请重试") {
                toiletCatMsg(result, null);
                return;
            }

            if(typeof(result) == "string") {
                result = eval("("+result+")");
            }

            console.info(result);
            userNo = result["userNo"];

        }
    });
}

function getAllMachineByPage(nowPage) {
    var startPage = (nowPage - 1) * pageSize;

    $.ajax({
        url:"/toiletCat/api/machine/getUserAllMachineByPage.action",
        type:"POST",
        async:false,
        data:{
            startPage:startPage
        },
        success:function(data) {
            // 转换数据
            if(typeof(data) == "string") {
                data = eval("("+data+")");
            }

            // 判断是否成功
            if(data["is_success"] != "success") {
                toiletCatMsg(data["result"], null);
                return;
            }

            var list = data["result"];

            var str = "";

            for(var i = 0; i<list.length; i++) {

                if(i % 2 == 0) {
                    str += "<div class='row' style='margin-bottom: 5px'>";
                }

                str += "<div class='toiletCat-col-xs-6' onclick='toGamePage("+list[i]["gameRoomNo"]+")'>"
                str += "    <div class='machine-panel panel-info'>";
                str += "        <div class='panel-body'>";
                str += "            <div class='toy-img index-img'>"
                str += "                <img height='100px' width=100% src='" + list[i]["toyImg"] + "' class='index-img' />";
                str += "            </div>";
                str += "            <div style='margin-bottom: 2px'><span>" + list[i]["toyName"] + "</span></div>";
                str += "            <div><span>围观:" + list[i]["viewer"] + "</span></div>";
                str += "            <div><span class='my-inline-right' ><img src='/image/background/coin_img.png' />:" + list[i]["toyNowCoin"] + "</span>";
                if(list[i]["available"] == "true") {
                    str += "        空闲</div>";
                } else {
                    str += "        <span class='my-inline-left'><img src='/image/background/busy.ico' />使用中</span></div>";
                }
                str += "        </div>";
                str += "    </div>";
                str += "</div>";

                if(i % 2 != 0 || i == list.length - 1) {
                    str += "</div>";
                }
            }
            $("#main").append(str);

        }
    });
}

function toIndex() {
    window.location.href="/toiletCat/index/machineRoom.html";
}

function getPage(page) {
    nowPage = getPageByNum(nowPage, page, totalPage, step);
    getAllByPage(showUrl, nowPage);

}

function nextPage() {
    nowPage = nextPageNum(nowPage, totalPage, step);
    getAllByPage(showUrl, nowPage);
}

//上一页
function lastPage() {
    nowPage = lastPageNum(nowPage, totalPage, step);
    getAllByPage(showUrl, nowPage);
}

function toUserToy() {
    window.location.href="/toiletCat/userToy/userToy.html?userNo="+userNo;
}

function toRecharge() {
    window.location.href="/toiletCat/userToy/userToy.html?userNo="+userNo;
}

function toUserIndex() {
    window.location.href="/toiletCat/userToy/userToy.html?userNo="+userNo;
}