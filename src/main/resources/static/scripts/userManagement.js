$(document).ready(function () {
    initToken();
    show('all');
    setSelectItems('college','学院');

    $('#role').change(function () {
        var role = $(this).val();
        if(role == 'ROLE_INNER') {
            if(innerAdmin == '') {
                show(role);
            } else {
                $('#tbody').html('');
                $('#tbody').append(innerAdmin);
            }
        } else if(role == 'ROLE_OUTER') {
            if(outerAdmin == '') {
                show(role);
            } else {
                $('#tbody').html('');
                $('#tbody').append(outerAdmin);
            }
        } else {
            if(allAdmin == '') {
                show(role);
            } else {
                $('#tbody').html('');
                $('#tbody').append(allAdmin);
            }
        }
    });

    $('input[name="roleType"]').change(function(){
        if($(this).val() == "ROLE_INNER") {
            $('#inner').hide();
        } else {
            $('#inner').show();
        }
    });

    $('#add').click(function () {
        var college = $('#college').val();
        var username = $('#Name').val();
        var password = $('#Password').val();
        var roleType = $('input[name="roleType"]:checked').val();
        var statusVal = $('input[name="status"]:checked').val();
        var status = statusVal == '开启'?1:0;
        if(roleType == 'ROLE_INNER') {
            college = '';
        }
        if(username == '') {
            layer.tips('不能为空', '#Name');
            return;
        }
        if($.inArray(username,userNames) != -1) {
            layer.tips('已存在', '#Name');
            return;
        }
        if(password == '') {
            password = '000000';
        }
        var data = new Object();
        data.college = college;
        data.username = username;
        data.password = password;
        data.roleType = roleType;
        data.status = status;
        $.ajax({
            type: "POST",
            url: "/addAdmin",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function () {
                $("#addModal").modal("hide");
                refresh('all');
                layer.tips('添加成功','#add');
                // $(document).find('html').html(data);
                // initToken();
            },
            error: function (XMLHttpRequest, status, errorThrown) {
                alert(status + " " + errorThrown);
            }
        })
    });

    $('#edt').click(function () {
        //TODO 保存修改内容
        var id = $('input[name="id"]').val();
        var college = $('#college').val();
        var username = $('#Name').val();
        var roleType = $('input[name="roleType"]:checked').val();
        var statusVal = $('input[name="status"]:checked').val();
        var status = statusVal == '开启'?1:0;
        if(roleType == 'ROLE_INNER') {
            college = '';
        }
        if(username == '') {
            layer.tips('不能为空', '#Name');
            return;
        }

        var data = new Object();
        data.id = id;
        data.password = "";
        data.college = college;
        data.username = username;
        data.roleType = roleType;
        data.status = status;
        $.ajax({
            type: "POST",
            url: "/modifyAdmin",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function () {
                $("#addModal").modal("hide");
                refresh('all');
                layer.tips('修改成功','#add');
                // $(document).find('html').html(data);
                // initToken();
            },
            error: function (XMLHttpRequest, status, errorThrown) {
                alert(status + " " + errorThrown);
            }
        })
    });



    $('#Name').blur(function () {
        if($('#myModalLabel').text() == '修改信息')
            return;
        var userName = $(this).val();
        if($.inArray(userName,userNames) != -1) {
            layer.tips('已存在', '#Name');
        }
    });

    $('#delete').click(function () {
        var data = getCheckedIds();
        $.ajax({
            type: 'POST',
            url: '/deleteAdmin',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success:function (data) {
                refresh($('#role').val());
                // alert('删除成功');
                layer.tips('删除成功','#add');
                $('#selCkb').prop('checked',false);
            },
            error: function (XMLHttpRequest, status, errorThrown) {
                alert(status + " " + errorThrown);
            }
        });
    });

    $('#openStatus').click(function () {
        changeStatus('open');
    });
    $('#closeStatus').click(function () {
        changeStatus('close');
    });
    $('#resetPwd').click(function () {
        if(confirm("是否确认重置密码")) {
            var data = getCheckedIds();
            $.ajax({
                type: 'POST',
                url: '/resetPassword',
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function (data) {
                    refresh($('#role').val());
                    // alert('更新成功');
                    layer.tips('密码已重置为000000', '#add');
                    $('#selCkb').prop('checked',false);
                },
                error: function (XMLHttpRequest, status, errorThrown) {
                    alert(status + " " + errorThrown);
                }
            });
        }
    });
    $('#selCkb').click(function () {
        $('input:checkbox[name="ckb"]').prop('checked',this.checked);
    });

    $('#create').removeAttr('disabled');
    $('#selCkb').removeAttr('disabled');
    $('#delete').removeAttr('disabled');
    $('#openStatus').removeAttr('disabled');
    $('#closeStatus').removeAttr('disabled');
    $('#resetPwd').removeAttr('disabled');
});



