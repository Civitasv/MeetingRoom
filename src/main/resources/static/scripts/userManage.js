let addUserModal, editUserModal, deleteUserModal, updateUserPwdModal;
$(function () {
    auth(() => {
        location.href = `${base}`;
    }, () => {
    }).then(async () => {
        // 刷新是否成功
        if (isLogin()) {
            startCountdown(() => {
                location.href = `${base}`;
            }, () => {
            });
            showUserInfo();
            // 默认展示预约历史数据
            setCurrentPageTitle("撤销会议室");
            await getCanRevokeData(1, $("#revokePerPageNum").val());
        }
    }, () => {
    })
    addUserModal = new bootstrap.Modal(document.getElementById("addUserModal"), {});
    editUserModal = new bootstrap.Modal(document.getElementById("editUserModal"), {});
    deleteUserModal = new bootstrap.Modal(document.getElementById("deleteUserModal"), {});
    updateUserPwdModal = new bootstrap.Modal(document.getElementById("updatePwdModal"), {});

    $("#revokeMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        $('.dataTable_wrapper').removeClass('hidden');
        $('#HTable').addClass('hidden');
        $('#ERTable').removeClass('hidden');
        $('#userTable').addClass('hidden');
        // 展示可以撤销的数据
        setCurrentPageTitle("撤销会议室");
        await getCanRevokeData(1, $("#revokePerPageNum").val());
    });

    $("#historyMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        $('.dataTable_wrapper').removeClass('hidden');
        $('#ERTable').addClass('hidden');
        $('#HTable').removeClass('hidden');
        $('#userTable').addClass('hidden');
        // 展示预约历史数据
        setCurrentPageTitle("历史预订记录");
        await getHistoryData(1, $("#historyPerPageNum").val());
    })

    $("#examineHMR").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        $('.dataTable_wrapper').removeClass('hidden');
        $('#ERTable').removeClass('hidden');
        $('#HTable').addClass('hidden');
        $('#userTable').addClass('hidden');
        // 展示管理员审核数据
        setCurrentPageTitle("预定审核");
        await getExamineData(1, $("#revokePerPageNum").val());
    });

    $("#userManage").click(async () => {
        $('#editPersonInfo').addClass('hidden');
        $('.dataTable_wrapper').removeClass('hidden');
        $('#ERTable').addClass('hidden');
        $('#HTable').addClass('hidden');
        $('#userTable').removeClass('hidden');
        // 展示所有用户
        setCurrentPageTitle("用户管理");
        await getUserData(1, $("#userPerPageNum").val());
    });

    $("#editUserInfo").click(async () => {
        $('.dataTable_wrapper').addClass('hidden');
        $('#editPersonInfo').removeClass('hidden');
        setCurrentPageTitle("信息修改");
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
    //Summit to modify the personal infomation.
    $('#btnModify').click(async function () {
        //the new password does not match with the repeated one.
        const checked = $('#chkChgPwd').is(':checked');
        const password = $('#newPwd1').val();
        const repeatPassword = $('#newPwd2').val();
        const phone = $('#phone').val();
        if (phone === "") {
            alert("手机号码不可以为空！");
            return;
        }
        let user;
        if (checked) {
            if (password === "") {
                alert("新密码不可以为空！");
                return;
            }
            if (password !== repeatPassword) {
                alert("两次输入的密码必须相同！");
                return;
            }
            user = {
                id: localStorage.getItem("login_user_id"),
                password: password,
                phone: phone
            }
            await updatePwdAndPhone(user, () => {
                alert("恭喜，信息修改成功！");
            }, () => {
                alert("抱歉，信息修改失败！");
            });
        } else {
            user = {
                id: localStorage.getItem("login_user_id"),
                phone: phone
            }
            await updatePhone(user, () => {
                alert("恭喜，信息修改成功！");
            }, () => {
                alert("抱歉，信息修改失败！");
            });
        }
    });
    $("#addUser").click(async () => {
        // 添加用户
        const id = $("#addUserID").val();
        const password = $("#addUserPwd").val();
        const phone = $("#addUserPhone").val();
        const name = $("#addUserName").val();
        const roleId = $("#addUserRole option:selected").attr("value");
        if (id === "") {
            alert("用户ID不可以为空！");
            return;
        }
        if (name === "") {
            alert("用户名不可以为空！");
            return;
        }
        if (password === "") {
            alert("密码不可以为空！");
            return;
        }
        if (phone === "") {
            alert("手机号码不可以为空！");
            return;
        }
        await addUser({
            id: id,
            password: password,
            phone: phone,
            name: name,
            roleId: roleId
        })
    });

    $("#updateUser").click(async () => {
        // 更新用户
        const id = $("#editUserID").val();
        const phone = $("#editUserPhone").val();
        const name = $("#editUserName").val();
        const roleId = $("#editUserRole option:selected").attr("value");
        if (id === "") {
            alert("用户ID不可以为空！");
            return;
        }
        if (name === "") {
            alert("用户名不可以为空！");
            return;
        }
        if (phone === "") {
            alert("手机号码不可以为空！");
            return;
        }
        await updateUser({
            id: id,
            phone: phone,
            name: name,
            roleId: roleId
        })
    });
    $("#deleteUser").click(async () => {
        const userId = $("#deleteUserId").text();
        await deleteUser(userId);
    });

    $("#updateUserPwd").click(async () => {
        const userId = $("#updateUserId").text();
        const password = $("#updatePwd").val();
        await updatePwd({
            id: userId,
            password: password,
        });
    });

    $("#userTable .page-link").click(async function () {
        await getUserData($(this).attr("data-page"), $("#userPerPageNum option:selected").attr("value"));
    })

    $("#ERTable .page-link").click(async function () {
        if ($("#current-page-title").text() === "撤销会议室") {
            await getCanRevokeData($(this).attr("data-page"), $("#revokePerPageNum option:selected").attr("value"));
        } else {
            await getExamineData($(this).attr("data-page"), $("#revokePerPageNum option:selected").attr("value"));
        }
    })

    $("#HTable .page-link").click(async function () {
        await getHistoryData($(this).attr("data-page"), $("#historyPerPageNum option:selected").attr("value"));
    })

    $("#userPerPageNum").change(async () => {
        initPagination("userTable");
        await getUserData(1, $("#userPerPageNum option:selected").attr("value"));
    })

    $("#revokePerPageNum").change(async () => {
        initPagination("ERTable");
        // 刷新
        if ($("#current-page-title").text() === "撤销会议室") {
            await getCanRevokeData(1, $("#revokePerPageNum option:selected").attr("value"));
        } else {
            await getExamineData(1, $("#revokePerPageNum option:selected").attr("value"));
        }
    })

    $("#historyPerPageNum").change(async () => {
        initPagination("HTable");
        await getHistoryData(1, $("#historyPerPageNum option:selected").attr("value"));
    })
});

