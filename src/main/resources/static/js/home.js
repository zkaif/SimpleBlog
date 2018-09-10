var typeId = "";
var dateId = "";
var original = "";

$(function () {
    var typeId = sessionStorage.getItem("typeId");
    var typeName = sessionStorage.getItem("typeName");
    setTypeIdNotGetDate(typeId, typeName);
    var dateId = sessionStorage.getItem("dateId");
    var dateName = sessionStorage.getItem("dateName");
    setDateIdNotGetDate(dateId, dateName);
    var original = sessionStorage.getItem("original");
    setOriginalNotGetDate(original);
    sessionStorage.removeItem("typeId");
    sessionStorage.removeItem("typeName");
    sessionStorage.removeItem("dateId");
    sessionStorage.removeItem("dateName");
    sessionStorage.removeItem("original");

    getDate(1);

    if(!isEmpty(typeId)||!isEmpty(dateId)||!isEmpty(original)) {
        layer.msg("已筛选出符合条件的文章");
    }
});

var htmlFlag = "home";

var pageSize = 6;
var listSize = 3;

var urlBlogContent = blogServer + "blogContent/list";

var filtrate = $("#filtrate")

function setTypeId(id, name) {
    setTypeIdNotGetDate(id, name);
    getDate(1);
    layer.msg("已重新筛选出符合条件的文章");
}
function setDateId(id, name) {
    setDateIdNotGetDate(id, name);
    getDate(1);
    layer.msg("已重新筛选出符合条件的文章");
}
function setOriginal(originalTmp) {
    setOriginalNotGetDate(originalTmp);
    getDate(1);
    layer.msg("已重新筛选出符合条件的文章");
}
function setDateIdNotGetDate(id, name) {
    dateId = id;
    if (id == undefined && name == undefined)
        return;
    $("#dateId").remove();
    filtrate.html(filtrate.html() +
        '<div class="chip" id="dateId">' + name + '<i class="close material-icons" onclick="setDateId()">close</i></div>');
}
function setTypeIdNotGetDate(id, name) {
    typeId = id;
    if (id == undefined && name == undefined)
        return;
    $("#typeId").remove();
    filtrate.html(filtrate.html() +
        '<div class="chip" id="typeId">' + name + '<i class="close material-icons" onclick="setTypeId()">close</i></div>');
}
function setOriginalNotGetDate(originalTmp) {
    original = originalTmp;
    if (originalTmp == undefined)
        return;
    $("#original").remove();
    filtrate.html(filtrate.html() +
        '<div class="chip" id="original">' + originalTmp + '<i class="close material-icons" onclick="setOriginal()">close</i></div>');
}

