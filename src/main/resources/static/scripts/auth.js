// 鉴权
let inMemoryToken; // 用于验证的token存入内存
let interval; // 定时器
function login({access_token, access_token_expiry}) {
    inMemoryToken = {
        token: access_token,
        expiry: access_token_expiry
    };
}

function logout() {
    localStorage.removeItem("login_user");
    localStorage.removeItem("login_user_id");
    inMemoryToken = null; // 将token置空
    endCountdown(); // 停止倒计时
    localStorage.setItem("logout", Date.now());
}

async function auth(loginCallback, logoutCallback) {
    if (!inMemoryToken) {
        const url = `${base}/token/refresh`;
        try {
            const response = await fetch(url, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache'
                }
            })
            if (response.ok) {
                const res = await response.json();
                if (res.code !== 200) {
                    console.log("需要重新登录");
                    if (res.code === 401 && inMemoryToken) {
                        logout();
                        logoutCallback();
                    }
                } else {
                    const {access_token, access_token_expiry, user_id, user_name} = res.data;
                    login({access_token, access_token_expiry});
                    localStorage.setItem("login_user", user_name);
                    localStorage.setItem("login_user_id", user_id);
                }
            } else {
                console.log(response.statusText)
            }
        } catch (e) {
            console.log(e);
            logout();
            logoutCallback();
            loginCallback();
        }
    }
    const access_token = inMemoryToken;
    // We already checked for server. This should only happen on client.
    if (!access_token) {
        loginCallback();
    }
    return access_token
}

const addMinutes = function (dt, minutes) {
    return new Date(dt.getTime() + minutes * 60000);
}

const startCountdown = function () {
    interval = setInterval(async () => {
        if (inMemoryToken) {
            if (addMinutes(new Date(), 1) >= new Date(inMemoryToken.expiry)) {
                inMemoryToken = null;
                inMemoryToken = await auth();
            }
        } else {
            inMemoryToken = await auth();
        }
    }, 60000);
}

const onLogout = function (callback) {
    window.addEventListener("storage", callback);
}

const endCountdown = function () {
    clearInterval(interval);
}