async function addUser(user) {
    // 首先检查用户名是否可用
    const repeatUrl = `${base}/user/repeat?userId=${user.id}`;
    try {
        const response = await fetch(repeatUrl, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache',
                "Authorization": inMemoryToken["token"]
            }
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
                alert("该用户ID已被注册！");
                return;
            }
        } else {
            console.log(response.statusText);
            alert("该用户ID已被注册！");
            return;
        }
    } catch (e) {
        console.log(e);
        alert("该用户ID已被注册！");
        return;
    }
    const url = `${base}/user/add`
    try {
        const response = await fetch(url, {
            method: 'POST',
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
            if (res.code !== 201) {
                console.log(res.message);
                alert("添加用户失败！");
                return;
            }
            alert("成功添加用户！");
            // 模态框消失
            addUserModal.hide();
            // 重新展示用户数据
            await getUserData($("#userTable .active>a").attr("data-page"), $("#userPerPageNum option:selected").attr("value"));
        } else {
            console.log(response.statusText);
            alert("添加用户失败！");
        }
    } catch (e) {
        console.log(e);
        alert("添加用户失败！");
    }
}

async function updateUser(user) {
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
            if (res.code !== 200) {
                console.log(res.message);
                alert("更新用户失败！");
                return;
            }
            alert("成功更新用户！");
            // 模态框消失
            editUserModal.hide();
            // 重新展示用户数据
            await getUserData($("#userTable .active>a").attr("data-page"), $("#userPerPageNum option:selected").attr("value"));
        } else {
            console.log(response.statusText);
            alert("更新用户失败！");
        }
    } catch (e) {
        console.log(e);
        alert("更新用户失败！");
    }
}

