$(function () {
    initToken();
    $(".content-left,.content-right").height($(window).height() - 130);
    $(".accordion-inner").click(function () {
        $(".active").html($(this).find(".left-body").text());
    });
    username = $('title').text();
});

function initToken() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
}

$(window).resize(function () {
    $(".content-left,.content-right").height($(window).height());
    $(".iframe").height($(window).height());
});

$('#modifyPwd').click(function () {
    $('#ModifyPwdModal').modal('show');
   // alert(username);
    if(username == 'admin') {
        $('#OldPwdGroup').hide();
    } else {
        $('#OldPwdGroup').show();
    }
    $('#OldPassword').val('');
    $('#NewPassword').val('');
});

$('#OldPassword').blur(function () {
    var url = '/checkPwd/' + $('#OldPassword').val();
    $.get(url,function (data) {
        // alert(data);
        if(data == "fail") {
            layer.tips('密码错误','#OldPassword');
            pwdFlag = false;
        } else {
            layer.tips('密码正确','#OldPassword');
            pwdFlag = true;
        }
    })
});

$('#edtPwd').click(function () {
    var data = new Object();
    if(pwdFlag == false && username!='admin') {
        layer.tips('密码错误','#OldPassword');
        return;
    }

    data.password = $('#NewPassword').val();
    //alert(data.password);
    if(data.password == "") {
        layer.tips('密码不能为空','#NewPassword');
        return;
    }
    $.ajax({
        type: "POST",
        url: "/modifyPassword",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function () {
            $("#ModifyPwdModalLabel").modal("hide");
            // alert('haha');
            top.window.location = '/login';
            // return false;
            // location.reload(true);
            // $(document).find('html').html(data);
            // initToken();
        },
        error: function (XMLHttpRequest, status, errorThrown) {
            alert(status + " " + errorThrown);
        }
    })
});

var username = "";
var pwdFlag = false;

