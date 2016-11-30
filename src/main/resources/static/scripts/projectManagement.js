
$(function () {
    init();
    showFilter();
    show();
    over();
});
var myStatus = -1;
function init() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    for(var i=1;i<=6;i++) {
        var sz = i*5;
        $('#size').append('<option value="' + sz + '">' + sz + '</option>');
    }
    setTotPages();
    $('#showPic').hide();
    $('#first').hide();
    $('#prev').hide();
    $.get('/getMyAdmin',function (data) {
        // alert(JSON.stringify(data));
        myStatus = data.status;
    });
}

function over() {
    $('#add').removeAttr('disabled');
    $('#excel').removeAttr('disabled');
    $('#del').removeAttr('disabled');
    $('#pass').removeAttr('disabled');
    $('#noPass').removeAttr('disabled');
}

function showFilter() {
    var filter = $('#filter').val();
    setSelectItems('filter-list',filter);
}

$('#add').click(function () {
    if(validate()==false) {
        return;
    }
    window.location = '/projectAdd';
});

$('#excel').click(function () {
    if(validate()==false) {
        return;
    }
    window.open('/excel', 'newWindow', 'height=400,width=700');
});

$('#filter').change(function () {
    $('#filter-list').html('');
    if($('#filter').val() != "未选择") {
        $('#filter-list').append('<option value="未选择">未选择</option>');
    }
    showFilter();
    if($(this).val() == '未选择') {
        $('#tbody').html('');
        show();
    }
});


$('#filter-list').change(function () {
    changePage();
});

$('#selCkb').click(function () {
    $('input:checkbox[name="ckb"]').prop('checked',this.checked);
});

$('#size').change(function () {
    $('#cur').text(1);
    changePage();
});

$('#del').click(function () {
    if(validate()==false) {
        return;
    }
    var ids = new Array();
    $('input:checkbox[name="ckb"]:checked').each(function () {
        $tr = $(this).parent().parent();
        var state = $tr.find('td').eq(-2).text();
        if(state=='通过') {
            layer.tips('不能删除已通过的项目','#selCkb');
            $(this).prop('checked','');
        } else {
            var id = $tr.find('input').eq(0).val();
            ids.push(id);
        }
    });
    if(ids.length==0) {
        return;
    }
    opAjax("/deleteProjects",ids);
});

$('#pass').click(function () {
    if(validate()==false) {
        return;
    }
    var ids = getCheckedIds();
    if(ids.length==0) {
        return;
    }
    var data = new Object();
    data.id = ids;
    data.status = "通过";
    opAjax('/changeProjectsStatus',data);
});

$('#noPass').click(function () {
    if(validate()==false) {
        return;
    }
    var sz = $('input:checkbox[name="ckb"]:checked').size();
    if (sz == 0) {
        return;
    }
    $('#noPassModal').modal('show');
    $('#noPassReason').val('');
});

$('#save').click(function () {
    var ids = getCheckedIds();
    if(ids.length==0) {
        return;
    }
    var data = new Object();
    data.id = ids;
    data.status = "不通过";
    data.msgForbid = $('#noPassReason').val();
    opAjax('/changeProjectsStatus',data);
});

function validate() {
    if(myStatus == 0) {
        layer.tips('没有权限，请等管理员开启','#selCkb');
        return false;
    }
    return true;
}

function opAjax(url,data) {
    $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function () {
            changePage();
            layer.tips('更新成功','#selCkb');
            $('#selCkb').prop('checked','');
            $('#noPassModal').modal('hide');
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            alert(status + " " + errorThrown);
        }
    });
}

$('#first').click(function () {
    $('#cur').text(1);
    setList();
});

$('#prev').click(function () {
    var cur = Number($('#cur').text());
    $('#cur').text(cur-1);
    setList();
});

$('#next').click(function () {
    var cur = Number($('#cur').text());
    $('#cur').text(cur+1);
    setList();
});

$('#last').click(function () {
    $('#cur').text(($('#tot').text()));
    setList();
});

function getCheckedIds() {
    var data = new Array();
    $('input:checkbox[name="ckb"]:checked').each(function () {
        $tr = $(this).parent().parent();
        var id = $tr.find('input').eq(0).val();
        data.push(id);
    });
    return data;
}

