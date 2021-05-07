$(function () {
    auth(() => {
        location.href = `${base}`;
    }, () => {
    }).then(() => {
        // 刷新是否成功
        if (isLogin()) {
            startCountdown(() => {
                location.href = `${base}`;
            }, () => {
            });
            showUserInfo();
        }
    }, () => {
    })
    $("#revokeMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        await getCanRevokeData();
    });

    $("#historyMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        await getHistoryData();
    })

    $("#examineHMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        await getExamineData();
    });

    $("#editUserInfo").click(async () => {
        $('#result table:first').addClass('hidden');
        $('#result table:last').addClass('hidden');
        $('#editPersonInfo').removeClass('hidden');
        const userId = localStorage.getItem("login_user_id");
        const userName = localStorage.getItem("login_user");
        const phone = localStorage.getItem("login_user_phone");
        const roles = JSON.parse(localStorage.getItem("login_user_roles"));
        if (roles.find(value => {
            return value === '管理员'
        })) {
            $('#role').val("管理员");
        } else {
            $('#role').val("普通用户");
        }
        $('#userID').val(userId);
        $('#userName').val(userName);
        $('#phone').val(phone);
    });

    $('#editPersonInfo form input[type="checkbox"]').change(function () {
        if ($(this).is(":checked")) {
            //show the controls of setting password.
            $('#newPwd1Con').removeClass('hidden');
            $('#newPwd2Con').removeClass('hidden');
        } else {
            //hide the password setting controls.
            $('#newPwd1Con').addClass('hidden');
            $('#newPwd2Con').addClass('hidden');
        }
    });
});

function showUserInfo() {
    // 获取角色
    const roles = JSON.parse(localStorage.getItem("login_user_roles"));
    if (roles.find(value => {
        return value === '管理员'
    })) {
        $('nav li:nth-last-child(2)').removeClass('hidden');
        $('#examineHMR').removeClass('hidden');
    } else {
        console.log("普通用户");
    }
}

async function getCanRevokeData() {
    const url = `${base}/record/canRevoke`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                alert(res.message);
                return;
            }
            showRevokeTable(res.data);
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

async function getHistoryData() {
    const userId = localStorage.getItem("login_user_id");
    const url = `${base}/record/history?id=${userId}`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                alert(res.message);
                return;
            }
            showHistoryData(res.data);
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

async function getExamineData() {
    const url = `${base}/record/examine`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                alert(res.message);
                return;
            }
            showExamineData(res.data);
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

