const base = "http://localhost";
const room = [220, 301, 313, 320, 504, 429, 430, 431];
const week = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
let chooseDate = 0;

// 如果已经登录
if (window.localStorage.getItem("login_user")) {
    $("#userLabel").text(window.localStorage.getItem("login_user")).removeClass(
        'hidden');
    $("#btnLogin").addClass('hidden');
    $('#btnLogout').removeClass(
        'hidden');
}

const dateStringToTimestamp = function (DateString) {
    const dateStr = DateString.replace(/-/g, '/');
    return Date.parse(dateStr) / 1000;
}

const timestampToDate = function (timestamp) {
    return new Date(timestamp * 1000);
}

const timestampToDateString = function (timestamp) {
    const date = timestampToDate(timestamp);
    return date.getFullYear() + "-" + showIntString(date.getMonth() + 1) + "-" + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes();
}

const formatDateYYYYMMSS = function (date) {
    return date.getFullYear() + "-" + showIntString(date.getMonth() + 1) + "-" + showIntString(date.getDate());
}

const getWeekDay = function (timestamp) {
    const date = timestampToDate(timestamp);
    return week[date.getDay()];
}
const time2num = function (timeStr) {
    timeStr = timeStr.replace("：", ":");
    const hm = timeStr.split(":");
    return parseInt(hm[0].trim()) * 60 + parseInt(hm[1].trim());
}

const num2timeStr = function (numInt) {
    if (numInt % 60 === 0) {
        return numInt / 60 + ":00";
    } else {
        return (numInt - 30) / 60 + ":30";
    }
}

const showRecordByTimestamp = function (timestamp) {
    initTableRoom();
    $.ajax({
        url: `${base}/record/${timestamp}`,
        type: 'GET',
        dataType: 'json'
    }).done(function (res) {
        if (res.code !== 200) {
            alert(res.message);
            return;
        }
        const date = formatDateYYYYMMSS(timestampToDate(timestamp));
        $("#selectDate").val(date);
        $('#today').html(date + ", " + getWeekDay(timestamp));
        /*
         * If any user is returned, show it.
         */
        const records = res.data;
        if (records.length > 0) {
            displayRecords(records);
        }


        /* const userLabelLink = "<a href=./userManage.html>"
             + data.userName + "</a>";
         $("#userLabel").append(
             userLabelLink).removeClass(
             'hidden');*/
        /*$("#btnLogin").addClass('hidden');
        $('#btnLogout').removeClass(
            'hidden');
        $("#loginModal").modal('hide');*/

    }).fail(function () {
        console.log("error");
    });
}

const initTableRoom = function () {
    $('#content table tbody td').empty();
    $('#content table tbody tr').empty().remove();
    for (let i = 0; i < 32; i++) {
        const hm = genTimeByRowId(i);
        const $tr = $('<tr></tr>');
        const rowId = showIntString(i);
        const time = getTimeByRowId(i);
        $tr.append("<th scope='row'>" + hm + "</th>");
        room.forEach((item) => {
            const h = $("<td id=" + item.toString() + rowId + "></td>");
            h.attr("time", time);
            $tr.append(h);
        })
        $('#content table tbody').append($tr);
    }

    $("#content table td").dblclick(function () {
        if (!window.localStorage.getItem("login_user")) {
            $("#loginModal").modal('show');
            return;
        }
        $("#bookingModal").modal('show');
    });
}

const genTimeByRowId = function (rowId) {
    const start = 7;
    let hour;
    let minute;
    if (rowId % 2 === 0) {
        hour = start + rowId / 2;
        minute = ':00';
    } else {
        hour = start + (rowId - 1) / 2;
        minute = ':30';
    }
    return hour.toString() + minute;
}

const getTimeByRowId = function (rowId) {
    let start = 7 * 60 * 60; // 7:00
    for (let i = 0; i < rowId; i++) {
        start += 30 * 60;
    }
    return start;
}

const showIntString = function (val) {
    return val.toString().padStart(2, "0");
}

