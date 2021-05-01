
$(document).ready(function() {
	$.ajax({
		url : './servlets/UserManage',
		type : 'GET',
		dataType : 'json',
		data:{
			service:0
		}
	}).done(function(data) {
		console.log(data.uid.trim()+", " + data.role.trim())
		if(data.uid.trim() == "" || data.role.trim() == ""){
			console.log("please login");
			/*window.location.href="index.html"*/
		}else{
			if(data.role.trim() == 'MANAGER'){
				$('nav li:nth-last-child(2)').removeClass('hidden');
			}
			else if(data.role.trim() == 'H_MANAGER')
			{
				console.log("H_MANAGER");
				$('nav li:nth-last-child(2)').removeClass('hidden');
				$('#examineHMR').removeClass('hidden');
			}
			else
				{
				console.log("USER");
				}
		}
	}).fail(function() {
		console.log("accessing usermanager error");
		/*window.location.href="index.html"*/
	});
});


function showRevokeTable(data){
	$('#ERTable tbody').html('');
	var len = data.length;
		for(var i=0;i<len;i++){
			var $tr = $('<tr></tr>');
		$tr.append("<th scope='row'>" + i + "</th>");
		$tr.append("<td>"+ data[i].date +"</td>");
		$tr.append("<td>"+ data[i].user +"</td>");
		$tr.append("<td>"+ data[i].room +"</td>");
		$tr.append("<td>"+ data[i].startTime +"</td>");
		$tr.append("<td>"+ data[i].endTime +"</td>");
		$tr.append("<td>"+ data[i].actualUser +"</td>");
		$tr.append("<td>"+ data[i].phone +"</td>");
		var $a = $('<a href="#" ></a>').attr("id",data[i].rowId).text('撤销');
		
		(function(rowid){
			$a.click(function(event){
			$.ajax({
				url : './servlets/UserManage',
				type : 'GET',
				dataType : 'json',
				data:{
					service:3,
					rowID:rowid
				}
			}).done(function(result) {
				if(result.revokeState == 1){
					//success.
					$('#'+result.rowID).parents('tr').empty().remove();
					alert("已撤销该会议室预订！");
					$.ajax({
						url : './servlets/UserManage',
						type : 'GET',
						dataType : 'json',
						data:{
							service:1
						}
					}).done(function(data) {
						showRevokeTable(data.records);
					}).fail(function() {
						console.log("error occurs while revoking booked rooms");
					});
				}
			}).fail(function() {
				alert("撤销失败，请稍后再试！");
				console.log("error");
			});
		});
		
		})($a.attr("id"));
		
		
		var $td = $('<td></td>').append($a);
		$tr.append($td);
		$('#result table:first tbody').append($tr);
		}
		$('#result table:last').addClass('hidden');
		$('#result table:first').removeClass('hidden');
}


function showExamineTable(data){
	$('#ERTable tbody').html('');
	var len = data.length;
		for(var i=0;i<len;i++){
			var $tr = $('<tr></tr>');
		$tr.append("<th scope='row'>" + i + "</th>");
		$tr.append("<td>"+ data[i].date +"</td>");
		$tr.append("<td>"+ data[i].user +"</td>");
		$tr.append("<td>"+ data[i].room +"</td>");
		$tr.append("<td>"+ data[i].startTime +"</td>");
		$tr.append("<td>"+ data[i].endTime +"</td>");
		$tr.append("<td>"+ data[i].actualUser +"</td>");
		$tr.append("<td>"+ data[i].phone +"</td>");
		
		var $a = $('<a href="#" ></a>').attr("id",data[i].rowId).text('批准');
		var $b = $('<a href="#" ></a>').attr("id",data[i].rowId).text('拒绝');
		
		if(data[i].state == "Y")
			{
				$a.text('已批准');
				$a.removeAttr('href');
				$a.removeAttr('onclick');
				$b.addClass('hidden');
			}
		
		(function(rowid){
			$a.click(function(event){
			$.ajax({
				url : './servlets/UserManage',
				type : 'GET',
				dataType : 'json',
				data:{
					service:5,
					rowID:rowid,
					act:1
				}
			}).done(function(result) {
				if(result.passState == 1){
					//success.
					console.log("pass");
					$a.text('已批准');
					$a.removeAttr('href');
					$a.removeAttr('onclick');
					$b.addClass('hidden');
					alert("已批准该会议室预订！");
					$.ajax({
						url : './servlets/UserManage',
						type : 'GET',
						dataType : 'json',
						data:{
							service:4
						}
					}).done(function(data) {
						showExamineTable(data.records);
					}).fail(function() {
						console.log("error occurs while examining booked rooms");
					});
					//$('#'+result.rowID).parents('tr').empty().remove();
				}
			}).fail(function() {
				alert("批准失败，请稍后再试！");
				console.log("pass error");
			});
		});
		
		})($a.attr("id"));
		
		(function(rowid){
			$b.click(function(event){
			$.ajax({
				url : './servlets/UserManage',
				type : 'GET',
				dataType : 'json',
				data:{
					service:5,
					rowID:rowid,
					act:0
				}
			}).done(function(result) {
				if(result.rejectState == 1){
					//success.
					console.log("reject");
					$('#'+result.rowID).parents('tr').empty().remove();
					alert("已拒绝该会议室预订！");
					$.ajax({
						url : './servlets/UserManage',
						type : 'GET',
						dataType : 'json',
						data:{
							service:4
						}
					}).done(function(data) {
						showExamineTable(data.records);
					}).fail(function() {
						console.log("error occurs while examining booked rooms");
					});
				}
			}).fail(function() {
				alert("拒绝失败，请稍后再试！");
				console.log("reject error");
			});
		});
		
		})($b.attr("id"));
		
		
		var $td = $('<td></td>').append($('<p></p>').append($a))
		$tr.append($td.append($('<p></p>').append($b)));
		
		$('#result table:first tbody').append($tr);
		}
		$('#result table:last').addClass('hidden');
		$('#result table:first').removeClass('hidden');
}


