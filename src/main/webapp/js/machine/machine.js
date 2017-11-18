// 开始页码
var startPage = 0;

// 总数据数
var totalCount = 0;

// 每页数据数
var pageSize = 0;

var step = 5;

var nowPage = 1;


$(function(){
    // 获得总页数和总数量
    getTotalCountAndPageSize();
    // 分页获得所有记录
    getAllToyByPage();
    // 初始化页码
    initPage(totalPage,step);
});

// 根据分页获得所有玩具数据
function getAllToyByPage() {
    $.ajax({
        url:"/wawaji/machine/getAllMachineByPage.action",
        type:"POST",
        async:false,
        data:{
            startPage:startPage
        },
        success:function(data) {

            data = eval("(" + data + ")");

            var list = data["list"];
            var str = "";
            //console.info("length:"+s.length);
            for(var i = 0;i<list.length;i++) {

                str += "<tr id=tr"+list[i]["id"]+" class='even gradeA'>";

                $("#titleColumn").find("lable").each(function() {

                    var col = $(this).attr("name");

                    var td = "<td>";

                    if(col == "operation") {

                        td += "<a href='/wawaji/machine/machineDetailPage.jsp?type=update&id="+list[i]['id']+"&toyNo="+list[i]['machineNo']+"'>修改</a> | ";
                        td += "<a href='javascript:void(0);' onclick=deleteThis('"+list[i]['id']+"','"+list[i]['machineNo']+"')>删除</a>";

                    } else if(col == "currentState") {
                        if(list[i][col] == "0") {
                            td += "禁用";
                        } else if(list[i][col] == "1") {
                            td += "可用";
                        }
                    } else {
                        //如果数据为空则写无
                        if((list[i][col] == undefined) || (list[i][col] == null) || (list[i][col] == "null")) {
                            td += "无";
                        } else {
                            td += list[i][col] + "";
                        }
                    }

                    td += "</td>";

                    str += td;
                });
                str += "</tr>";
            }

            $("#dataBody").append(str);
        }

    });
}

function deleteThis(id, toyNo) {
    $.ajax({
        url:"/wawaji/machine/deleteToyByIdAndToyNo.action",
        type:"POST",
        async:false,
        data:{
            id:id,
            toyNo:toyNo
        },
        success:function(data){

            if(data == "success") {
                $("#tr"+id).remove();
            }

        }

    });
}

// 获得当前玩具总数量和分页数据
function getTotalCountAndPageSize() {
    $.ajax({
        url:"/wawaji/machine/getMachineTotalCountAndPageSize.action",
        type:"POST",
        async:false,
        data:{
            startPage:startPage
        },
        success:function(data){

            data = eval("(" + data + ")");

            totalCount = data["totalCount"];

            pageSize = data["pageSize"];

            if(totalCount <= pageSize) {
                totalPage = 1;
            } else if(totalCount % pageSize == 0) {
                totalPage = totalCount/pageSize;
            } else {
                totalPage = parseInt(totalCount/pageSize);
            }

        }

    });
}

function getPage(page){
    nowPage = getPageByNum(nowPage,page,totalPage,step);

}

function nextPage(){
    nowPage = nextPageNum(nowPage,totalPage,step);

}

//上一页
function lastPage(){
    nowPage = lastPageNum(nowPage,totalPage,step);
}

function toAddPage() {
    window.location.href="/wawaji/machine/machineDetailPage.jsp?type=save";
}