/**
 * Created by Administrator on 2016/9/4.
 */
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    setCollege();
    setSelectItems('competitionRank','竞赛等级');
    setSelectItems('competitionPrize','所获奖项');

    var node1="<div class='teamerInfo'>"+
        "<input type='text' class='form-control' id='studentId' placeholder='学号'/>"+
        "<input type='text' class='form-control' id='studentName'  placeholder='姓名'/>"+
        "<select class='form-control' id='form-control'>";
       /* "<option value='机械与汽车工程学院'>机械与汽车工程学院</option>"+
        "<option value='建筑学院'>建筑学院</option>"+
        "<option value='土木与交通学院'>土木与交通学院</option>"+
        "<option value='电子与信息学院'>电子与信息学院</option>"+
        "<option value='材料科学与工程学院'>材料科学与工程学院</option>"+
        "<option value='化学与化工学院'>化学与化工学院</option>"+
        "<option value='轻工科学与工程学院'>轻工科学与工程学院 </option>"+
        "<option value='食品科学与工程学院'>食品科学与工程学院</option>"+
        "<option value='数学学院'>数学学院 </option>"+
        "<option value='物理与光电学院'>物理与光电学院 </option>"+
        "<option value='经济与贸易学院'>经济与贸易学院</option>"+
        "<option value='自动化科学与工程学院'>自动化科学与工程学院</option>"+
        "<option value='计算机科学与工程学院'>计算机科学与工程学院</option>"+
        "<option value='电力学院'>电力学院</option>"+
        "<option value='生物科学与工程学院'>生物科学与工程学院</option>"+
        "<option value='环境与能源学院'>环境与能源学院</option>"+
        "<option value='软件学院'>软件学院</option>"+
        "<option value='工商管理学院（创业教育学院)'>工商管理学院（创业教育学院）</option>"+
        "<option value='公共管理学院'>公共管理学院</option>"+
        "<option value='马克思主义学院'>马克思主义学院</option>"+
        "<option value='外国语学院'>外国语学院</option>"+
        "<option value='法学院（知识产权学院)'>法学院（知识产权学院）</option>"+
        "<option value='新闻与传播学院'>新闻与传播学院</option>"+
        "<option value='艺术学院'>艺术学院</option>"+
        "<option value='体育学院'>体育学院</option>"+
        "<option value='设计学院'>设计学院</option>"+
        "<option value='医学院'>医学院</option>"+
        "<option value='国际教育学院'>国际教育学院</option>"+*/
    var node2= "</select>"+
        "<input type='text' class='form-control' id='studentClass' placeholder='班级'/>  "+
        "<input type='checkbox' id='captainCkb'/> 是否队长"+
        "<input type='button' value='添加' class='btn btn-success' onclick='addPartnerToTable($(this))'/> "+
        "<input type='button' id='cancle' value='取消' class='btn btn-default' onclick='removeNode($(this))'/>"+
        "</div>";


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

    $("#addPartner").click(function () {
        if(addFlag==false) {
            $(node1+'<option>'+college+'</option>'+node2).appendTo(partnerAddNode);
            addFlag = true;
        }
    });

});
var partnerAddNode=$("#partnerAdd");
var teamerInfo=$("#teamer-info");
var addFlag = false;
var college = "";

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
