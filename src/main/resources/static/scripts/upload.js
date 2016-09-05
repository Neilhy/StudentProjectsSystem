$(function () {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
    $('#competitionPhoto').fileupload({
        dataType: 'json',
		autoUpload:"true",
		singleFileUploads:"true",
		done: function (e, data) {
			var fileName = data.result.fileName;
			$('#upload-img').css('display','block');
			$('#upload-img').attr('src','/getPic/'+fileName);
			$('#browseText').val(fileName);
        },

		fail: function (e,data) {
        	var err = data._response.jqXHR.responseText;
			console.log(err);
			$('#responseTxt').val(err);
		},

        progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
			// alert(progress);
	        $('#progress .bar').css(
	            'width',
	            progress + '%'
	        );
			$('.progress .bar').text(progress + '%');
   		}
    }).on("fileuploadadd",function () {
    	var progress = 0;
		// alert(progress);
		$('#progress .bar').css(
			'width',
			progress + '%'
		);
		$('.progress .bar').text(progress + '%');
		$('#responseTxt').val("");
		// alert('begin');
	});
});