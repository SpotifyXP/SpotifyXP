function sendPlayPause() {
    makeGet("playpause");
}
function load(name) {
    makeGet("load", "key=" + name);
}
function next() {
    makeGet("next");
}
function previous() {
    makeGet("prev");
}
function vup() {
    makeGet("volumeup");
}
function vdown() {
    makeGet("volumedown");
}

function makeGet(url, params, whendone) {
    const req = new XMLHttpRequest();
    req.onload = function() {
        try {
        whendone(req.responseText);
        }catch(e){

        }
    }
    req.open("GET", "http://" + location.host + "/" + url + "?" + params);
    req.send();
}

function omakeGet(url, params, method, whendone) {
    makeGet("apikey", "", function(key) {
    const req = new XMLHttpRequest();
    req.onload = function() {
        whendone(req.responseText);
    }
    req.open(method, "https://api.spotify.com/v1/" + url + "?" + params);
    req.setRequestHeader("Authorization", "Bearer " + key);
    req.send();
    });
}

function checksel(on) {
    var sepc = document.getElementById("searchepisode").checked;
    var ssoc = document.getElementById("searchsong").checked;
    var sep = document.getElementById("searchepisode");
    var sso = document.getElementById("searchsong");
    if(sepc || ssoc) {
        sep.checked = false;
        sso.checked = false;
    }
    on.checked = true;
}

function search() {
    var tos = document.getElementById("tosearch").value;
    var sep = document.getElementById("searchepisode").checked;
    var sso = document.getElementById("searchsong").checked;
    if(sep) {
        return searchEpisodes(tos);
    }
    if(sso) {
        return searchSongs(tos);
    }
}

function play(id) {
    makeGet("loadId", "key=" + id);
}

function searchSongs(name) {
    var endp = "search";
    omakeGet(endp, "type=track&market=US&limit=25&q=" + encodeURI(name), "GET", function(res) {
        parseSongs(res);
    });
}

function parseSongs(res) {
    var json = JSON.parse(res);
    var out = "";
    for(var i = 0; i < json.tracks.items.length; i++) {
        var artists = "";
        for(var i2 = 0; i2 < json.tracks.items[i].artists.length; i2++) {
            artists = artists + ", " + json.tracks.items[i].artists[i2].name;
        }
        artists = artists.substring(1);
        out = out + "<button onclick=\"play('" + json.tracks.items[i].uri + "')\">" + artists + " - " + json.tracks.items[i].name + "</button><br>"; 
    }
    document.getElementById("result").innerHTML = out;
}

function removeLastInstance(badtext, str) {
    var charpos = str.lastIndexOf(badtext);
    if (charpos<0) return str;
    ptone = str.substring(0,charpos);
    pttwo = str.substring(charpos+(badtext.length));
    return (ptone+pttwo);
}


function searchEpisodes(name) {
    var endp = "search";
    omakeGet(endp, "type=episode&market=US&limit=25&q=" + encodeURI(name), "GET", function(res) {
        parseEpisodes(res);
    });
}

function parseEpisodes(res) {
    var json = JSON.parse(res);
    var out = "";
    for(var i = 0; i < json.episodes.items.length; i++) {
        out = out + "<button onclick=\"play('" + json.episodes.items[i].uri + "')\">" + json.episodes.items[i].name + "</button><br>"; 
    }
    document.getElementById("result").innerHTML = out;
}

document.addEventListener("load", function() {
    search()
})