function getDate(page) {
    scrollTo(0, 0);
    if (isEmpty(typeId)) {
        typeId = undefined;
    }
    if (isEmpty(dateId)) {
        dateId = undefined;
    }
    if (isEmpty(original)) {
        original = undefined;
    }
    $.ajax({
        type: "post",
        url: urlBlogContent,
        dataType: 'json',
        data: {
            page: page,
            pageSize: pageSize,
            group: typeId,
            dateStr: dateId,
            type: original
        },
        success: function (msg) {
            //错误处理
            if (msg.code != 0) {
                return;
            }
            //成功处理
            var data = msg.body[0].data;
            var readCount = msg.body[1];
            showPageIndex(msg.body[0].page, msg.body[0].totalPage);
            var html = '';
            for (var i = 0; i < data.length; ++i) {
                if (i % listSize == 0) {
                    html += '<div class="row">';
                }
                if (readCount[data[i].id]==null){
                    readCount[data[i].id] = 0;
                }
                html += '<div class="col s12 m4"><div class="card">';
                html += '   <div class="card-image waves-effect waves-block waves-light"><img class="activator" src="' + data[i].img + '"></div>';
                html += '   <div class="card-content"><div class="row">';
                html += '       <div class="col s12"><span class="card-title activator grey-text text-darken-4">' + data[i].title + '<i class="material-icons right">more_vert</i></span></div>';
                html += '       <div class="col s12"><div class="row">';
                html += '           <div class="col s6">时间：<div><a href="javascript:setDateId(\'' + data[i].dateStr + '\',\''+data[i].dateStr+'\')">' + data[i].date.split(' ')[0] + '</a></div></div>';
                html += '           <div class="col s6">分类：<div><a href="javascript:setTypeId(\'' + data[i].group + '\',\'' + data[i].group + '\')">' + data[i].group + '</a></div></div>';
                html += '           <div class="col s6">类型：<div><a href="javascript:setOriginal(\'' + data[i].type + '\')">' + data[i].type + '</a></div></div>';
                html += '           <div class="col s6">阅读：<div><a href="#!">' + readCount[data[i].id] + '人阅读</a></div></div>';
                html += '       </div></div>';
                html += '       <div class="col s12"><p><a href="' + blogServer + 'blog/' + data[i].id + '.html">阅读文章</a></p></div>';
                html += '   </div></div>';
                html += '   <div class="card-reveal">';
                html += '       <div class="row">';
                html += '           <div class="col s12"><span class="card-title grey-text text-darken-4">' + data[i].title + '<i class="material-icons right">close</i></span></div>';
                html += '           <div class="col s12"><p>' + data[i].digest + '</p></div>';
                html += '           <div class="col s12"><p><a href="' + blogServer + 'blog/' + data[i].id + '.html">阅读全文</a></p></div>';
                html += '       </div>';
                html += '   </div>';
                html += '</div></div>';
                if (i % listSize == 2) {
                    html += '</div>';
                }
            }
            $("#content").html(html);
        },
        error: function (data) {
        }
    });
}
function showPageIndex(page, totalPage) {
    page = page - 0;
    totalPage = totalPage - 0;
    if (page <= 0) {
        page = 1;
    }
    if (totalPage <= 0) {
        totalPage = 1;
    }
    var html = '';
    html += '<li class="' + (page == 1 ? "disabled" : "waves-effect") + '"><a href="' + (page == 1 ? "#!" : 'javascript:getDate(' + (page - 1) + ')') + '"><i class="material-icons">chevron_left</i></a></li>';
    if (totalPage <= 7) {
        for (var i = 1; i <= totalPage; i++) {
            if (i == page) {
                html += '<li class="active" style="background-color: #bdbdbd"><a href="#!">' + i + '</a></li>';
                continue;
            }
            html += '<li class="waves-effect"><a href="javascript:getDate(' + i + ')">' + i + '</a></li>';
        }
    }
    else {
        if (page <= 4) {
            for (var i = 1; i <= 5; i++) {
                if (i == page) {
                    html += '<li class="active" style="background-color: #bdbdbd"><a href="#!">' + i + '</a></li>';
                    continue;
                }
                html += '<li class="waves-effect"><a href="javascript:getDate(' + i + ')">' + i + '</a></li>';
            }
            html += '<li class="waves-effect">...</li>';
            html += '<li class="waves-effect"><a href="javascript:getDate(' + totalPage + ')">' + totalPage + '</a></li>';
        }
        else if (page > 4 && page < totalPage - 3) {
            html += '<li class="waves-effect"><a href="javascript:getDate(1)">1</a></li>';
            html += '<li class="waves-effect">...</li>';
            html += '<li class="waves-effect"><a href="javascript:getDate(' + (page - 1) + ')">' + (page - 1) + '</a></li>';
            html += '<li class="active" style="background-color: #bdbdbd"><a href="#!">' + page + '</a></li>';
            html += '<li class="waves-effect"><a href="javascript:getDate(' + (page + 1) + ')">' + (page + 1) + '</a></li>';
            html += '<li class="waves-effect">...</li>';
            html += '<li class="waves-effect"><a href="javascript:getDate(' + totalPage + ')">' + totalPage + '</a></li>';
        }
        else {
            html += '<li class="waves-effect"><a href="javascript:getDate(1)">1</a></li>';
            html += '<li class="waves-effect">...</li>';
            for (var i = totalPage - 4; i <= totalPage; i++) {
                if (i == page) {
                    html += '<li class="active" style="background-color: #bdbdbd"><a href="#!">' + i + '</a></li>';
                    continue;
                }
                html += '<li class="waves-effect"><a href="javascript:getDate(' + i + ')">' + i + '</a></li>';
            }
        }
    }
    html += '<li class="' + (page == totalPage ? "disabled" : "waves-effect") + '"><a href="' + (page == totalPage ? "#!" : 'javascript:getDate(' + (page + 1) + ')') + '"><i class="material-icons">chevron_right</i></a></li>';

    $("#pageIndex").html(html);
}