function show() {
    var url = '/getProjects/'+$('#filter').val()+'/'
        +$('#filter-list').val() +'/'+($('#cur').text()-1)+'/'+$('#size').val();
    $.get(url,function (data) {
        list(data);
    })
}

function changePage() {
    setList();
    setTotPages();
}

function setList() {
    var url = '/getProjects/'+$('#filter').val()+'/'
        +$('#filter-list').val() + '/' + ($('#cur').text()-1)+'/'+$('#size').val();
    $.get(url,function (data) {
        // alert(JSON.stringify(data));
        $('#tbody').html('');
        list(data);
        var cur = $('#cur').text();
        $('#first').show();
        $('#prev').show();
        $('#next').show();
        $('#last').show();
        if(cur==1) {
            $('#first').hide();
            $('#prev').hide();
        }
        if(cur==$('#tot').text()) {
            $('#next').hide();
            $('#last').hide();
        }
    });
}

function setTotPages() {
    var url = 'getTotPages/'+$('#size').val()+'/'+$('#filter').val()+'/'
                 +$('#filter-list').val();
    $.get(url,function (data) {
        $('#tot').text(data);
        $('#cur').text(1);
        if(data==1) {
            $('#next').hide();
            $('#last').hide();
        }
    })
}


function list(data) {
    var content = data.content;
    for(var i=0;i<content.length;i++) {
        var project = content[i];
        var line = '<tr>';
        var size = Number($('#size').val());
        var num = (Number($('#cur').text())-1) * size + i + 1;
        line += '<input type="hidden" id="id" name="id" value="'+project.id+'"/>';
        line += '<input type="hidden" id="filePath" name="filePath" value="'+project.filePath+'"/>';
        line += '<input type="hidden" id="msgForbid" name="msgForbid" value="'+project.msgForbid+'"/>';
        line += '<td>' + (num) + '</td>';
        line += '<td><a href="/projectEdit?project='+project.id+'">'+ project.projectName+'</a></td>';
        line += '<td>' + project.projectDate + '</td>';
        line += '<td>' + project.level + '</td>';
        line += '<td>' + project.rank + '</td>';
        var studentList = project.studentList;
        var type = '个人';
        if(studentList.length>1) {
            type = '团体';
        }
        var names = '';
        for(var k=0;k<studentList.length;k++) {
            names += studentList[k].studentName+' ';
        }
        line += '<td>' + type + '</td>';
        line += '<td>' + names + '</td>';
        line += '<td>' + project.captainCollege + '</td>';
        line += '<td>' + project.teacher + '</td>';
        if(project.filePath=='') {
            line += '<td>' + project.photoStatus + '</td>';
        } else {
            line += '<td><a href="javascript:void(0)" onclick="showImg(this)">已上传</a></td>';
        }
        line += '<td><a href="javascript:void(0)" onclick="showState(this)">' + project.state + '</a></td>';
        line += '<td>' + '<input type="checkbox" name="ckb"/>' +'</td>';
        line += '</tr>';
        $('#tbody').append(line);
        // alert(student.id);
    }
}

function showImg(obj) {
    var par = $(obj).parent().parent();
    var filePath = par.find('input').eq(1).val();
    $('#showPic').show();
    $('#pic').attr('src','/getLocalPic/'+filePath);
}

function showState(obj) {
    var par = $(obj).parent().parent();
    var msgForbid = par.find('input[name="msgForbid"]').val();
    var state = $(obj).text();
    $('#showPassModal').modal('show');
    $('#result').val(state);
    if(state == '不通过') {
        $('#reason').val(msgForbid);
        $('#reasonGroup').show();
    } else{
        $('#reasonGroup').hide();
    }
}

$('#showPic').click(function () {
   $(this).hide();
});

