# RestAPI
### Communicate with SpotifyXP without GUI


#### How to start the RestAPI
Append '--no-gui' to the arguments

<h4>Port</h4>
<p>The port is 4090</p>

<p><h4>Response format</h4> JSON. Example: <code>{"Response":"Hello World", [ADDITIONAL]}</code></p>



<br>

<h3>Methods</h3>
<table>
<tr>
<th>Method name</th>
<th>Description</th>
</tr>
<tr><td><a href="#apiauthenticate">api.authenticate</a></td><td>Authenticate your ip for requests (secure)</td></tr>
<tr><td><a href="#apiauthenticateinsecure">api.authenticateinsecure</a></td><td>Authenticate your ip for requests (insecure)</td></tr>
<tr><td><a href="#apigetloginstatus">api.getloginstatus</a></td><td>Returns the login status of Spotify</td></tr>
<tr><td><a href="#apigetkey">api.getkey</a></td><td>Returns the Bearer token for Spotify</td></tr>
<tr><td><a href="#apiinitlogin">api.initlogin</a></td><td>Login into Spotify</td></tr>
<tr><td><a href="#apiiplogout">api.iplogout</a></td><td>Logout from the RestAPI</td></tr>
<tr><td><a href="#apilogout">api.logout</a><td>Logout from Spotify</td></td></tr>
<tr><td><a href="#playergetmetadata">player.getmetadata</a></td><td>Get info about current playing track</td></tr>
<tr><td><a href="#playerplaypause">player.playpause</a></td><td>Play/Pause player</td></tr>
<tr><td><a href="#playernext">player.next</a></td><td>Play next track from queue</td></tr>
<tr><td><a href="#playerprevious">player.previous</a></td><td>Play previous track from queue</td></tr>
<tr><td><a href="#playerload">player.load</a></td><td>Load track uri</td></tr>
<tr><td><a href="#playerloadplay">player.loadplay</a></td><td>Play track uri</td></tr>
<tr><td><a href="#playergetvolume">player.getvolume</a></td><td>Returns current volume</td></tr>
<tr><td><a href="#playersetvolume">player.setvolume</a></td><td>Sets the current volume</td></tr>
<tr><td><a href="#playervolumeup">player.volumeup</a></td><td>Turns the volume up step by step</td></tr>
<tr><td><a href="#playervolumedown">player.volumedown</a></td><td>Turns the volume down step by step</td></tr>
<tr><td><a href="#playercurrenttime">player.currenttime</a></td><td>Sets the current playtime (milliseconds)</td></tr>
</table>


<!--

Method parameter table skeleton

<table>
<tr>
<th>Parameter name</th>
<th>Required</th>
<th>Description</th>
</tr>
</table>

-->



<h3 id="apiauthenticate">api.authenticate</h3>
<table>
<tr>
<th>Parameter name</th>
<th>Required</th>
<th>Description</th>
</tr>
<tr><td>username</td><td>Yes</td><td>E-Mail of the Spotify account</td></tr>
<tr><td>password</td><td>Yes</td><td>Password of the Spotify acccount</td></tr>
<tr><td>secret</td><td>Yes</td><td>Signals the RestAPI to decode</td></tr>
</table>

<p>Hashing info: SHA512 the username and the password</p>

```javascript
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       var response = xhttp.responseText;
    }
};
xhttp.open("GET", "http://IPADDRESS:4090?method=api.authenticate&username=[HASHED]&password=[HASHED]", true);
xhttp.send();
```

<br>


<h3 id="apiauthenticateinsecure">api.authenticateinsecure</h3>
<table>
<tr>
<th>Parameter name</th>
<th>Required</th>
<th>Description</th>
</tr>
<tr><td>username</td><td>Yes</td><td>E-Mail of the Spotify account</td></tr>
<tr><td>password</td><td>Yes</td><td>Password of the Spotify acccount</td></tr>
</table>

```javascript
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       var response = xhttp.responseText;
    }
};
xhttp.open("GET", "http://IPADDRESS:4090?method=api.authenticate&username=[VALUE]&password=[VALUE]", true);
xhttp.send();
```

<br>


<h3 id="apigetloginstatus">api.getloginstatus</h3>

<p>Example response: <code>{"Response":"loggedIn"}</code></p>

```javascript
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       var response = xhttp.responseText;
    }
};
xhttp.open("GET", "http://IPADDRESS:4090?method=api.getloginstatus", true);
xhttp.send();
```

<br>


<h3 id="apigetkey">api.getkey</h3>

<p>Response: Bearer token for Spotify</p>

```javascript
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       var response = xhttp.responseText;
    }
};
xhttp.open("GET", "http://IPADDRESS:4090?method=api.getkek", true);
xhttp.send();
```