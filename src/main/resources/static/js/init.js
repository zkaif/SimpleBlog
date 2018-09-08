(function($){
    $(function(){
        $('.button-collapse').sideNav();
        $('.parallax').parallax();
        $('.collapsible').collapsible();
    }); // end of document ready
})(jQuery); // end of jQuery name space

var urlType = blogServer + "blogType/list";
var urlDate = blogServer + "dateType/list";
$.ajax({
    type: "get",
    url: urlType,
    dataType: 'json',
    success: function (msg) {
        //错误处理
        if (msg.code != 0) {
            return;
        }
        //成功处理
        var data = msg.body[0];
        var html = '';
        var htmlCollapsible = '';
        for (var i = 0; i < data.length; ++i) {
            if(data[i].blogCount<=0){
                continue;
            }
            html += '<li><a href="javascript:toHomeByType(\''+data[i].name+'\',\''+data[i].name+'\')" class="grey-text text-darken-3">' + data[i].name + '<br />（共' + data[i].blogCount + '篇）</a></li>';
            htmlCollapsible += '<li><a href="javascript:toHomeByType(\''+data[i].name+'\',\''+data[i].name+'\')" style="padding-left: 45px">' + data[i].name + '（共' + data[i].blogCount + '篇）</a></li>';
        }
        $("#typeMenu").html(html);
        $("#typeMenuCollapsible").html(htmlCollapsible);
    },
    error: function (data) {
    }
});

$.ajax({
    type: "get",
    url: urlDate,
    dataType: 'json',
    success: function (msg) {
        //错误处理
        if (msg.code != 0) {
            return;
        }
        //成功处理
        var data = msg.body[0];
        var html = '';
        var htmlCollapsible = '';
        for (var i = 0; i < data.length; ++i) {
            if(data[i].blogCount<=0){
                continue;
            }
            var dates = data[i].date.split('-');
            html += '<li><a href="javascript:toHomeByDate(\''+data[i].date+'\',\''+data[i].date+'\')" class="grey-text text-darken-3">' +  data[i].date + '<br />（共' + data[i].blogCount + '篇）</a></li>';
            htmlCollapsible += '<li><a href="javascript:toHomeByDate(\''+data[i].date+'\',\''+data[i].date+'\')" style="padding-left: 45px">' +  data[i].date + '（共' + data[i].blogCount + '篇）</a></li>';
        }
        $("#dateMenu").html(html);
        $("#dateMenuCollapsible").html(htmlCollapsible);
    },
    error: function (data) {
    }
});

function toHomeByDate(id,name) {
    if(htmlFlag=="home"){
        setDateId(id,name);
    }
    else{
        sessionStorage.setItem("dateId",id);
        sessionStorage.setItem("dateName",name);
        window.location.href = "/index.html";
    }
}
function toHomeByType(id,name) {
    if(htmlFlag=="home"){
        setTypeId(id,name);
    }
    else{
        sessionStorage.setItem("typeId",id);
        sessionStorage.setItem("typeName",name);
        window.location.href = "/index.html";
    }
}



function getBio() {
    layer.msg("抱歉，您没有获取简历的权限！！")
}
