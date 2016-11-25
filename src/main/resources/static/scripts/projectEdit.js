/**
 * Created by Administrator on 2016/11/23.
 */

$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    var url = '/getProject/'+document.URL.split('?')[1].split('=')[1];
    $.get(url,function (data) {
        var thisProject = data,
            filePath=thisProject.filePath,
            msgForbid=thisProject.msgForbid,
            projectName=thisProject.projectName,
            projectDate=thisProject.projectDate,
            level=thisProject.level,
            rank=thisProject.rank,
            studentList=thisProject.studentList,
            captainCollege=thisProject.captainCollege,
            teacher=thisProject.teacher,
            note=thisProject.note;
        ID = thisProject.id;
        //photoStatus=thisProject.photoStatus
        //state=thisProject.state
        $("#competitionName").val(projectName);
        $("#competitionRank").val(rank);
        $("#competitionTime").val(projectDate);
        $("#teacher").val(teacher);
        $("#competitionPrize").val(level);
        $("#note").val(note);
        //每一行都一样，用模板的方法
        var tpl="<tr>"+
            "<td>{{registerId}}</td>"+
            "<td>{{Name}}</td>"+
            "<td>{{college}}</td>"+
            "<td>{{className}}</td>"+
            "<td>{{duty}}</td>"+
            "<td><input type='button' class='btn btn-warning' value='删除' onclick='removetrNode($(this))'></td>"+
            "</tr>";
        var students=[];
        if(studentList.length>1){//团队赛
            $(":radio[name=competitionMethod][value=team]").attr("checked","true");
            for(var i=0;i<studentList.length;i++){
                var tempTpl=tpl.replace("{{Name}}",studentList[i].studentName)
                    .replace("{{registerId}}",studentList[i].registerId)
                    .replace("{{className}}",studentList[i].className)
                    .replace("{{college}}",studentList[i].college);
                if(studentList[i].captainOrNot==1){
                    tempTpl= tempTpl.replace("{{duty}}","队长");
                }else{
                    tempTpl=tempTpl.replace("{{duty}}","");
                }
                students.push(tempTpl);
            }
            $('#individual').hide();
            $('#table').show();
            $("#teamer-info").html(''+students.join(''));
        }else{
            var student=studentList[0];
            $(":radio[name=competitionMethod][value=person]").attr("checked","true");
            $("#registerId").val(student.registerId);
            $("#Name").val(student.studentName);
            selectItem('college',student.college);
            $("#className").val(student.className);
        }
        $('#photoGroup').hide();
    });
    //填充值


