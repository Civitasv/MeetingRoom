$(document).ready(function () {
    initTableByRoom();
    showDefaultData();
    $('#bookingDate, #selectDate').datepicker({
        todayBtn: "linked",
        language: "zh-CN",
        todayHighlight: true
    });
    $('#btn_select_room').click(function () {
        alert("此功能还在开发中，敬请期待！");
    });
});

function showDefaultData() {
    $.ajax({
        url: './getBookingData',
        type: 'GET',
        dataType: 'json'
    }).done(function (data) {
        $('#selectDate').val(data.currentDate);
        $('#today').html(data.currentDate + "," + data.dayOfWeek);
        /*
         * If any user is returned, show it.
         */
        var records = data.records;
        if (records.length > 0) {
            displayRecordsByRoom(records);
        }

        if (data.userName.trim() != 'undefined' && data.userName.trim() != "" && $('#userLabel').html().trim() == "") {
            var userLabelLink = "<a href=./userManage.html>"
                + data.userName + "</a>";
            $("#userLabel").append(
                userLabelLink).removeClass(
                'hidden');
            $("#btnLogin").addClass('hidden');
            $('#btnLogout').removeClass(
                'hidden');
            $("#loginModal").modal('hide');
        }

    }).fail(function () {
        console.log("error");
    });
}

function initTableByRoom() {
    $('#content table tbody td').empty();
    $('#content table tbody tr').empty().remove();
    for (let i = 1; i < 32; i++) {
        const time = genTimeByRowId(i);
        const $tr = $('<tr></tr>');
        const rowId = genIdByRowId(i);
        $tr.append("<th scope='row'>" + time + "</th>");
        $tr.append("<td id=220" + rowId + "></td>");
        $tr.append("<td id=301" + rowId + "></td>");
        $tr.append("<td id=319" + rowId + "></td>");
        $tr.append("<td id=320" + rowId + "></td>");
        $tr.append("<td id=504" + rowId + "></td>");
        $tr.append("<td id=429" + rowId + "></td>");
        $tr.append("<td id=430" + rowId + "></td>");
        $tr.append("<td id=431" + rowId + "></td>");
        $('#content table tbody').append($tr);
    }
    $("#content table  td").addClass('available');
    $("#content  table td").dblclick(function (event) {
        const id = $(this).attr("id")

        if ($('#userLabel').html().trim() === "") {
            $("#loginModal").modal('show');
            return;
        }

        $.ajax({
            url: './servlets/prebook',
            type: 'GET',
            dataType: 'json',
            data: {
                roomStime: id
            }
        }).done(function (data) {
            $('#bookingDate').val(data.bookingDate);
            var room = data.bookingRoom;
            $("#bookingRoom option[value='" + room + "']").attr("selected", true);
            //have the selected room option selected.
            $('#startTime').val(data.startTime);
            $('#endTime').val(data.endTime);
            var unaRooms = data.unaRooms;

            $('.bg-danger').removeClass('bg-danger');
            for (var i = 0; i < unaRooms.length; i++) {
                $('#' + unaRooms[i]).addClass("bg-danger");
            }


            $("#bookingModal").modal('show');
        }).fail(function () {
            console.log("error");
        });


    });
}

function genIdByRowId(rowId) {
    if (rowId < 10)
        return "0" + rowId;
    else {
        return rowId;
    }
}

function genTimeByRowId(rowId) {
    var hour;
    var mins;
    if (rowId % 2 == 0) {
        hour = 7 + rowId / 2;
        mins = ':00';
    } else {
        hour = 7 + (rowId - 1) / 2;
        mins = ':30';
    }
    return hour.toString() + mins;
}