//revoke the booked room.
$('#revokeMR').click(function(event){
	$('#editPersonInfo').addClass('hidden');
	$.ajax({
		url : './servlets/UserManage',
		type : 'GET',
		dataType : 'json',
		data:{
			service:1
		}
	}).done(function(data) {
		debugger;
		showRevokeTable(data.records);
	}).fail(function() {
		console.log("error occurs while revoking booked rooms");
	});
});

//Browse the history of booking.
$('#historyMR').click(function(event){
	$('#editPersonInfo').addClass('hidden');
	$.ajax({
		url : './servlets/UserManage',
		type : 'GET',
		dataType : 'json',
		data:{
			service:2
		}
	}).done(function(data) {
		debugger;
		$('#result table:last tbody td').empty().remove();
		$('#result table:last tbody tr').empty().remove();
		var len = data.records.length;
		for(var i=0;i<len;i++){
			var $tr = $('<tr></tr>');
		$tr.append("<th scope='row'>" + i + "</th>");
		$tr.append("<td>"+ data.records[i].date +"</td>");
		$tr.append("<td>"+ data.records[i].user +"</td>");
		$tr.append("<td>"+ data.records[i].room +"</td>");
		$tr.append("<td>"+ data.records[i].startTime +"</td>");
		$tr.append("<td>"+ data.records[i].endTime +"</td>");
		$tr.append("<td>"+ data.records[i].actualUser +"</td>");
		$tr.append("<td>"+ data.records[i].phone +"</td>");
		
		$('#result table:last tbody').append($tr);
		}
		$('#result table:first').addClass('hidden');
		$('#result table:last').removeClass('hidden');
	}).fail(function() {
		console.log("error");
	});
});


//examine the 301 room.
$('#examineHMR').click(function(event){
	$('#editPersonInfo').addClass('hidden');
	$.ajax({
		url : './servlets/UserManage',
		type : 'GET',
		dataType : 'json',
		data:{
			service:4
		}
	}).done(function(data) {
		showExamineTable(data.records);
	}).fail(function() {
		console.log("error occurs while examining booked rooms");
	});
});



$('#editUserInfo').click(function(){
	$('#result table:first').addClass('hidden');
	$('#result table:last').addClass('hidden');
	//Get personal infomation from server.
	$.ajax({
				url : './servlets/userInfo',
				type : 'GET',
				dataType : 'json',
				data:{
					service:0
				}
			}).done(function(result) {
				$('#userID').val(result.uid);
				$('#userName').val(result.name);
				$('#role').val(result.role);
				$('#phone').val(result.phone);
				$('#editPersonInfo').removeClass('hidden');
			}).fail(function() {
				console.log("error");
			});
	
});

$('#editPersonInfo form input[type="checkbox"]').change(function(){
	if($(this).is(":checked")){
		//show the controls of setting password.
		$('#newPwd1Con').removeClass('hidden');
		$('#newPwd2Con').removeClass('hidden');
	}else{
		//hide the passowrd setting controls.
		$('#newPwd1Con').addClass('hidden');
		$('#newPwd2Con').addClass('hidden');
	}
});

//Summit to modify the personal infomation.
$('#btnModify').click(function(){
	var oldPwd = $('#oldPwd').val().trim();
	if(oldPwd === ""){
		$('#infoLabel').text("旧密码不能为空！");
		$('#infoLabelCon').removeClass("hidden");
		return ;
	}
	
	//the new password does not match with the repeated one.
	if( $('#chkChgPwd').is(':checked') && $('#newPwd1').val().trim() !== $('#newPwd2').val().trim()){
		$('#infoLabel').text("新密码两次输入的不一样！");
		$('#infoLabelCon').removeClass("hidden");
		return ;
	}
	var para = {};
	if($('#chkChgPwd').is(':checked')){
		//modify the password.
		para={
			service:2,
			phone:$('#phone').val(),
			oldPassword:$('#oldPwd').val(),
			newPassword:$('#newPwd1').val()
		}
	}else{
		//only update the phone number.
		para={
			service:1,
			phone:$('#phone').val(),
			oldPassword:$('#oldPwd').val()
		}
	}
	$.ajax({
				url : './servlets/userInfo',
				type : 'GET',
				dataType : 'json',
				data:para
			}).done(function(result) {
				if(result.state == 1){
					$('#infoLabel').text("恭喜，信息修改成功！");
					$('#infoLabelCon').removeClass("hidden");
				}else if(result.state == 0){
					$('#infoLabel').text("抱歉，信息修改失败！");
					$('#infoLabelCon').removeClass("hidden");
				}else if(result.state == 2){
					$('#infoLabel').text("旧密码不正确！");
					$('#infoLabelCon').removeClass("hidden");
				}
			}).fail(function() {
				console.log("error");
			});
	
});

$('#btnCancel').click(function(){
	
});
$('#logout').click(function(event){
	  $.ajax({
			url : './logout',
			type : 'GET',
			dataType : 'json'
		}).done(function(data) {
			/*window.location.href="../../templates/index.html"*/
		}).fail(function() {
			console.log("error");
		});
});