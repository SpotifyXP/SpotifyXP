function sendStop() {
    makeGet("stop");
}
function sendPlay() {
    makeGet("play");
}
function makeGet(url, params) {
    const req = new XMLHttpRequest();
req.addEventListener("load", function() {
    return req.responseText;
});
req.open("GET", "http://127.0.0.1:790/" + url + "?" + params);
req.send();
}