//下面和projectAdd一样
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    // setCollege();
    setTextItems('collegesText','学院');
    setSelectItems('college','学院');
    setSelectItems('competitionRank','竞赛等级');
    setSelectItems('competitionPrize','所获奖项');
    var node1="<div class='teamerInfo'>"+
        "<input type='text' class='form-control' id='studentId' placeholder='学号'/>"+
        "<input type='text' class='form-control' id='studentName'  placeholder='姓名'/>"+
        "<select class='form-control' id='form-control'>";

    var node2= "</select>"+
        "<input type='text' class='form-control' id='studentClass' placeholder='班级'/>  "+
        "<input type='checkbox' id='captainCkb'/> 是否队长"+
        "<input type='button' value='添加' class='btn btn-success' onclick='addPartnerToTable($(this))'/> "+
        "<input type='button' id='cancle' value='取消' class='btn btn-default' onclick='removeNode($(this))'/>"+
        "</div>";


    $("#saveEdit").click(function () {
        if ($("#competitionName").val() == "") {
            layer.tips('不能为空', '#competitionName');
            $(window).scrollTop($('#competitionName').offset());
            return;
        }

        if ($("#competitionTime").val() == "") {
            layer.tips('不能为空', '#competitionTime');
            $(window).scrollTop($('#competitionTime').offset());
            return;
        }
        if($('input:radio[name="competitionMethod"]:checked').val() == 'person') {
            if ($("#registerId").val() == "") {
                layer.tips('不能为空', '#registerId');
                $(window).scrollTop($('#registerId').offset());
                return;
            }
            if ($("#Name").val() == "") {
                layer.tips('不能为空', '#Name');
                $(window).scrollTop($('#Name').offset());
                return;
            }
        } else {
            var cnt = 0;
            teamerInfo.children().each(function () {
                var captain = $(this).find('td').eq(4).text();
                if(captain == '队长') {
                    cnt++;
                }
            });
            if(cnt!=1) {
                layer.tips('必须有且只有一个队长', '#competitionMethod');
                $(window).scrollTop($('#competitionMethod').offset());
                return;
            }
        }
        /*       if ($('input[name="competitionMethod"]:checked').val() == "") {
         layer.tips('不能为空', '#competitionMethod');
         return;
         }*/

        // alert($("#browseText").val());
        var studentList = new Array();
        var captainCollege = "";
        if($('input:radio[name="competitionMethod"]:checked').val() == 'person') {
            var student = new Object();
            student.registerId = $("#registerId").val();
            student.studentName = $("#Name").val();
            student.college = $("#college").val();
            student.className = $('#className').val();
            student.grade = student.registerId.substr(0, 4);
            student.captainOrNot = 1;            //1人默认队长
            captainCollege = student.college;
            studentList.push(student);
        } else {
            teamerInfo.children().each(function () {
                //alert($(this).html());
                var student = new Object();
                student.registerId = $(this).find('td').eq(0).text();
                student.studentName = $(this).find('td').eq(1).text();
                student.college = $(this).find('td').eq(2).text();
                student.grade = student.registerId.substr(0, 4);
                student.className = $(this).find('td').eq(3).text();
                var captain = $(this).find('td').eq(4).text();
                if(captain == '队长') {
                    student.captainOrNot = 1;
                    captainCollege = student.college;
                } else {
                    student.captainOrNot = 0;
                }
                studentList.push(student);
            });
        }
        var filePath = '';
        var photoStatus = '';
        // if($('#browseText').val()=='点击上传文件') {
        //     filePath = '';
        //     photoStatus = '未上传';
        // } else {
        //     filePath = $('#browseText').val();
        //     photoStatus = '已上传';
        // }
        var reUpload = $('input:radio[name="reUpload"]').val();
        if(reUpload == 'yes' && $('#browseText').val()!='点击上传文件') {
            // filePath = $('#browseText').val();
            photoStatus = '已上传';
        }
        var project = {
            id:ID,
            projectName: $("#competitionName").val(),
            level:$("#competitionRank").val(),
            projectDate:$("#competitionTime").val(),
            teacher:$("#teacher").val(),
            rank:$("#competitionPrize").val(),
            photoStatus:photoStatus,
            note:$("#note").val(),
            captainCollege:captainCollege,
            studentList:studentList,
            status:'未审核',
        };
        $.get('/savePic',function (data) {
            project.filePath = data;
            $.ajax({
                url: "/modifyProject",
                // timeout: 300000,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                type: "post",
                data: JSON.stringify(project),
                success: function (data) {
                    // alert(data);
                    window.location.reload();
                    //load(curr);
                },
                error:function (XMLHttpRequest,status,errorThrown) {
                    alert('error');
                    alert(status + " " + errorThrown);
                }
            });
        });
    });

    $('input:radio[name="competitionMethod"]').change( function(){
        $(partnerAddNode).toggle();
        $("#table").toggle();
        // alert($(this).val());
        if($(this).val()=='team') {
            $('#individual').hide();
        } else {
            $('#individual').show();
        }
    });

    $('input:radio[name="reUpload"]').change(function(){
        if($(this).val()=='yes') {
            $('#photoGroup').show();
        } else {
            $('#photoGroup').hide();
        }
    });

    $("#addPartner").click(function () {
        if(addFlag==false) {
            // $(node1+'<option>'+college+'</option>'+node2).appendTo(partnerAddNode);
            $(node1+$('#collegesText').val() +node2).appendTo(partnerAddNode);
            addFlag = true;
        }
    });

});
var partnerAddNode=$("#partnerAdd");
var teamerInfo=$("#teamer-info");
var addFlag = false;
var college = "";
var ID;
function selectItem(selId,itemName) {
    $('#'+selId + ' option').each(function () {
        if($(this).text() == itemName) {
            $(this).attr('selected',true);
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

function setCollege() {
    $.get('/getCollege',function (data) {
        college = data;
        $('#college').append('<option>'+college+'</option>')
    })
}

//把队友信息添加至表里面
function addPartnerToTable(ele){
    var $children=ele.parent().children();
    var id=$children[0].value,
        name=$children[1].value,
        colleage=$children[2].value,
        className=$children[3].value,
        isCaptin=$children[4].checked;
    if(id=="") {layer.tips('不能为空',$children[0]);return;}
    if(name=="") {layer.tips('不能为空',$children[1]);return;}
    if(className=="") {layer.tips('不能为空',$children[3]);return;}
    var trNode="<tr>"+
        "<td>"+id+"</td>"+
        "<td>"+name+"</td>"+
        "<td>"+colleage+"</td>"+
        "<td>"+className+"</td>";
    if(isCaptin==true)trNode+="<td>队长</td>";
    else trNode+="<td>  </td>";
    trNode+="<td><input type='button' class='btn btn-warning' value='删除' onclick='removetrNode($(this))'></td></tr>";
    $(trNode).appendTo(teamerInfo);
    $("#table").show();
    $('#studentId').val('');
    $('#studentName').val('');
    $('#studentClass').val('');
    $('#captainCkb').prop('checked','');
}
//去除 添加队友 的节点
function removeNode(ele) {
    var $x=ele.parent();
    $x.remove();

    addFlag = false;
}
//删除表里面的ele的所在行
function removetrNode(ele) {
    var $x=ele.parent().parent();
    $x.remove();
}

