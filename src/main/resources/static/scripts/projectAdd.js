/**
 * Created by Administrator on 2016/9/4.
 */
$(function () {
    setTextItems('collegesText','学院');
    setSelectItems('college','学院');
    setSelectItems('competitionRank','竞赛等级');
    setSelectItems('competitionPrize','所获奖项');

    var node1="<div class='teamerInfo'>"+
        "<input type='text' class='form-control' placeholder='学号'/>"+
        "<input type='text' class='form-control'  placeholder='姓名'/>"+
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
        "<input type='text' class='form-control'  placeholder='班级'/>  "+
        "<input type='checkbox'/> 是否队长"+
        "<input type='button' value='添加' class='btn btn-success' onclick='addPartnerToTable($(this))'/> "+
        "<input type='button' value='取消' class='btn btn-default' onclick='removeNode($(this))'/>"+
        "</div>";




   $("#add").click(function () {
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

       if ($("#competitionTime").val() == "") {
           layer.tips('不能为空', '#competitionTime');
           return;
       }

       if ($('input[name="competitionMethod"]:checked').val() == "") {
           layer.tips('不能为空', '#competitionMethod');
           return;
       }


       var formdata = {
           flag: "add",
           id: $("#schoolId").val(),
           garde:$("#schoolId").val().substr(0,4),
           name: $("#Name").val(),
           college: $("#college").val(),
           className:$("#className").val(),
           competitionName: $("#competitionName").val(),
           competitionRank:$("#competitionRank").val(),
           competitionTime:$("#competitionTime").val(),
           teacher:$("#teacher").val(),
           competitionMethod:$("#competitionMethod").val(),
           competitionPrize:$("#competitionPrize").val(),
           competitionPhoto:$("#competitionPhoto").val(),
           note:$("#note").val()

       };

       $.ajax({
           url: "/addProject",
           // timeout: 300000,
           contentType: "application/json; charset=utf-8",
           dataType: "json",
           type: "post",
           data: formdata,
           success: function (data) {

               layer.alert(data.msg);
               $("input").val("");
               //load(curr);
           }
       })


   })

    $('input:radio[name="competitionMethod"]').change( function(){
        $(partnerAddNode).toggle();
         $("#table").toggle();
        });

    $("#addPartner").click(function () {
        var colleges = $('#collegesText').val();
        // alert(colleges);
        $(node1+colleges+node2).appendTo(partnerAddNode);
    });
    




});
var partnerAddNode=$("#partnerAdd");
var teamerInfo=$("#teamer-info");

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

}
//删除  添加队友 的节点
function removeNode(ele) {
    var $x=ele.parent();
    $x.remove();

}
//删除表里面的队友的所在行
function removetrNode(ele) {
    var $x=ele.parent().parent();
    $x.remove();
}
