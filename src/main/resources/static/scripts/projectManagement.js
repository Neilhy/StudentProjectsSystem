
$(function () {
    init();
    initParams();
    showFilter();
    show();
    over();
});

var filter = "未选择";
var filterItem = "无";
var pageSize = 5;
var myStatus = -1;
var myCollege = "";
var first = true;
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
    $('#showPic').hide();
    $('#first').hide();
    $('#prev').hide();
    $.get('/getMyAdmin',function (data) {
        // alert(JSON.stringify(data));
        myStatus = data.status;
        myCollege = data.college;
        if(myCollege=='') {
            myCollege = "无";
        }
    });
}

document.body.onbeforeunload = function () {
    $.cookie('filter',filter,{expired:1});
    $.cookie('filterItem',filterItem,{expired:1});
    $.cookie('pageSize',pageSize,{expired:1});
};

function initParams() {
    filter = getParamFromCookie('filter',filter);
    filterItem = getParamFromCookie('filterItem',filterItem);
    pageSize = getParamFromCookie('pageSize',pageSize);
}

function getParamFromCookie(name,value) {
    if($.cookie(name) == null) {
        return value;
    } else {
        return $.cookie(name);
    }
}

function over() {
    setTotPages();
    $('#add').removeAttr('disabled');
    $('#excel').removeAttr('disabled');
    $('#del').removeAttr('disabled');
    $('#pass').removeAttr('disabled');
    $('#noPass').removeAttr('disabled');
    $('#selCkb').removeAttr('disabled');
}

function showFilter() {
    $('#filter').val(filter);
    $('#filter-list').html('');
    if(filter == '未选择') {
        $('#filter-list').append('<option value="无">无</option>');
        filterItem = '无';
        $('#filter-list').val(filterItem);
        if(first == true) {
            $('#size').val(pageSize);
            first = false;
        }
        return;
    }
    var url = "/getDictItems";
    var data = new Object();
    data.keyword = filter;
    $.ajax({
        type: "POST",
        url: url,
        cache: false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (data) {
            var items = data;
            var html = "";
            for (var i = 0; i < items.length; i++) {
                html += '<option value="' + items[i] + '">' + items[i] + '</option>';
            }
            $('#filter-list').append(html);
            if(first == true) {
                $('#filter-list').val(filterItem);
                $('#size').val(pageSize);
                first = false;
            }
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            console.log('error');
            console.log(status + " " + status);
        }
    });
    // setSelectItems('filter-list',filter);
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
    window.open('/excel/'+myCollege, 'newWindow', 'height=400,width=700');
});

$('#filter').change(function () {
    $('#filter-list').html('');
    filter = $('#filter').val();
    if($('#filter').val() != "未选择") {
        $('#filter-list').append('<option value="未选择">未选择</option>');
    } else {
        $('#filter-list').append('<option value="无">无</option>');
        filterItem = '无';
        $('#tbody').html('');
        show();
    }
    showFilter();
});


$('#filter-list').change(function () {
    filterItem = $(this).val();
    changePage();
});

$('#selCkb').click(function () {
    $('input:checkbox[name="ckb"]').prop('checked',this.checked);
});

$('#size').change(function () {
    pageSize = $(this).val();
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
            console.log(status + " " + errorThrown);
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
    var url = '/getProjects/'+filter+'/'
        +filterItem +'/'+($('#cur').text()-1)+'/'+pageSize;
    $.get(url,function (data) {
        list(data);
    })
}

function changePage() {
    setList();
    setTotPages();
}

function setList() {
    var url = '/getProjects/'+filter+'/'
        +filterItem + '/' + ($('#cur').text()-1)+'/'+pageSize;
    $.get(url,function (data) {
        // console.log(JSON.stringify(data));
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
    var url = 'getTotPages/'+pageSize+'/'+filter+'/'
                 +filterItem;
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
        var size = Number(pageSize);
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
        // console.log(student.id);
    }
}

function showImg(obj) {
    var par = $(obj).parent().parent();
    var filePath = par.find('input').eq(1).val();
    $('#pic').attr('src','/getLocalPic/'+filePath);
    $('#showPic').show();
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