async function revokeRecord(id) {
    const url = `${base}/record/delete/${id}`
    try {
        const response = await fetch(url, {
            method: 'DELETE',
            credentials: 'include',
            headers: {"Authorization": inMemoryToken["token"]}
        })
        if (response.ok) {
            const res = await response.json();
            console.log(res.code !== 200 ? "无法取消预定" : "成功取消预定");
            if (res.code === 200) {
                $('#' + id).parents('tr').empty().remove();
                alert("已撤销该会议室预订！");
            }
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

async function agreeRecord(id, $a, $b) {
    const url = `${base}/record/update/${id}/1`
    try {
        const response = await fetch(url, {
            method: 'PUT',
            credentials: 'include',
            headers: {"Authorization": inMemoryToken["token"]}
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code === 200) {
                $a.text('已批准');
                $a.removeAttr('href');
                $a.removeAttr('onclick');
                $b.addClass('hidden');
                alert("已批准该会议室预订！");
            }
        } else {
            console.log(response.statusText)
        }
    } catch (e) {
        console.log(e);
    }
}

/**
 * @param data 可以撤销的数据
 */
function showRevokeTable(data) {
    $('#ERTable tbody').html('');
    data.forEach((item, i) => {
        const $tr = $('<tr></tr>');
        $tr.append("<th scope='row'>" + i + "</th>");
        $tr.append("<td>" + formatDateYYYYMMSS(timestampToDate(item.date)) + "</td>");
        $tr.append("<td>" + item.user.name + "</td>");
        $tr.append("<td>" + item.room + "</td>");
        $tr.append("<td>" + timestampToDateString(item.start) + "</td>");
        $tr.append("<td>" + timestampToDateString(item.end) + "</td>");
        $tr.append("<td>" + item.realUser + "</td>");
        $tr.append("<td>" + item.phone + "</td>");
        const $a = $('<a href="#" ></a>').attr("id", item.id).text('撤销');
        $a.click(async () => {
            await revokeRecord(item.id);
        })
        const $td = $('<td></td>').append($a);
        $tr.append($td);
        $('#ERTable tbody').append($tr);
    });
    $('#HTable').addClass('hidden');
    $('#ERTable').removeClass('hidden');
}

/**
 * @param data 用户预定历史记录
 */
function showHistoryData(data) {
    $('#HTable tbody').html('');
    data.forEach((item, i) => {
        const $tr = $('<tr></tr>');
        $tr.append("<th scope='row'>" + i + "</th>");
        $tr.append("<td>" + formatDateYYYYMMSS(timestampToDate(item.date)) + "</td>");
        $tr.append("<td>" + item.user.name + "</td>");
        $tr.append("<td>" + item.room + "</td>");
        $tr.append("<td>" + timestampToDateString(item.start) + "</td>");
        $tr.append("<td>" + timestampToDateString(item.end) + "</td>");
        $tr.append("<td>" + item.realUser + "</td>");
        $tr.append("<td>" + item.phone + "</td>");

        $('#HTable tbody').append($tr);
    });
    $('#ERTable').addClass('hidden');
    $('#HTable').removeClass('hidden');
}

/**
 * @param data 管理员审核数据
 */
function showExamineData(data) {
    $('#ERTable tbody').html('');
    data.forEach((item, i) => {
        const $tr = $('<tr></tr>');
        $tr.append("<th scope='row'>" + i + "</th>");
        $tr.append("<td>" + formatDateYYYYMMSS(timestampToDate(item.date)) + "</td>");
        $tr.append("<td>" + item.user.name + "</td>");
        $tr.append("<td>" + item.room + "</td>");
        $tr.append("<td>" + timestampToDateString(item.start) + "</td>");
        $tr.append("<td>" + timestampToDateString(item.end) + "</td>");
        $tr.append("<td>" + item.realUser + "</td>");
        $tr.append("<td>" + item.phone + "</td>");
        const $a = $('<a href="#" ></a>').attr("id", item.id).text('批准');
        const $b = $('<a href="#" ></a>').attr("id", item.id).text('拒绝');

        if (item.state === 1) {
            $a.text('已批准');
            $a.removeAttr('href');
            $a.removeAttr('onclick');
            $b.addClass('hidden');
        }

        $a.click(async () => {
            await agreeRecord(item.id, $a, $b); // 批准
        });

        $b.click(async () => {
            await revokeRecord(item.id);
        });

        const $td = $('<td></td>').append($('<p></p>').append($a));
        $tr.append($td.append($('<p></p>').append($b)));

        $('#ERTable tbody').append($tr);
    });
    $('#ERTable').removeClass('hidden');
    $('#HTable').addClass('hidden');
}

//Summit to modify the personal infomation.
$('#btnModify').click(async function () {
    //the new password does not match with the repeated one.
    const checked = $('#chkChgPwd').is(':checked');
    const password = $('#newPwd1').val();
    const repeatPassword = $('#newPwd2').val();
    if (checked && password !== repeatPassword) {
        $('#infoLabel').text("两次输入的新密码不一样！");
        $('#infoLabelCon').removeClass("hidden");
        return;
    }
    let user;
    if (checked) {
        user = {
            id: localStorage.getItem("login_user_id"),
            password: password,
            phone: $('#phone').val(),
            name: localStorage.getItem("login_user")
        }
        await updatePwdAndPhone(user, () => {
            $('#infoLabel').text("恭喜，信息修改成功！");
            $('#infoLabelCon').removeClass("hidden");
        }, () => {
            $('#infoLabel').text("抱歉，信息修改失败！");
            $('#infoLabelCon').removeClass("hidden");
        });
    } else {
        user = {
            id: localStorage.getItem("login_user_id"),
            phone: $('#phone').val()
        }
        await updatePhone(user, () => {
            $('#infoLabel').text("恭喜，信息修改成功！");
            $('#infoLabelCon').removeClass("hidden");
        }, () => {
            $('#infoLabel').text("抱歉，信息修改失败！");
            $('#infoLabelCon').removeClass("hidden");
        });
    }


});

/**
 * 更新用户
 * @param user 用户数据
 * @param resolve 成功
 * @param reject 失败
 */
async function updatePwdAndPhone(user, resolve, reject) {
    const url = `${base}/user/update`
    try {
        const response = await fetch(url, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache',
                "Authorization": inMemoryToken["token"]
            },
            body: JSON.stringify(user)
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code === 200) {
                resolve();
            } else
                reject();
        } else {
            reject();
            console.log(response.statusText)
        }
    } catch (e) {
        reject();
        console.log(e);
    }
}

/**
 * 更新用户手机
 * @param user 用户数据
 * @param resolve 成功
 * @param reject 失败
 */
async function updatePhone(user, resolve, reject) {
    const url = `${base}/user/updatePhone`
    try {
        const response = await fetch(url, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache',
                "Authorization": inMemoryToken["token"]
            },
            body: JSON.stringify(user)
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code === 200) {
                resolve();
            } else
                reject();
        } else {
            reject();
            console.log(response.statusText)
        }
    } catch (e) {
        reject();
        console.log(e);
    }
}