const displayRecords = function (records) {
    for (let i = 0; i < records.length; i++) {
        const userName = records[i].user.name; // 用户名
        const phone = records[i].phone; // 用户电话
        // const id = records[i].id; // 预定id
        const time = records[i].date; // 开始时间
        const start = records[i].start; // 开始时间
        const end = records[i].end; // 结束时间
        const room = records[i].room; // 会议室
        const state = records[i].state;
        const extra = 7 * 60 * 60; // 开始时间：7:00
        const first = (start - time - extra) / 1800; // 开始id
        const last = (end - time - extra) / 1800; // 结束id
        const len = (end - start) / 1800 + 1;
        const dataContent = "<h6>联系人:<b>" + records[i].realUser
            + "</b> </h6>   <h6>电   话:<b>" + phone
            + "</b></h6>";

        let $userLink;
        if (state === 0) {
            $userLink = $("<a tabindex='0'  data-toggle='popover' data-trigger='focus'  title='实际借用人信息' >"
                + userName + " [待审核]" + "</a>");
        } else {
            $userLink = $("<a tabindex='0'  data-toggle='popover' data-trigger='focus'  title='实际借用人信息' >"
                + userName + "</a>");
        }

        $userLink.attr("data-content", dataContent);
        $userLink.popover({
            html: true
        });
        for (let i = first + 1; i < last; i++) {
            $("#" + room + showIntString(i)).remove();
        }
        if (state === 1) {
            $("#" + room + showIntString(first)).attr("rowSpan", len).append($userLink).addClass(
                'center').addClass('table-success').off('dblclick');
        } else {
            $("#" + room + showIntString(first)).attr("rowSpan", len).append($userLink).addClass(
                'center').addClass('table-warning').off('dblclick');
        }
    }
}

$('#loginBtn').click(() => {
    // 用户名
    const userId = $("#userName").val();
    const password = $("#password").val();
    if (userId === "" || password === "") {
        $('#logLabel').removeClass('hidden').empty().append(
            '<p>用户名和密码不能为空！</p>');
        return;
    }
    $.ajax({
        url: `${base}/user/login`,
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify({
            id: userId,
            password: password
        })
    }).done((res) => {
        if (res.code !== 200) {
            $('#logLabel')
                .removeClass('hidden')
                .empty()
                .append('<p>用户名或者密码错误！</p>');
            return;
        }
        const data = res.data;
        // 获取token
        const token = data.token;
        // 获取user
        const user = data.user;
        // 将token和username存入localStorage
        window.localStorage.setItem("JWT_TOKEN", token);
        window.localStorage.setItem("login_user", user.name);
        window.localStorage.setItem("login_user_id", user.id);
        $("#userLabel").text(user.name).removeClass(
            'hidden');
        $("#btnLogin").addClass('hidden');
        $('#btnLogout').removeClass(
            'hidden');
        $("#loginModal").modal('hide');
    }).fail(function () {
        console.log("error");
    }).always(function () {
        console.log("complete");
    });
});

$("#content .previousDay").click(() => {
    // 时间向前移动
    chooseDate = chooseDate - 24 * 60 * 60;
    const date = formatDateYYYYMMSS(timestampToDate(chooseDate));
    $("#selectDate").val(date);
    $('#today').html(date + ", " + getWeekDay(chooseDate));
    showRecordByTimestamp(chooseDate);
});

$("#content .today").click(() => {
    chooseDate = dateStringToTimestamp(formatDateYYYYMMSS(new Date())); // 选择今天
    const date = formatDateYYYYMMSS(timestampToDate(chooseDate));
    $("#selectDate").val(date);
    $('#today').html(date + ", " + getWeekDay(chooseDate));
    showRecordByTimestamp(chooseDate);
});

$("#content .nextDay").click(() => {
    // 时间向后移动
    chooseDate = chooseDate + 24 * 60 * 60;
    const date = formatDateYYYYMMSS(timestampToDate(chooseDate));
    $("#selectDate").val(date);
    $('#today').html(date + ", " + getWeekDay(chooseDate));
    showRecordByTimestamp(chooseDate);
});


$('#btnSelectDate').click(() => {
    chooseDate = dateStringToTimestamp($("#selectDate").val());
    const date = formatDateYYYYMMSS(timestampToDate(chooseDate));
    $('#today').html(date + ", " + getWeekDay(chooseDate));
    showRecordByTimestamp(chooseDate);
});