function displayRecordsByRoom(records) {
    for (var i = 0; i < records.length; i++) {
        debugger;
        var userName = records[i].userName;
        var recordId = records[i].recordId;
        var record = records[i].record;
        var record_len = record.length;

        var state = records[i].state;

        var dataContent = "<h6>联系人:<b>" + records[i].actualUser
            + "</b> </h6>   <h6>电   话:<b>" + records[i].phoneNum
            + "</b></h6>";

        var $userLink = $("<a tabindex='0'  data-toggle='popover' data-trigger='focus'  title='实际借用人信息' >"
            + userName + "</a>");

        if (state == "D") {
            $userLink = $("<a tabindex='0'  data-toggle='popover' data-trigger='focus'  title='实际借用人信息' >"
                + userName + " [待审核]" + "</a>");
        }


        $userLink.attr("data-content", dataContent);
        $userLink.popover({
            html: true
        });
        if (record_len == 1) {
            //only half a hour.
            if (state == "Y") {
                $("#" + record[0]).append($userLink).addClass('center').addClass(
                    'table-success').off('dblclick');
            } else {
                $("#" + record[0]).append($userLink).addClass('center').addClass(
                    'table-warning').off('dblclick');
            }

        } else if (record_len == 2) {
            // only a hour.
            $("#" + record[1]).remove();
            if (state == "Y") {
                $("#" + record[0]).attr("rowSpan", 2).append($userLink).addClass(
                    'center').addClass('table-success').off('dblclick');
            } else {
                $("#" + record[0]).attr("rowSpan", 2).append($userLink).addClass(
                    'center').addClass('table-warning').off('dblclick');
            }

        } else {
            //More than a hour.
            for (var j = 1; j < record_len; j++) {
                var divId = record[j];
                $("#" + divId).remove();
            }
            if (state == "Y") {
                //var userLink = "<a href=" + "/servlets/detail?recordId=" + recordId + ">" + userName + "</a>";
                $("#" + record[0]).attr("rowSpan", record_len).append($userLink)
                    .addClass('center').addClass('table-success').off(
                    'dblclick');
            } else {
                //var userLink = "<a href=" + "/servlets/detail?recordId=" + recordId + ">" + userName + "</a>";
                $("#" + record[0]).attr("rowSpan", record_len).append($userLink)
                    .addClass('center').addClass('table-warning').off(
                    'dblclick');
            }
        }
    }
}

$('#loginBtn').click(function (event) {

    if ($("#userName").val().trim() == ""
        || $('#password').val().trim() == "") {
        $('#logLabel').removeClass('hidden').empty().append(
            '<p>用户名和密码不能为空！</p>');
        return;
    }
    $
        .ajax({
            url: './servlets/login',
            type: 'POST',
            dataType: 'json',
            async: false,
            data: {
                userName: $("#userName").val().trim(),
                password: $('#password').val().trim()
            }
        })
        .done(
            function (data) {
                if (data.status == 1) {
                    var userLabelLink = "<a href=./userManage.html>"
                        + data.userName + "</a>";
                    $("#userLabel").append(
                        userLabelLink).removeClass(
                        'hidden');
                    $("#btnLogin").addClass('hidden');
                    $('#btnLogout').removeClass(
                        'hidden');
                    $("#loginModal").modal('hide');
                } else {
                    $('#logLabel')
                        .removeClass('hidden')
                        .empty()
                        .append('<p>用户名或者密码错误！</p>');
                }
            }).fail(function () {
        console.log("error");
    }).always(function () {
        console.log("complete");
    });

});

$("#content .previousDay").click(function (event) {

    $.ajax({
        url: './changeDate',
        type: 'GET',
        dataType: 'json',
        data: {
            changedDay: -1
        }
    }).done(function (data) {
        initTableByRoom();
        $('#selectDate').val(data.currentDate);
        $('#today').html(data.currentDate + "," + data.dayOfWeek);
        var records = data.records;
        if (records.length > 0) {
            displayRecordsByRoom(records);
        }
    }).fail(function () {
        console.log("error");
    });
});

$("#content .today").click(function (event) {
    $.ajax({
        url: './changeDate',
        type: 'GET',
        dataType: 'json',
        data: {
            changedDay: 0
        }
    }).done(function (data) {
        initTableByRoom();
        $('#selectDate').val(data.currentDate);
        $('#today').html(data.currentDate + "," + data.dayOfWeek);
        var records = data.records;
        if (records.length > 0) {
            displayRecordsByRoom(records);
        }
    }).fail(function () {
        console.log("error");
    });
});

$("#content .nextDay").click(function (event) {
    $.ajax({
        url: './changeDate',
        type: 'GET',
        dataType: 'json',
        data: {
            changedDay: 1
        }
    }).done(function (data) {
        initTableByRoom();
        $('#selectDate').val(data.currentDate);
        $('#today').html(data.currentDate + "," + data.dayOfWeek);
        var records = data.records;
        if (records.length > 0) {
            displayRecordsByRoom(records);
        }
    }).fail(function () {
        console.log("error");
    });
});

