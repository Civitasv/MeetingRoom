// 鉴权
let inMemoryToken; // 用于验证的token存入内存
let interval; // 定时器
function login({access_token, access_token_expiry}) {
    inMemoryToken = {
        token: access_token,
        expiry: access_token_expiry
    };
}

function showLogin() {
    $("#loginModal").modal('show');
}

function userLogout() {
    inMemoryToken = null; // 将token置空
    localStorage.setItem("logout", Date.now());
    // 进入登录页面
    showLogin();
}

async function auth() {
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
            showLogin();
        }
    }
    const access_token = inMemoryToken;
    // We already checked for server. This should only happen on client.
    if (!access_token) {
        showLogin();
    }
    return access_token
}

const addMinutes = function (dt, minutes) {
    return new Date(dt.getTime() + minutes * 60000);
}

const startCountdown = function () {
    interval = setInterval(async () => {
        if (inMemoryToken) {
            console.log(inMemoryToken);
            if (addMinutes(new Date(), 1) >= new Date(inMemoryToken.expiry)) {
                inMemoryToken = null;
                inMemoryToken = await auth();
            }
        } else {
            inMemoryToken = await auth();
        }
    }, 60000);
    window.addEventListener("storage", syncLogout);
}

function syncLogout(event) {
    if (event.key === 'logout') {
        console.log('logged out from storage!')
        showLogin();
    }
}
