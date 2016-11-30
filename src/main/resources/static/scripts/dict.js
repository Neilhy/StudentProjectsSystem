$(document).ready(function () {
    initToken();
});

function initToken() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

function insertRow() {
    var trs = $('#tab tr');
    var idx = trs.length;
    var trhtml = [
        '<tr>',
            '<td>',  idx ,'</td>',
            '<td>', '<input type="text" name="itemName"/>', '</td>',
            '<td>', '<input type="button" class="btn btn-warning" onclick="deleteRow(' + idx + ')" value="删除"/>', '</td>',
        '</tr>'
    ].join('');
    $('#tbody').append(trhtml);
}

function deleteRow(idx) {
    var tr = $('#tbody').find('tr').eq(idx-1);
    var next = tr.nextAll();
    if(next.size() > 0) {
        next.each(function () {
            var a = $(this).find('td').eq(0).text() - 1;
            $(this).find('td').eq(0).text(a);
            $(this).find('input').eq(1).attr('onclick','deleteRow('+a+')');
        })
    }
    tr.remove();
}

function clearRows() {
    var $tr = $('#tbody').find('tr').eq(0);
    $tr.find('input').eq(0).val("");
    var $next = $tr.nextAll();
    if($next.size() > 0) {
        $next.each(function () {
            $(this).remove();
        })
    }
}

function exportExcel(url) {
    // openWindow('/excel','700','400');
    window.open(url,'newWindow','height=400,width=700');
}
/**
 * flag用于区分更新操作和添加操作
 * @returns {Object}
 */
function getData(){
    var data = new Object();
    var rows = new Array();
    var idx = 0;
    var type = $('#dataType').val();
    var flag = "update";
    if(type == "新建") {
        type = $('#newType').val();
        flag = "add";
    }
    $tbody = $('#tbody').find('tr');
    $('#tbody').find('tr').each(function (){
        var row = new Object();
        row.code = $(this).find('td').eq(0).text();
        row.itemName = $(this).find('input').val();
        row.keyword = type;
        row.date = new Date();
        rows[idx] = row;
        idx += 1;
    });
    data.dictList = rows;
    data.flag = flag;
    // alert(JSON.stringify(data));
    return data;
}

function check() {
    //判断是否存在重复的数据类型
    var types = $('#dataType').find('option');
    var nowType = $('input[name=newType]').val();
    var flag = true;
    types.each(function () {
        if(nowType == $(this).text()) {
            flag = false;
            return false;
        }
    });
    if(!flag) {
        alert('该数据类型已存在');
        return false;
    }
    //判断是否存在重复的数据项
    var items = $('#tbody').find('input[name=itemName]');
    var arr = new Array();
    items.each(function () {
        // alert($(this).val());
        arr.push($(this).val());
    });
    if(arr.length != $.unique(arr).length) {
        alert('请不要重复输入相同的数据项');
        return false;
    }
    return true;
}

function saveEdit() {
    if(!check())
        return;
    $.ajax({
        type: "POST",
        url: "/addDicts",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(getData()),
        success: function (data) {
            $(document).find('html').html(data);
            initToken();
            getKeywords();
        },
        error:function (XMLHttpRequest,status,errorThrown) {
            alert('error');
            alert(status + " " + errorThrown);
        }
    });

}
/**
 * 当改变数据类型时，改变当前页面的显示
 */
function changeType() {
    var type = $('#dataType').val();
   // var inType = '<input type="text" name="newType" id="newType" placeholder="新的数据类型名称"/>';
    clearRows();
    //var del = '<a class="btn btn-success" id="deleteType" onclick="deleteCurType()">删除此数据类型</a>';
   // $('#deleteType').remove();
    if(type=="新建") {
        //$('.select').prepend(inType);/////////////////////////
       // $('#saveType').show();
        $("#deleteType").hide();
        $('#newType').show();

    } else {
        $('#newType').hide();
       // $('#saveType').hide();
        $("#deleteType").show();
        //$('.select').append(del);
        getDictItemsAndShow(type);
    }
}

/**
 * 获取所有数据类型，并添加到select中去
 */
function getKeywords() {
    $.get('/getKeywords',function (data) {
        // alert(data);
        var html = [
                '<span>数据类型：</span>',
                '<select id="dataType" onchange="changeType()">',
                '<option>新建</option>'].join('');
        var keywords = data;
        for(var i=0;i<keywords.length;i++) {
            html += '<option>' + keywords[i] + '</option>';
        }
        html += '</select> ';
        html += '<input type="text" name="newType" id="newType" placeholder="新的数据类型名称"/>';
        // alert(html);
        $("#deleteType").hide();
        $('.select').prepend(html);////////////////////////////////////////////
    })
}

/**
 * 通过数据类型获取相应的数据项，并显示在表中
 * @param keyword
 */
function getDictItemsAndShow(keyword) {
    var url = "/getDictItems";
    var data = new Object();
    data.keyword = keyword;
    $.ajax({
        type: "POST",
        url: url,
        cache: false,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (data) {
            var items = data;
            if(items.length > 0) {
                for(var i=0;i<items.length;i++) {
                    if(i>0) {
                        insertRow();
                    }
                    var $tr = $('#tbody').find('input[name=itemName]').eq(i);
                    $tr.val(items[i]);
                }
            }
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            alert('error');
            alert(status + " " + status);
        }
    });

}

/**
 * 获取输入数据类型对应的数据项并添加到select中去
 * @param keyword
 */
function setSelectItems(selName,keyword) {
    var url = "/getDictItems";
    var data = new Object();
    data.keyword = keyword;
    $.ajax({
         type: "POST",
         url: url,
         cache: false,
         contentType: "application/json; charset=utf-8",
         data: JSON.stringify(data),
         success: function (data) {
             // alert(data);
             var items = data;
             var html = "";
             for (var i = 0; i < items.length; i++) {
             html += '<option value="' + items[i] + '">' + items[i] + '</option>';
             }
             //alert(html);
             $('#' + selName).append(html);
         },
         error: function (XMLHttpRequest, status, errorThrown) {
         alert('error');
         alert(status + " " + status);
     }
     });
}

function setTextItems(textName,keyword) {
    var url = "/getDictItems";
    var data = new Object();
    data.keyword = keyword;
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
            // alert(html);
            $('#' + textName).val(html);
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            alert('error');
            alert(status + " " + status);
        }
});
}

/**
 * 删除当前数据类型
 */
function deleteCurType() {
    var keyword = $('.select option:selected').text();
    if(confirm('您确定要删除'+keyword+"吗？")) {
        var url = '/deleteKeyword/' + keyword;
        $.get(url, function (data) {
            $(document).find('html').html(data);
            initToken();
            getKeywords();
        })
    }
}



