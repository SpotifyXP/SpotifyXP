var lastview = "";
var script;
var script2;

function switchView(to) {
    if (lastview != "") {
        window[lastview + "Controller"].disable();
        window[lastview + "Controller"] = null;
    }
    //Load View
    document.getElementById("middle").innerHTML = "";
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            document.getElementById('middle').innerHTML = xhr.responseText;
            //Load Controller for View
            var script = document.createElement("script");
            script.src = "controller/" + to + ".js";
            script.id = "controller-" + to;
            script.onload = function() {
                var script2 = document.createElement("script");
                script2.innerHTML = "var st = new " + to + "Controller();";
                document.getElementById("middle").appendChild(script2);
            }
            document.getElementById("middle").appendChild(script);
            lastview = to;
        }
    };
    xhr.open('GET', "views/" + to + ".html", true);
    xhr.send();
}

function setPlayerSongName(to) {
    document.getElementById("songname").textContent = to;
}

function setPlayerSongArtist(to) {
    document.getElementById("songartist").textContent = to;
}

function setPlayerSongImage(to) {
    document.getElementById("songimage").src = to;
}

function setPlayerSongLiked(value) {
    if (value) {
        document.getElementById("songliked").setAttribute("class", "ion-checkmark");
    } else {
        document.getElementById("songliked").setAttribute("class", "ion-plus");
    }
}

window.addEventListener("load", () => {
    createPage();
});

function createPage() {
    switchView("home");
    document.getElementById("profilepicture").src = getUserProfilePicture();
    document.getElementById("profilefirstname").textContent = getUserFirstName();
    document.getElementById("profilelastname").textContent = getUserLastName();
}