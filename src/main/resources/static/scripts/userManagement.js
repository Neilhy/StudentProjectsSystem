/**
 * Created by Administrator on 2016/9/6.
 */
function del() {
    //询问框
    layer.confirm('您确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        /*$.ajax({
            url: "../Json/Index.aspx",
            timeout: 300000,
            dataType: "json",
            type: "post",
            data: { "flag": "del", "userName": userName },
            success: function (data) {
                layer.alert(data.msg);
                load(curr);
            }
        })*/
    }, function () {
        //  layer.close();
    });

}