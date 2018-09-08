$('#pushContactMessage').click(function () {
    var name = $("#name").val();
    var email = $("#email").val();
    var title = $("#title").val();
    var content = $("#content").val();
    // var mailTest = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{1,6}$/;
    if (isEmpty(name)){
        layer.msg("名称不能为空！");
        return;
    }
    if (isEmpty(email)){
        layer.msg("邮箱不能为空！");
        return;
    }
    // if (!mailTest.test(email)){
    //     layer.msg("邮箱格式不正确！");
    //     return;
    // }
    if (isEmpty(title)){
        layer.msg("标题不能为空！");
        return;
    }
    if (isEmpty(content)){
        layer.msg("内容不能为空！");
        return;
    }
    var url = blogServer + "contactMessage/create";
    $.ajax({
        type: "post",
        url: url,
        data: {
            dType: 'json',
            name: name,
            email: email,
            title: title,
            content: content
        },
        success: function (msg) {
//                //错误处理
            if (msg.code != 0) {
                layer.msg("留言失败！");
                return;
            }
//                //成功处理
            $("#name").val("");
            $("#email").val("");
            $("#title").val("");
            $("#content").val("");
            layer.msg("你好" + name + "，留言已收到，如果需要会通过邮件与您联系！");
        },
        error: function (data) {
            layer.msg("留言失败，网络错误！");
        }
    });
});