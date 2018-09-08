var listByBlogIdUrl = blogServer + "comment/listByBlogId/";
var createUrl = blogServer + "comment/create";
var addReaderCount = blogServer + "blogContent/addReaderCount/";

var pIdComment=0;

hljs.initHighlighting.called = false;
var ID = $("body").data("id");
showComment();
if(isEmpty(sessionStorage.getItem("blogContent_addReaderCount_flag_"+ID))) {
    $.ajax({
        type: "get",
        url: addReaderCount + ID,
        success: function (msg) {
            //错误处理
            if (msg.code != 0) {
                return;
            }
            sessionStorage.setItem("blogContent_addReaderCount_flag_"+ID,"done");
        },
        error: function (data) {
        }
    });
}

function pushComment() {
    var name = $("#name").val();
    var content = $("#content").val();
    if (isEmpty(name)){
        layer.msg("名称不能为空。");
        return;
    }
    if (isEmpty(content)){
        layer.msg("内容不能为空。");
        return;
    }
    if (name.length>=256){
        layer.msg("名称太长。");
        return;
    }
    if (content.length>=2000){
        layer.msg("内容太长，请保持在2000个字符内。");
        return;
    }
    var pId;
    if(isEmpty(pIdComment)){
        pId="";
    }else{
        pId=pIdComment;
    }
    $.ajax({
        type: "post",
        url: createUrl,
        data : {
            name : name,
            pId : pId,
            blogContentId : ID,
            content : content
        },
        success: function (msg) {
            //错误处理
            if (msg.code != 0) {
                layer.msg("发表失败！");
                return;
            }
            //成功处理
            showComment();
            layer.msg("发表成功");
            $("#name").val('');
            $("#content").val('');
        },
        error: function (data) {
            layer.msg("发表失败，网络错误！");
        }
    });

}

function showComment() {
    $.ajax({
        type: "post",
        url: listByBlogIdUrl+ID,
        success: function (msg) {
            //错误处理
            if (msg.code != 0) {
                return;
            }
            //成功处理
            var data = msg.body[0];
            var html = '';
            for(var i = 0;i<data.length;i++){
                html += '<div class="card-panel">';
                html += '    <div class="row">';
                html += '        <div class="col s6">';
                html += '            <h5>'+data[i].name+'说:</h5>';
                html += '        </div>';
                html += '        <div class="col s6" style="text-align:right; ">';
                html += '            <h6><a href="javascript:quote(\''+data[i].id+'\',\''+data[i].name+'\')">#引用</a></h6>';
                html += '            <h6>'+data[i].date+'</h6>';
                html += '        </div>';
                html += '    </div>';
                if (!isEmpty(data[i].pId)) {
                    for(var n = 0;n<data.length;n++) {
                        if (data[i].pId==data[n].id) {
                            html += '    <blockquote>';
                            html += '        <h6>引用'+data[n].name+'的发言：</h6>';
                            html += '        <p>'+data[n].content+'</p>';
                            html += '    </blockquote>';
                        }
                    }
                }
                html +=      data[i].content;
                html += '</div>';
            }
            $("#commentContent").html(html);
            hljs.initHighlighting();
        },
        error: function (data) {
        }
    });
}
function quote(id,name) {
    pIdComment=id;
    var html="";
    html += '发表评论: ';
    html += '<div class="chip">';
    html += '   引用'+name+'的发言';
    html += '   <i class="close material-icons" onclick="quoteClose()">close</i>';
    html += '</div>';
    $("#quoteFlag").html(html);
}
function quoteClose() {
    pIdComment=0;
}