/*
var curr = 1;
$(function () {
    load(curr);
})


function load(curr) {
    $.ajax({
        url: "../Json/Index.aspx",
        timeout: 300000,
        dataType: "json",
        type: "post",
        data: { "flag": "load", "curr": curr },
        success: function (data) {

            var html = "";
            $.each(data.items, function (i, item) {
                html += " <tr> " +
                        " <td>" + item.userName + "</td> " +
                        " <td>" + item.Chinese + "</td> " +
                        " <td>" + item.Math + "</td> " +
                        " <td>" + item.English + "</td> " +
                        " <td><a class=\"btn btn-info\" onclick='openedt(\"" + item.userName + "\");'>修改</a>&nbsp;&nbsp;<a class=\"btn btn-warning\" onclick='del(\"" + item.userName + "\");'>删除</a></td> " +
                        " </tr>";
            })
            $("#tbody").html(html);
            laypage({
                cont: 'page', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                pages: Math.ceil(data.cnt / 10), //通过后台拿到的总页数
                skin: "#49afcd",
                curr: curr || 1, //当前页
                jump: function (obj, first) { //触发分页后的回调
                    if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                        curr = obj.curr;
                        load(curr);
                    }
                }
            });

        }
    })
}*/
/*
function openadd() {
    $("#myModalLabel").text("添加项目");
    $("#userName").attr("readonly", false);
    $("input").val("");
    $("#addModal").modal("show");
    $("#add").show();
    $("#edt").hide();
}


function add() {
    if ($("#college").val() == "") {
        layer.tips('不能为空', '#college');
        return;
    }
    if ($("#schoolId").val() == "") {
        layer.tips('不能为空', '#schoolId');
        return;
    }
    if ($("#Name").val() == "") {
        layer.tips('不能为空', '#Name');
        return;
    }
    if ($("#competitionName").val() == "") {
        layer.tips('不能为空', '#competitionName');
        return;
    }
    if ($("#competitionRank").val() == "") {
        layer.tips('不能为空', '#competitionRank');
        return;
    }
    if ($("#competitionHost").val() == "") {
        layer.tips('不能为空', '#competitionHost');
        return;
    }
    if ($("#competitionTime").val() == "") {
        layer.tips('不能为空', '#competitionTime');
        return;
    }if ($("#competitionMethod").val() == "") {
        layer.tips('不能为空', '#competitionMethod');
        return;
    }


    var formdata = {
        flag: "add",
        userName: $("#userName").val(),
        Chinese: $("#Chinese").val(),
        Math: $("#Math").val(),
        English: $("#English").val()
    }
    $.ajax({
        url: "../Json/Index.aspx",
        timeout: 300000,
        dataType: "json",
        type: "post",
        data: formdata,
        success: function (data) {
            $("#addModal").modal("hide");
            layer.alert(data.msg);
            $("input").val("");
            load(curr);
        }
    })
}


function openedt(userName) {
    $.ajax({
        url: "../Json/Index.aspx",
        timeout: 300000,
        dataType: "json",
        type: "post",
        data: { "flag": "getUser", "userName": userName },
        success: function (data) {
            $("#myModalLabel").text("修改成绩");
            $("#userName").val(data.userName);
            $("#userName").attr("readonly", true);
            $("#Chinese").val(data.Chinese);
            $("#Math").val(data.Math);
            $("#English").val(data.English);

            $("#edt").show();
            $("#add").hide();
            $("#addModal").modal("show");
        }
    })
}

function edt() {
    if ($("#userName").val() == "") {
        layer.tips('不能为空', '#userName');
        return;
    }
    if ($("#Chinese").val() == "") {
        layer.tips('不能为空', '#Chinese');
        return;
    }
    if ($("#Math").val() == "") {
        layer.tips('不能为空', '#Math');
        return;
    }
    if ($("#English").val() == "") {
        layer.tips('不能为空', '#English');
        return;
    }
    var formdata = {
        flag: "edt",
        userName: $("#userName").val(),
        Chinese: $("#Chinese").val(),
        Math: $("#Math").val(),
        English: $("#English").val()
    }
    $.ajax({
        url: "../Json/Index.aspx",
        timeout: 300000,
        dataType: "json",
        type: "post",
        data: formdata,
        success: function (data) {
            $("#addModal").modal("hide");
            layer.alert(data.msg);
            $("input").val("");
            load(curr);
        }
    })
}


function del(userName) {
    //询问框
    layer.confirm('您确定要删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            url: "../Json/Index.aspx",
            timeout: 300000,
            dataType: "json",
            type: "post",
            data: { "flag": "del", "userName": userName },
            success: function (data) {
                layer.alert(data.msg);
                load(curr);
            }
        })
    }, function () {
        //  layer.close();
    });

}

$(function(){
    $('input:radio[name="comMethod"]').change( function(){
        $("#partner").toggle();
    });
})
 */
