/**
 * Created by Administrator on 2016/9/4.
 */
$(function () {
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

       if ($("#competitionMethod").val() == "") {
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
          /* url: "/addProject",*/
           timeout: 300000,
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

});


