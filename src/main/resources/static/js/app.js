var blogServer = "/";
function isEmpty(text) {
    if (typeof (text) == "undefined" || null == text || text == ""
        || text == "null") {
        return true;
    }
    return false;
}
function getParam(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    if (isEmpty(url)) {
        url = window.location.search.substr(1);
    } else {
        var index = url.indexOf("?");
        url = url.substring(index - 0 + 1);
    }
    var r = url.match(reg);
    if (r != null)
        return unescape(r[2]);
    return "";
}

var htmlFlag="";
