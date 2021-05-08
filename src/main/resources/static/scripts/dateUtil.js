const dateStringToTimestamp = function (DateString) {
    const dateStr = DateString.replace(/-/g, '/');
    return Date.parse(dateStr) / 1000;
}

const timestampToDate = function (timestamp) {
    return new Date(timestamp * 1000);
}

const showIntString = function (val) {
    return val.toString().padStart(2, "0");
}

const timestampToDateString = function (timestamp) {
    const date = timestampToDate(timestamp);
    return date.getFullYear() + "-" + showIntString(date.getMonth() + 1) + "-" + showIntString(date.getDate()) + ' ' + showIntString(date.getHours()) + ':' + showIntString(date.getMinutes());
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