async function deleteUser(userId) {
    const url = `${base}/user/delete?id=${userId}`
    try {
        const response = await fetch(url, {
            method: 'DELETE',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache',
                "Authorization": inMemoryToken["token"]
            }
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
                alert("删除用户失败！");
                return;
            }
            alert("成功删除用户！");
            // 模态框消失
            deleteUserModal.hide();
            // 重新展示用户数据
            await getUserData($("#userTable .active>a").attr("data-page"), $("#userPerPageNum option:selected").attr("value"));
        } else {
            console.log(response.statusText);
            alert("删除用户失败！");
        }
    } catch (e) {
        console.log(e);
        alert("删除用户失败！");
    }
}

/**
 * 更新用户密码
 * @param user 用户数据
 */
async function updatePwd(user) {
    const url = `${base}/user/updatePwd`
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
            if (res.code !== 200) {
                console.log(res.message);
                alert("更新用户密码失败！");
                return;
            }
            alert("成功更新用户密码！");
            // 模态框消失
            updateUserPwdModal.hide();
            // 重新展示用户数据
            await getUserData($("#userTable .active>a").attr("data-page"), $("#userPerPageNum option:selected").attr("value"));
        } else {
            alert("更新用户密码失败！");
            console.log(response.statusText)
        }
    } catch (e) {
        alert("更新用户密码失败！");
        console.log(e);
    }
}

function showUserInfo() {
    // 获取角色
    const roles = JSON.parse(localStorage.getItem("login_user_roles"));
    if (roles.find(value => {
        return value === '管理员'
    })) {
        $('#manage').removeClass('hidden');
    } else {
        console.log("普通用户");
        $('#manage').remove();
    }
}