$('#bookingBtn').click(() => {
    // Start to book a  meeting room.
    // rules here.
    // end time must be later than the start time.
    const startTime = $('#startTime').val();
    const endTime = $('#endTime').val();
    const actualUser = $('#actualUser').val();
    const phoneNum = $('#phoneNum').val();
    const startTimestamp = time2num(startTime);
    const endTimestamp = time2num(endTime);
    const timestamp = dateStringToTimestamp($("#bookingDate").val());
    const room = $("#bookingRoom option:selected").attr("value");
    const userId = window.localStorage.getItem("login_user_id");
    const a = /^(([01]?[0-9])|(2[0-3])):[03]?[0]$/;
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

    if (startTimestamp < 420) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>开始时间不能早于7:00！</p>');
        return;
    }

    if (startTimestamp > 1320) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>开始时间不能晚于22:00！</p>');
        return;
    }
    if (endTimestamp > 1350) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间不能晚于22:30！</p>');
        return;
    }
    if (endTimestamp < 450) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间不能早于7:30！</p>');
        return;
    }
    if (startTimestamp >= endTimestamp) {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>结束时间段必须晚于开始时间段，请检查预订的时间段！</p>');
        return;
    }

    if (actualUser.trim() === "") {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>联系人信息不能为空！</p>');
        return;
    }

    if (phoneNum.trim() === "") {
        $('#bookingMsg').removeClass('hidden').empty()
            .append('<p>电话信息不能为空！</p>');
        return;
    }

    $.ajax({
        url: `${base}/record/book`,
        type: 'POST',
        dataType: 'json',
        contentType: "application/json",
        data: JSON.stringify({
            timestamp: timestamp,
            room: room,
            state: room === '301' ? 0 : 1,
            start: timestamp + startTimestamp * 60,
            end: timestamp + endTimestamp * 60,
            phone: phoneNum,
            userId: userId,
            realUser: actualUser
        })
    }).done(function (res) {
        if (res.code !== 201) {
            $('#bookingMsg').removeClass('hidden').empty()
                .append('<p>抱歉，预订失败，请检查预订的时间段！</p>');
            return;
        }
        if (room === '301') {
            $('#bookingMsg').removeClass('hidden').empty()
                .append('<p>已提交301会议室预订申请！</p>');
            alert("已提交申请，请尽快联系院办321魏秀琴老师审批！");
        } else {
            $('#bookingMsg').removeClass('hidden').empty()
                .append('<p>恭喜您，已经预订成功！</p>');
            alert("恭喜您，已经预订成功！");
        }
        $("#bookingModal").modal('hide');
        showRecordByTimestamp(timestamp);
    }).fail(function () {
        console.log("error");
    }).always(function () {
        console.log("complete");
    })
});
$('.spinner .btn:first-of-type').on('click', function () {
    const btn = $(this);
    const input = btn.closest('.spinner').find('input');

    if (input.attr('max') === undefined || time2num(input.val()) < parseInt(input.attr('max'))) {
        const newValue = num2timeStr(time2num(input.val()) + 30);
        input.val(newValue);
    } else {
        btn.next("disabled", true);
    }
});
$('.spinner .btn:last-of-type').on('click', function () {
    const btn = $(this);
    const input = btn.closest('.spinner').find('input');
    if (input.attr('min') === undefined || time2num(input.val()) > parseInt(input.attr('min'))) {
        const newValue = num2timeStr(time2num(input.val()) - 30);
        input.val(newValue);
    } else {
        btn.prev("disabled", true);
    }
});

$('#bookingDate, #bookingRoom').change(function () {
    const time = dateStringToTimestamp($("#bookingDate").val());
    if (!time) return;
    const room = $("#bookingRoom option:selected").attr("value");
    $.ajax({
        url: `${base}/record/unavailable/${room}/${time}`,
        type: 'GET',
        dataType: 'json'
    }).done(function (res) {
        if (res.code !== 200) {
            alert(res.message);
            return;
        }
        const unavailableRooms = res.data;

        $('.bg-danger').removeClass('bg-danger');
        unavailableRooms.forEach(item => {
            const start = item.start;
            const end = item.end;
            const extra = 7 * 60 * 60; // 开始时间：7:00
            const first = (start - time - extra) / 1800 + 1; // 开始id
            const last = (end - time - extra) / 1800 + 1; // 结束id
            for (let i = first; i <= last; i++) {
                $('#' + i).addClass("bg-danger");
            }
        })
    }).fail(function () {
        console.log("error");
    });
});

$("#btnLogout").click(function () {
    $('#userLabel').empty().addClass('hidden');
    $('#btnLogin').removeClass('hidden');
    $('#btnLogout').addClass('hidden');
    window.localStorage.removeItem("JWT_TOKEN");
    window.localStorage.removeItem("login_user");
    window.localStorage.removeItem("login_user_id");
});

$(function () {
    chooseDate = dateStringToTimestamp(formatDateYYYYMMSS(new Date())); // 默认选择今天
    $("#bookingDate").val(formatDateYYYYMMSS(timestampToDate(chooseDate)));
    showRecordByTimestamp(chooseDate);
    $('#bookingDate, #selectDate').datepicker({
        todayBtn: "linked",
        language: "zh-CN",
        todayHighlight: true
    });
    $('#btn_select_room').click(function () {
        alert("此功能还在开发中，敬请期待！");
    });
});