$('#bookingBtn').click(function (event) {
    //Start to book a  meeting room.
    //rules here.
    //Endtime must be later than the start time.
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    var actualUser = $('#actualUser').val();
    var phoneNum = $('#phoneNum').val();

    var a = /^(([01]?[0-9])|(2[0-3])):[03]?[0]$/;
    if (!a.test(startTime)) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>开始时间格式不正确，只能是7:00或者7:30这种整点格式！</p>');
        return;
    }

    if (!a.test(endTime)) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间格式不正确，只能是7:00或者7:30这种整点格式！</p>');
        return;
    }

    if (time2num(startTime) < 420) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>开始时间不能早于7:00！</p>');
        return;
    }

    if (time2num(startTime) > 1320) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>开始时间不能晚于22:00！</p>');
        return;
    }
    if (time2num(endTime) > 1350) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间不能晚于22:30！</p>');
        return;
    }
    if (time2num(endTime) < 450) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间不能早于7:30！</p>');
        return;
    }
    if (time2num(startTime) >= time2num(endTime)) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间段必须晚于开始时间段，请检查预订的时间段！</p>');
        return;
    }

    if (actualUser.trim() == "") {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>联系人信息不能为空！</p>');
        return;
    }

    if (phoneNum.trim() == "") {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>电话信息不能为空！</p>');
        return;
    }

    $.ajax({
        url: './servlets/book',
        type: 'POST',
        dataType: 'json',
        async: false,
        data: {
            bookingDate: $("#bookingDate").val(),
            bookingRoom: $('#bookingRoom').val(),
            startTime: startTime,
            endTime: endTime,
            actualUser: actualUser,
            phoneNum: phoneNum
        }
    }).done(
        function (data) {
            if (data.status == 1) {
                if ($('#bookingRoom').val() == '301') {
                    $('#bookingMsg').removeClass('hidden').empty()
                        .append('<p>已提交301会议室预订申请！</p>');
                    alert("已提交申请，请尽快联系院办321魏秀琴老师审批！");
                    $("#bookingModal").modal('hide');
                    initTableByRoom();
                    showDefaultData();
                } else {
                    $('#bookingMsg').removeClass('hidden').empty()
                        .append('<p>恭喜您，已经预订成功！</p>');
                    alert("恭喜您，已经预订成功！");
                    $("#bookingModal").modal('hide');
                    initTableByRoom();
                    showDefaultData();
                }

            } else {
                $('#bookingMsg').removeClass('hidden').empty()
                    .append('<p>抱歉，预订失败，请检查预订的时间段！</p>');
            }
        }).fail(function () {
        console.log("error");
    }).always(function () {
        console.log("complete");
    });
});

$('#btnSelectDate').click(function (event) {
    $.ajax({
        url: './changeDate',
        type: 'GET',
        dataType: 'json',
        data: {
            selectedDate: $('#selectDate').val()
        }
    }).done(function (data) {
        initTableByRoom();
        $('#selectDate').val(data.currentDate);
        $('#today').html(data.currentDate + "," + data.dayOfWeek);
        var records = data.records;
        if (records.length > 0) {
            displayRecordsByRoom(records);
        }
    }).fail(function () {
        console.log("error");
    });
});


function time2num(timeStr) {
    timeStr = timeStr.replace("：", ":");
    var hm = timeStr.split(":")
    return parseInt(hm[0].trim()) * 60 + parseInt(hm[1].trim());
}

function num2timeStr(numInt) {
    if (numInt % 60 == 0) {
        return numInt / 60 + ":00";
    } else {
        return (numInt - 30) / 60 + ":30";
    }
}

$('.spinner .btn:first-of-type').on('click', function () {
    var btn = $(this);
    var input = btn.closest('.spinner').find('input');

    if (input.attr('max') == undefined || time2num(input.val()) < parseInt(input.attr('max'))) {
        var newValue = num2timeStr(time2num(input.val()) + 30);
        input.val(newValue);
    } else {
        btn.next("disabled", true);
    }
});
$('.spinner .btn:last-of-type').on('click', function () {
    var btn = $(this);
    var input = btn.closest('.spinner').find('input');
    if (input.attr('min') == undefined || time2num(input.val()) > parseInt(input.attr('min'))) {
        var newValue = num2timeStr(time2num(input.val()) - 30);
        input.val(newValue);
    } else {
        btn.prev("disabled", true);
    }
});

$('#bookingDate, #bookingRoom').change(function () {
    $.ajax({
        url: './servlets/prebook',
        type: 'GET',
        dataType: 'json',
        data: {
            bookingDate: $('#bookingDate').val(),
            bookingRoom: $('#bookingRoom').val()
        }
    }).done(function (data) {
        var unaRooms = data.unaRooms;

        $('.bg-danger').removeClass('bg-danger');
        for (var i = 0; i < unaRooms.length; i++) {
            $('#' + unaRooms[i]).addClass("bg-danger");
        }
    }).fail(function () {
        console.log("error");
    });

});

$('#btnLogout').click(function (event) {
    $.ajax({
        url: './logout',
        type: 'GET',
        dataType: 'json'
    }).done(function (data) {
        $('#userLabel').empty().addClass('hidden');
        $('#btnLogin').removeClass('hidden');
        $('#btnLogout').addClass('hidden');
    }).fail(function () {
        console.log("error");
    });
});