async function getCanRevokeData(page, size) {
    const userId = localStorage.getItem("login_user_id");
    const url = `${base}/record/canRevoke?id=${userId}&page=${page}&size=${size}`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
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

async function getHistoryData(page, size) {
    const userId = localStorage.getItem("login_user_id");
    const url = `${base}/record/history?id=${userId}&page=${page}&size=${size}`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
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

async function getExamineData(page, size) {
    const url = `${base}/record/examine?page=${page}&size=${size}`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
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


async function getUserData(page, size) {
    const url = `${base}/user/all?page=${page}&size=${size}`
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include',
            headers: {"Authorization": inMemoryToken["token"]}
        })
        if (response.ok) {
            const res = await response.json();
            if (res.code !== 200) {
                console.log(res.message);
                return;
            }
            showUserData(res.data);
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
                // 刷新
                if ($("#current-page-title").text() === "撤销会议室") {
                    await getCanRevokeData($("#ERTable .active>a").attr("data-page"), $("#revokePerPageNum option:selected").attr("value"));
                } else {
                    await getExamineData($("#ERTable .active>a").attr("data-page"), $("#revokePerPageNum option:selected").attr("value"));
                }
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
                // 刷新
                await getExamineData($("#ERTable .active>a").attr("data-page"), $("#revokePerPageNum option:selected").attr("value"));
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
    // 获取分页相关信息
    const pageNum = data.pageNum; // 当前显示页
    const prePage = data.prePage; // 前一页页码
    const nextPage = data.nextPage; // 下一页页码
    const total = data.pages; // 总页码

    setPagination("ERTable", {pageNum, prePage, nextPage, total});
    data.list.forEach((item, i) => {
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
}

/**
 * @param data 用户预定历史记录
 */
function showHistoryData(data) {
    $('#HTable tbody').html('');
    // 获取分页相关信息
    const pageNum = data.pageNum; // 当前显示页
    const prePage = data.prePage; // 前一页页码
    const nextPage = data.nextPage; // 下一页页码
    const total = data.pages; // 总页码

    setPagination("HTable", {pageNum, prePage, nextPage, total});
    data.list.forEach((item, i) => {
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
}

/**
 * @param data 管理员审核数据
 */
function showExamineData(data) {
    $('#ERTable tbody').html('');
    // 获取分页相关信息
    const pageNum = data.pageNum; // 当前显示页
    const prePage = data.prePage; // 前一页页码
    const nextPage = data.nextPage; // 下一页页码
    const total = data.pages; // 总页码

    setPagination("ERTable", {pageNum, prePage, nextPage, total});

    data.list.forEach((item, i) => {
        const $tr = $('<tr></tr>');
        $tr.append("<th scope='row'>" + i + "</th>");
        $tr.append("<td>" + formatDateYYYYMMSS(timestampToDate(item.date)) + "</td>");
        $tr.append("<td>" + item.user.name + "</td>");
        $tr.append("<td>" + item.room + "</td>");
        $tr.append("<td>" + timestampToDateString(item.start) + "</td>");
        $tr.append("<td>" + timestampToDateString(item.end) + "</td>");
        $tr.append("<td>" + item.realUser + "</td>");
        $tr.append("<td>" + item.phone + "</td>");

        const $btnGroup = $('<div class="btn-group" role="group"></div>');
        const $a = $('<button type="button" class="btn btn-success" ></button>').attr("id", item.id).text('批准');
        const $b = $('<button type="button" class="btn btn-danger" ></button>').attr("id", item.id).text('拒绝');
        $btnGroup.append($a).append($b);
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

        const $td = $('<td></td>').append($btnGroup);
        $tr.append($td);

        $('#ERTable tbody').append($tr);
    });
}

function initPagination(id) {
    $(`#${id} .page-item:nth-child(1) > a`).attr("data-page", 1).parent(".page-item").removeClass("disabled");
    $(`#${id} .page-item:nth-child(2) > a`).attr("data-page", 1).text(1).parent(".page-item").addClass("active");
    $(`#${id} .page-item:nth-child(3) > a`).attr("data-page", 2).text(2).parent(".page-item").removeClass("hidden");
    $(`#${id} .page-item:nth-child(4) > a`).attr("data-page", 3).text(3).parent(".page-item").removeClass("hidden");
    $(`#${id} .page-item:nth-child(5) > a`).attr("data-page", 2).parent(".page-item").removeClass("disabled");
}

function setPagination(id, {pageNum, prePage, nextPage, total}) {
    if (total === 0 || total === 1) {
        $(`#${id} .page-item:nth-child(3)`).addClass("hidden");
        $(`#${id} .page-item:nth-child(4)`).addClass("hidden");
    } else if (total === 2) {
        $(`#${id} .page-item:nth-child(4)`).addClass("hidden");
    }
    // 先清除
    $(`#${id} .page-item`).removeClass("active");
    // 设置分页信息
    if (pageNum >= 2 && pageNum <= total - 1) { // 此时需要将选中页码移动至中间
        $(`#${id} .page-item:nth-child(3) > a`).attr("data-page", pageNum).text(pageNum);
        $(`#${id} .page-item:nth-child(2) > a`).attr("data-page", prePage).text(prePage);
        $(`#${id} .page-item:nth-child(1) > a`).attr("data-page", prePage).parent().removeClass("disabled");
        if (nextPage !== 0) {
            $(`#${id} .page-item:nth-child(4) > a`).attr("data-page", nextPage).text(nextPage).parent().removeClass("hidden");
            $(`#${id} .page-item:nth-child(5) > a`).attr("data-page", nextPage).parent().removeClass("disabled");
        } else {
            $(`#${id} .page-item:nth-child(4)`).addClass("hidden");
            $(`#${id} .page-item:nth-child(5)`).addClass("disabled");
        }
    } else { // 此时页码不需要移动
        if (pageNum === total) {
            if (prePage !== 0)
                $(`#${id} .page-item:nth-child(1) > a`).attr("data-page", prePage).parent().removeClass("disabled");
            $(`#${id} .page-item:nth-last-child(1)`).addClass("disabled");
        }
        if (pageNum === 1) {
            $(`#${id} .page-item:nth-child(1)`).addClass("disabled");
            if (nextPage !== 0) {
                $(`#${id} .page-item:nth-child(3) > a`).removeClass("hidden").attr("data-page", nextPage).text(nextPage);
                $(`#${id} .page-item:nth-last-child(1) > a`).attr("data-page", nextPage).parent().removeClass("disabled");
            }
        }
    }
    $(`#${id} .page-link[data-page=${pageNum}]`).parent(".page-item").addClass("active");
}

/**
 * @param data 用户数据
 */
function showUserData(data) {
    $('#userTable tbody').html('');
    // 获取分页相关信息
    const pageNum = data.pageNum; // 当前显示页
    const prePage = data.prePage; // 前一页页码
    const nextPage = data.nextPage; // 下一页页码
    const total = data.pages; // 总页码

    setPagination("userTable", {pageNum, prePage, nextPage, total});

    data.list.forEach((item) => {
        const roles = item.roles;
        let role;
        if (roles.find(value => {
            return value.role === '管理员'
        })) {
            role = "管理员";
        } else {
            role = "普通用户";
        }
        const $tr = $('<tr></tr>');
        $tr.append("<th scope='row'>" + item.id + "</th>");
        $tr.append("<td>" + item.name + "</td>");
        $tr.append("<td>" + item.phone + "</td>");
        $tr.append("<td>" + role + "</td>");

        const $btnGroup = $('<div class="btn-group" role="group"></div>');
        // 编辑
        const $a = $('<button type="button" class="btn btn-success" ><i class="fas fa-edit"></i></button>').attr("id", item.id);
        // 删除
        const $b = $('<button type="button" class="btn btn-danger" ><i class="fas fa-trash-alt"></i></button>').attr("id", item.id);
        // 更新密码
        const $c = $('<button type="button" class="btn btn-secondary" ><i class="fas fa-key"></i></button>').attr("id", item.id);

        $btnGroup.append($a).append($b).append($c);

        $a.click(() => {
            showEditUserModal({ // 编辑
                id: item.id,
                name: item.name,
                phone: item.phone,
                roleId: role === "管理员" ? 2 : 1
            });
        });

        $b.click(() => {
            showDeleteUserModal(item.id);
        });

        $c.click(() => {
            showUpdatePwdModal(item.id);
        })

        const $td = $('<td></td>').append($btnGroup);
        $tr.append($td);

        $('#userTable tbody').append($tr);
    });
}

function showEditUserModal(user) {
    $("#editUserID").val(user.id);
    $("#editUserName").val(user.name);
    $("#editUserPhone").val(user.phone);
    $("#editUserRole").find(`option[value=${user.roleId}]`).attr("selected", true);
    editUserModal.show();
}

function showDeleteUserModal(userId) {
    $("#deleteUserId").text(userId);
    deleteUserModal.show();
}

function showUpdatePwdModal(userId) {
    $("#updateUserId").text(userId);
    updateUserPwdModal.show();
}

/**
 * 更新用户
 * @param user 用户数据
 * @param resolve 成功
 * @param reject 失败
 */
async function updatePwdAndPhone(user, resolve, reject) {
    const url = `${base}/user/updatePwdAndPhone`
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

function setCurrentPageTitle(title) {
    $("#current-page-title").text(title);
    $("#current-page").text(title);
}
