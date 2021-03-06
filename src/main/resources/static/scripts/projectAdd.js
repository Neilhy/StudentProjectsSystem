/**
 * Created by Administrator on 2016/9/4.
 */

$(function () {
    init();
    $('input:radio[name="competitionMethod"]').removeAttr("disabled");
    $("#add").click(function () {
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
       var numFlag = false;
       if($('input:radio[name="competitionMethod"]:checked').val() == 'person') {
           var student = new Object();
           student.registerId = $("#registerId").val();
           student.studentName = $("#Name").val();
           student.college = $("#college").val();
           student.className = $('#className').val();
           student.grade = student.registerId.substr(0, 4);
           student.captainOrNot = 1;            //1人默认队长
           captainCollege = student.college;
           if(/^[\d]+$/.test($('#registerId').val()) == false) {
               numFlag = true;
           }
           studentList.push(student);
       } else {
           teamerInfo.children().each(function () {
              //alert($(this).html());
               var student = new Object();
               student.registerId = $(this).find('td').eq(0).text();
               if(/^[\d]+$/.test(student.registerId) == false) {
                   numFlag = true;
               }
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
       if(numFlag == true) {
           layer.tips('不能包含数字以外的字符', '#registerId');
           $(window).scrollTop($('#registerId').offset());
           return;
       }
       if(captainCollege != college) {
           layer.tips('队长必须是该院的', '#competitionMethod');
           $(window).scrollTop($('#competitionMethod').offset());
           return;
       }
       var filePath = '';
       var photoStatus = '';
       if($('#browseText').val()=='点击上传文件') {
           filePath = '';
           photoStatus = '未上传';

       } else {
           filePath = $('#browseText').val();
           photoStatus = '已上传';
       }
       var project = {
           projectName: $("#competitionName").val(),
           level:$("#competitionRank").val(),
           projectDate:$("#competitionTime").val(),
           teacher:$("#teacher").val(),
           rank:$("#competitionPrize").val(),
           photoStatus:photoStatus,
           note:$("#note").val(),
           captainCollege:captainCollege,
           studentList:studentList
       };
       $.get('/savePic',function (data) {
           project.filePath = data;
           $.ajax({
               url: "/addProject",
               // timeout: 300000,
               contentType: "application/json; charset=utf-8",
               dataType: "json",
               type: "post",
               data: JSON.stringify(project),
               success: function (data) {
                   layer.tips('添加成功', '#competitionName');
                   $(window).scrollTop($('#registerId').offset());
                   $("input").val("");
                   $('#add').val('保存');
                   $('#addPartner').val('添加队友');
                   teamerInfo.html('');
                   removeNode($('#cancle'));
                   $(partnerAddNode).hide();
                   $("#table").hide();
                   $('input:radio[name="competitionMethod"]').eq(0).val('person');
                   $('input:radio[name="competitionMethod"]').eq(1).val('team');
                   $('input:radio[name="competitionMethod"]').eq(0).prop('checked','checked')
                   $('#upload-img').prop('src','');
                   $('#upload-img').css('display','none');
                   $('#browseText').val('点击上传文件');
                   $('#progress .bar').css(
                       'width',
                       0 + '%'
                   );
                   $('#note').text('');
                   //load(curr);
               },
               error:function (XMLHttpRequest,status,errorThrown) {
                   console.log('error');
                   console.log(status + " " + errorThrown);
               }
           });
       });
   });

    $('input:radio[name="competitionMethod"]').change( function(){
        $(partnerAddNode).toggle();
        $("#table").toggle();
       // console.log($(this).val());
        if($(this).val()=='team') {
            $('#individual').hide();
        } else {
            $('#individual').show();
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
var node1;
var node2;
var partnerAddNode=$("#partnerAdd");
var teamerInfo=$("#teamer-info");
var addFlag = false;
var college = "";
function init() {
    initToken();
    setCollege();
    setTextItems('collegesText','学院');
    // setSelectItems('college','学院');
    // $('input:radio[name="competitionMethod"]').attr('disabled','true');
    setSelectItems('competitionRank','竞赛等级');
    setSelectItems('competitionPrize','所获奖项');
    node1="<div class='teamerInfo'>"+
        "<input type='text' class='form-control' id='studentId' placeholder='学号'/>"+
        "<input type='text' class='form-control' id='studentName'  placeholder='姓名'/>"+
        "<select class='form-control' id='form-control'>";
    node2= "</select>"+
        "<input type='text' class='form-control' id='studentClass' placeholder='班级'/>  "+
        "<input type='checkbox' id='captainCkb'/> 是否队长"+
        "<input type='button' value='添加' class='btn btn-success' onclick='addPartnerToTable($(this))'/> "+
        "<input type='button' id='cancle' value='取消' class='btn btn-default' onclick='removeNode($(this))'/>"+
        "</div>";
}

function initToken() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

function setCollege() {
    $.get('/getMyAdmin',function (data) {
        college = data.college;
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