/*    $("#testAddProject").click(function(){
 $.ajax({
 type: "POST",
 url: "/addProject",
 contentType: "application/json; charset=utf-8",
 data: JSON.stringify({
 projectName:"华为比赛",
 level:"省级",
 rank:"一等奖",
 captainCollege:"计算机科学与工程学院",
 teacher:"我",
 note:"很好",
 projectDate:"2016-8-20",
 studentList:[
 {registerId:201430560001,studentName:"一号",college:"计算机科学与工程学院",className:"计算机科学与工程1班",grade:2014,captainOrNot:1},
 {registerId:201430560002,studentName:"二号",college:"计算机科学与工程学院",className:"计算机科学与工程2班",grade:2014},
 {registerId:201430560003,studentName:"三号",college:"计算机科学与工程学院",className:"网络工程",grade:2014},
 {registerId:201430560004,studentName:"四号",college:"计算机科学与工程学院",className:"信息安全",grade:2014},
 {registerId:201430560005,studentName:"五号",college:"计算机科学与工程学院",className:"计算机创新班",grade:2014},
 {registerId:201430560006,studentName:"六号",college:"计算机科学与工程学院",className:"计算机联合班",grade:2014},
 {registerId:201430560007,studentName:"七号",college:"计算机科学与工程学院",className:"网络工程",grade:2014}
 ]
 }),
 dataType:"json",
 success: function (data,status) {
 alert(JSON.parse(data)+"   "+status);
 $("#addProject").val(JSON.parse(data));
 }
 });
 $.ajax({
 type: "POST",
 url: "/addProject",
 contentType: "application/json; charset=utf-8",
 data: JSON.stringify({
 projectName:"三星比赛",
 level:"市级",
 rank:"二等奖",
 captainCollege:"建筑学院",
 teacher:"我",
 note:"很好",
 projectDate:"2016-8-21",
 studentList:[
 {registerId:201430560008,studentName:"一号",college:"建筑学院",className:"建筑1班",grade:2014,captainOrNot:1},
 {registerId:201430560009,studentName:"二号",college:"建筑学院",className:"建筑2班",grade:2014},
 {registerId:201430560010,studentName:"三号",college:"建筑学院",className:"建筑3班",grade:2014},
 {registerId:201430560011,studentName:"四号",college:"建筑学院",className:"建筑4班",grade:2014},
 {registerId:201430560012,studentName:"五号",college:"建筑学院",className:"建筑5班",grade:2014},
 {registerId:201430560013,studentName:"六号",college:"建筑学院",className:"建筑6班",grade:2014},
 {registerId:201430560014,studentName:"七号",college:"建筑学院",className:"建筑1班",grade:2014}
 ]
 }),
 dataType:"json",
 success: function (data,status) {
 alert(JSON.parse(data)+"   "+status);
 $("#addProject").val(JSON.parse(data));
 }
 });
 //                $.post("/addProject",
 //                        {
 //                            projectName:"三星比赛",
 //                            level:"省级",
 //                            rank:"一等奖",
 //                            captainCollege:"计算机科学与工程学院",
 //                            teacher:"我",
 //                            note:"很好",
 //                            projectDate:"2016-8-20",
 //                            studentList:[
 //                                {registerId:201430560008,studentName:"一号",college:"计算机科学与工程学院",className:"计算机科学与工程1班",grade:2014,captainOrNot:1,projectList:null},
 //                                {registerId:201430560009,studentName:"二号",college:"计算机科学与工程学院",className:"计算机科学与工程2班",grade:2014,captainOrNot:0,projectList:null},
 //                                {registerId:201430560010,studentName:"三号",college:"计算机科学与工程学院",className:"网络工程",grade:2014,captainOrNot:0,projectList:null},
 //                                {registerId:201430560011,studentName:"四号",college:"计算机科学与工程学院",className:"信息安全",grade:2014,captainOrNot:0,projectList:null},
 //                                {registerId:201430560012,studentName:"五号",college:"计算机科学与工程学院",className:"计算机创新班",grade:2014,captainOrNot:0,projectList:null},
 //                                {registerId:201430560013,studentName:"六号",college:"计算机科学与工程学院",className:"计算机联合班",grade:2014,captainOrNot:0,projectList:null},
 //                                {registerId:201430560014,studentName:"七号",college:"计算机科学与工程学院",className:"网络工程",grade:2014,captainOrNot:0,projectList:null}
 //                            ]
 //                        },function (data,status) {
 //                            alert(status);
 //                            $("#addProject").html(data);
 //                        },"json");
 });
 $("#testGetProject").click(function() {
 var url='/getProjects/'+$('#projectId1').val()+'/'+$('#projectId2').val();
 $.get(url, function (data, status) {
 alert(JSON.parse(data)+"   "+status);
 $("#getProjects").val(JSON.parse(data));
 });
 });
 $("#testAddStudents").click(function() {
 $.ajax({
 type: "POST",
 url: "/addStudents",
 contentType: "application/json; charset=utf-8",
 data: JSON.stringify({
 projectId:parseInt($('#projectId1').val()),
 studentList:[
 {registerId:parseInt($('#projectId2').val()),studentName:"一号",college:"建筑学院",className:"建筑1班",grade:2014,captainOrNot:1}
 ]
 }),
 dataType: "json",
 success: function (data, status) {
 alert(JSON.parse(data) + "   " + status);
 $("#deleteProjects").val(JSON.parse(data));
 }
 });
 });
 $("#testDeleteProjects").click(function() {
 $.ajax({
 type: "POST",
 url: "/deleteProjects",
 contentType: "application/json; charset=utf-8",
 data: JSON.stringify(
 //                        [parseInt($('#projectId1').val()), parseInt($('#projectId2').val())]
 [parseInt($('#projectId1').val())]
 ),
 dataType: "json",
 success: function (data, status) {
 alert(JSON.parse(data) + "   " + status);
 $("#deleteProjects").val(JSON.parse(data));
 }
 });
 });
 $('#testDict').click(function () {
 $('.iframe').attr('src','/dict');
 })
 */