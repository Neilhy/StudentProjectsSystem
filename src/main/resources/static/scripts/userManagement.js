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

    $('input[name="userType"]').change(function(){
        if($(this).val() == "inner") {
            $('#inner').hide();
        } else {
            $('#inner').show();
        }
    });

    $('#add').click(function () {
        var college = $('#college').val();
        var username = $('#Name').val();
        var password = $('#Password').val();
        var roleType = $('input[name="userType"]:checked').val();
        if(roleType == 'inner') {
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
        data.status = 0;

        $.ajax({
            type: "POST",
            url: "/addAdmin",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function () {
                $("#addModal").modal("hide");
                refresh('all');
                // $(document).find('html').html(data);
                // initToken();
            },
            error: function (XMLHttpRequest, status, errorThrown) {
                alert(status + " " + errorThrown);
            }
        })
    });

    $('#Name').blur(function () {
        var userName = $(this).val();
        if($.inArray(userName,userNames) != -1) {
            layer.tips('已存在', '#Name');
        }
    });

    $('#delete').click(function () {
        var data = new Array();
        $('input:checkbox:checked').each(function () {
            $tr = $(this).parent().parent();
            var username = $tr.find('td').eq(0).text();
            // alert(username);
            data.push(username);
        });
        $.ajax({
            type: 'POST',
            url: '/deleteAdmin',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success:function (data) {
                alert('删除成功');
                refresh($('#role').val());
            },
            error: function (XMLHttpRequest, status, errorThrown) {
                alert(status + " " + errorThrown);
            }
        });
    })
});

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
    $('input[name="userType"]').eq(0).val("inner");
    $('input[name="userType"]').eq(1).val("outer");
    $('#inner').hide();
    $("#add").show();
    $("#edt").hide();
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
            var tr = "<tr>";
            tr += '<td>' + user.username  + '</td>';
            tr += '<td>' + user.college + '</td>';
            tr += '<td>' + user.roleType + '</td>';
            tr += '<td>' + status + '</td>';
            if(user.roleType != 'ROLE_ADMIN') {
                tr += '<td>' + '<input type="checkbox"/> ' + '</td>';
            }
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

var allAdmin = "";
var innerAdmin = "";
var outerAdmin = "";
var userNames = {};

