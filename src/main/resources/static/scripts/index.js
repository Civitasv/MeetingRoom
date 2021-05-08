const room = [220, 301, 313, 320, 504, 429, 430, 431, 212];
const week = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
let loginModal, bookModal;
let chooseDate = 0;

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
    }).fail(function () {
        console.log("error");
    });
}

const initTableRoom = function () {
    $('#content table tbody td').empty();
    $('#content table tbody tr').empty().remove();
    for (let i = 0; i < 31; i++) {
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
        if (inMemoryToken) {
            bookModal.show();
        } else {
            loginModal.show();
        }
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
        const len = (end - start) / 1800;
        const dataContent = "<h6>联系人:<b>" + records[i].realUser
            + "</b> </h6>   <h6>电   话:<b>" + phone
            + "</b></h6>";

        let $userLink;
        if (state === 0) {
            $userLink = $("<a tabindex='0'  data-bs-toggle='popover' data-bs-trigger='focus' data-bs-placement='right' title='实际借用人信息' data-bs-content='ss' style='font-weight: bold;color: #009879;'>"
                + userName + " [待审核]" + "</a>");
        } else {
            $userLink = $("<a tabindex='0'  data-bs-toggle='popover' data-bs-trigger='focus' data-bs-placement='right' title='实际借用人信息' data-bs-content='ss' style='font-weight: bold;color: #000;'>"
                + userName + "</a>");
        }

        $userLink.attr("data-bs-content", dataContent);
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

$('#loginBtn').click(async () => {
    // 用户名
    const userId = $("#userName").val();
    const password = $("#password").val();
    if (userId === "" || password === "") {
        alert("用户名和密码不能为空！");
        return;
    }
    try {
        const response = await fetch(`${base}/user/login`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache'
            },
            body: JSON.stringify({
                id: userId,
                password: password
            })
        });
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                alert("用户名或者密码错误！");
                return;
            }
            console.log(res);
            const {access_token, access_token_expiry, user_id, user_name, user_roles, user_phone} = res.data;
            login({access_token, access_token_expiry});
            localStorage.setItem("login_user", user_name);
            localStorage.setItem("login_user_id", user_id);
            localStorage.setItem("login_user_roles", JSON.stringify(user_roles));
            localStorage.setItem("login_user_phone", user_phone);
            startCountdown(toLogin, toLogout);
            $("#userLabel").text(user_name).removeClass(
                'hidden');
            $("#btnLogin").addClass('hidden');
            $('#btnLogout').removeClass(
                'hidden');
            console.log("登录成功")
            loginModal.hide();
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
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
    const userId = localStorage.getItem("login_user_id");
    const a = /^(([01]?[0-9])|(2[0-3])):[03]?[0]$/;
    if (!a.test(startTime)) {
        alert("开始时间格式不正确，只能是7:00或者7:30这种整点格式！");
        return;
    }

    if (!a.test(endTime)) {
        alert("结束时间格式不正确，只能是7:00或者7:30这种整点格式！");
        return;
    }

    if (startTimestamp < 420) {
        alert("开始时间不能早于7:00！");
        return;
    }

    if (startTimestamp > 1320) {
        alert("开始时间不能晚于22:00！");
        return;
    }
    if (endTimestamp > 1350) {
        alert("结束时间不能晚于22:30！");
        return;
    }
    if (endTimestamp < 450) {
        alert("结束时间不能早于7:30！");
        return;
    }
    if (startTimestamp >= endTimestamp) {
        alert("结束时间段必须晚于开始时间段，请检查预订的时间段！");
        return;
    }

    if (actualUser === "") {
        alert("联系人信息不能为空！");
        return;
    }

    if (phoneNum === "") {
        alert("电话信息不能为空！");
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
        }),
        headers: {"Authorization": inMemoryToken["token"]}
    }).done(function (res) {
        if (res.code !== 201) {
            alert("预定失败，请检查预约时间段或稍后重试");
            return;
        }
        if (room === '301') {
            alert("已提交申请，请尽快联系院办321魏秀琴老师审批！");
        } else {
            alert("恭喜您，已经预订成功！");
        }
        bookModal.hide();
        showRecordByTimestamp(timestamp);
    }).fail(function () {
        alert("预定失败，请检查预约时间段或稍后重试");
        console.log("error");
    });
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

$("#userLabel").click(() => {
    window.location.href = `${base}/manage`;
});

function changeBookingDateOrRoom() {
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
            const first = (start - time - extra) / 1800; // 开始id
            const last = (end - time - extra) / 1800; // 结束id
            for (let i = first; i < last; i++) {
                $('#' + (i + 1)).addClass("bg-danger");
            }
        })
    }).fail(function () {
        console.log("error");
    });
}

async function syncLogout(event) {
    if (event.key === 'logout') {
        if (!inMemoryToken) {
            return;
        }
        console.log('logged out from storage!')
        $('#userLabel').empty().addClass('hidden');
        $('#btnLogin').removeClass('hidden');
        $('#btnLogout').addClass('hidden');
        inMemoryToken = null; // 将token置空
        if (interval)
            endCountdown(); // 停止倒计时
        const url = `${base}/user/logout`
        try {
            const response = await fetch(url, {
                method: 'GET',
                credentials: 'include'
            })
            if (response.ok) {
                const res = await response.json();
                console.log(res.code !== 200 ? "退出失败" : "退出成功");
            } else {
                console.log(response.statusText)
            }
        } catch (e) {
            console.log(e);
        }
        loginModal.show();
    }
}

function toLogin() {
    loginModal.show();
}

function toLogout() {
    $('#userLabel').empty().addClass('hidden');
    $('#btnLogin').removeClass('hidden');
    $('#btnLogout').addClass('hidden');
}

$(function () {
    chooseDate = dateStringToTimestamp(formatDateYYYYMMSS(new Date())); // 默认选择今天
    loginModal = new bootstrap.Modal(document.getElementById("loginModal"), {});
    bookModal = new bootstrap.Modal(document.getElementById("bookingModal"), {});
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
    $("#btnLogout").click(async () => {
        $('#userLabel').empty().addClass('hidden');
        $('#btnLogin').removeClass('hidden');
        $('#btnLogout').addClass('hidden');
        await logout();
    });
    $("#help").click(() => {
        window.location.href = `${base}/help`;
    });
    $('#bookingDate, #bookingRoom').change(changeBookingDateOrRoom);
    onLogout(syncLogout);
    auth(toLogin, toLogout).then(() => {
        // 刷新是否成功
        if (isLogin()) {
            $("#userLabel").text(localStorage.getItem("login_user")).removeClass(
                'hidden');
            $("#btnLogin").addClass('hidden');
            $('#btnLogout').removeClass(
                'hidden');
            startCountdown(toLogin, toLogout);
        }
    })
});