function changeStatus(status) {
    var data = new Object();
    var ids = getCheckedIds();
    data.id = ids;
    data.status = status;
    if(ids.length == 0) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: '/changeStatus',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success:function (data) {
            refresh($('#role').val());
            // alert('更新成功');
            layer.tips('更新成功','#add');
            $('#selCkb').prop('checked',false);
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            alert(status + " " + errorThrown);
        }
    });
}

function getCheckedIds() {
    var data = new Array();
    $('input:checkbox[name="ckb"]:checked').each(function () {
        $tr = $(this).parent().parent();
        var id = $tr.find('input').eq(1).val();
        data.push(id);
    });
    return data;
}



function refresh(role) {
    allAdmin = innerAdmin = outerAdmin = '';
    show(role);
}

function initToken() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

function openadd() {
    $("#myModalLabel").text("创建用户");
    $("#addModal").modal("show");
    $('input').val("");
    $('input[name="roleType"]').eq(0).val("ROLE_INNER");
    $('input[name="roleType"]').eq(1).val("ROLE_OUTER");
    $('input[name="status"]').eq(0).val('开启');
    $('input[name="status"]').eq(1).val('关闭');
    $('#Password').prop('placeholder','如不输入则默认密码000000');
    radioItem('roleType','ROLE_INNER');
    radioItem('status','关闭');
    $('#college option:first').prop('selected',true);
    $('#inner').hide();
    $("#add").show();
    $("#edt").hide();
}

function openedt() {
    $("#myModalLabel").text("修改信息");
    $("#addModal").modal("show");
    $('input').val("");
    $('input[name="roleType"]').eq(0).val("ROLE_INNER");
    $('input[name="roleType"]').eq(1).val("ROLE_OUTER");
    $('input[name="status"]').eq(0).val('开启');
    $('input[name="status"]').eq(1).val('关闭');
    $('#PasswordGroup').hide();
    $("#edt").show();
    $("#add").hide();
}


function show(value) {
    $.get('/getAdmins/'+value,function (data) {
        var len = data.length;
        var html = "";
        if(value == 'all') {
            userNames = new Array();
        }
        for(var i=0;i<len;i++) {
            var user = data[i];
            var status = user.status==1 ? "开启":"关闭";
            var roleType = "";
            var id = user.id;
            if(user.roleType == 'ROLE_ADMIN') {
                admin.id = id;
                admin.username = user.username;
                admin.roleType = user.roleType;
                admin.status = user.status;
                roleType = '管理员';
            } else if(user.roleType == 'ROLE_INNER') {
                roleType = '内部人员';
            } else {
                roleType = '外部人员';
            }
            var tr = "<tr>";
            // tr += '<td>' + user.username  + '</td>';
            var tmp = '<td><a href="javascript:void(0)" onclick="edit(this)">' + user.username + '</a></td>';
            if(user.roleType == 'ROLE_ADMIN') {
                tmp = '<td>' + user.username + '</td>';
            }
            tr += tmp;
            tr += '<td>' + user.college + '</td>';
            tr += '<td>' + roleType + '</td>';
            tr += '<td>' + status + '</td>';
            if(user.roleType != 'ROLE_ADMIN') {
                tr += '<td>' + '<input type="checkbox" name="ckb"/> ' + '</td>';
            }
            tr += '<input type="hidden" name="userId" value="'+id+'"/>';
            tr += '</tr>';
            html += tr;
            if(value == 'all') {
                userNames.push(user.username);
            }
            // console.log(html);
        }
        if(value=='ROLE_INNER'){
            innerAdmin = html;
        } else if(value == 'ROLE_OUTER'){
            outerAdmin = html;
        } else {
            allAdmin = html;
        }
        $('#tbody').html("");
        $('#tbody').append(html);
    })
}

function edit(obj) {
    var par = $(obj).parent().parent();
    var username = $(obj).text();
    var college = par.find('td').eq(1).text();
    var roleType = par.find('td').eq(2).text();
    var roleTypeVal = roleType == '内部人员'?'ROLE_INNER':'ROLE_OUTER';
    var status = par.find('td').eq(3).text();
    var id = par.find('input[name="userId"]').val();
    console.log(username + " " + college + " " + roleType + " " + status);
    openedt();
    if(roleType == '内部人员') {
        $('#inner').hide();
    } else {
        $('#inner').show();
    }
    $('#Name').val(username);
    $('input[name="id"]').val(id);
    selectItem('college',college);
    radioItem('status',status);
    radioItem('roleType',roleTypeVal);
}

function selectItem(selId,itemName) {
    // alert(itemName);
    $('#'+selId + ' option').each(function () {
        // console.log($(this).text());
        if($(this).text() == itemName) {
            $(this).prop('selected',true);
            return;
        }
    })
}

function radioItem(radioName,value) {
    console.log('value:'+value);
    $('input:radio[name="'+ radioName + '"]').each(function () {
        console.log($(this));
        if($(this).val() == value) {
            $(this).prop('checked',true);
            return;
        }
    })
}

var allAdmin = "";
var innerAdmin = "";
var outerAdmin = "";
var userNames = {};
var admin = new Object();
