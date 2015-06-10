var baseUrl = "";
var port = "8001";
var boards = ["NONE"];

function onLoad() {
    scan();
    setInterval(function() { updateCurrentScene();}, 2000);
}

var boardCount = 0;

function scan() {
    console.log("Scanning...");
    document.getElementById("boardDetail").style.visibility = "hidden";
    var panel = document.getElementById("boardList");
    while (panel.firstChild) {
        panel.removeChild(panel.firstChild);
    }
    boards = new Array();
    boardCount = 0;
    baseUrl = "";
    for ( var i=0; i<=255; i++ ) {
        pingHealthCheck(i);
    }
    console.log("Scan Complete");
}

function pingHealthCheck(ipLSB) {
    var req = new XMLHttpRequest();
    var panel = document.getElementById("boardList");
    req.onreadystatechange = function() {
        if ( req.readyState==4 ) {
            if ( req.status==200 ) {
                var url = "192.168.0."+ipLSB;
                console.log(url+" responded :)");
                var btn = document.createElement("BUTTON");
                btn.style.clear = "left";
                var t;
                if ( req.responseText=="" ) {
                    t = document.createTextNode(url);
                } else {
                    t = document.createTextNode(req.responseText);
                }
		        btn.id = url;
                btn.onclick = createLoadBoardFunction(ipLSB);
                btn.appendChild(t);
     	        btn.className = "btn-primary";
		    btn.style.margin = "5px";
                document.body.appendChild(btn);
                panel.appendChild(btn);
                boardCount++;
                boards.push(url);
            }
        }
    }
    req.open("GET", "http://192.168.0."+ipLSB+":8001/lightboard/system/name", true);
    req.send();
}

function createLoadBoardFunction(ipLSB) {
    return function() {
        baseUrl = "192.168.0." + ipLSB;
	for ( var i = 0; i<boards.length; i++ ) {
	    var u = boards[i];
	    var b = document.getElementById(u);
	    b.className = "btn-primary";
	}
        var url = boards[i];
	var btn = document.getElementById(baseUrl);
	btn.className = "btn-success";
        document.getElementById("boardDetail").style.visibility = "visible";
        loadScenes();
        loadColours();
    }
}

function loadScenes() {
    var req = new XMLHttpRequest();
    var panel = document.getElementById("sceneList");
    while (panel.firstChild) {
        panel.removeChild(panel.firstChild);
    }
    req.onreadystatechange = function() {
        if (req.readyState==4 ) {
            if ( req.status==200 ) {
                var json = JSON.parse(req.responseText);
                document.getElementById("cycleMode").checked = json.cycleMode;
                var scenes = json.scenes;
                for ( var i=0; i<scenes.length; i++ ) {
                    var scene = scenes[i];
                    var p = document.createElement("P");
                    var btn = document.createElement("BUTTON");
                    var t = document.createTextNode(scene.sceneName);
                    btn.id="scene"+baseUrl+scene.sceneId;
                    btn.appendChild(t);
     	            btn.className = "btn-primary";
                    btn.style.width = "90%";
                    btn.onclick = createLoadSceneFunction(scene.sceneId);
                    var c = document.createElement("INPUT");
                    c.type="checkbox";
                    c.style.float = "right";
                    p.appendChild(btn);
        		    p.appendChild(c);
                    panel.appendChild(p);
		            c.checked = scene.inCycle;
                    c.onchange = createCycleSceneFunction(scene.sceneId, c);
                }
            } else {
                var t = document.createTextNode("ERROR: Cannot Connect to LightBoard!");
                panel.appendChild(t);
            }
        }
    }
    req.open("GET", url("scene"), true);
    req.send();
}

var currentScene;

function changeTrackScene() {
    if ( !document.getElementById("trackScene").checked ) {
        var oldBtn = document.getElementById("scene"+baseUrl+currentScene);
        if ( oldBtn != null ) {
            oldBtn.className = "btn-primary";
        }
    }
}

function updateCurrentScene() {
    if ( document.getElementById("trackScene").checked ) {
        var req = new XMLHttpRequest();
        req.onreadystatechange = function() {
            if ( req.readyState==4 ) {
                if ( req.status==200 ) {
                    var oldBtn = document.getElementById("scene"+baseUrl+currentScene);
                    if ( oldBtn != null ) {
                        oldBtn.className = "btn-primary";
                    }
                    var newBtn = document.getElementById("scene"+baseUrl+req.responseText);
                    currentScene = req.responseText;
                    if ( newBtn != null ) {
                        newBtn.className = "btn-success";
                    }
                }
            }
        }
        req.open("GET", url("scene/current"), true);
        req.send();
    }
}

function createCycleSceneFunction(sceneId, checkbox) {
    return function() {
        cycleScene(sceneId, checkbox.checked);
    }
}

function sendCycleMode() {
    var req = new XMLHttpRequest();
    var cycle = document.getElementById("cycleMode").checked;
    req.onreadystatechange = function() {
        if (req.readyState==4 ) {
            if ( req.status!=200 ) {
                window.alert("Error Setting Cycle Mode");
            }
        }
    }
    if ( cycle ) {
        req.open("POST", url("scene/cycle-on"), true);
    } else {
        req.open("POST", url("scene/cycle-off"), true);
    }
    req.send();
}

function loadColours() {
    var req = new XMLHttpRequest();
    var panel = document.getElementById("colourList");
    while (panel.firstChild) {
        panel.removeChild(panel.firstChild);
    }
    req.onreadystatechange = function() {
        if (req.readyState==4 ) {
            if ( req.status==200 ) {
                var json = JSON.parse(req.responseText);
                var colours = json.colours;
                for ( var i=0; i<colours.length; i++ ) {
                    var colour = colours[i];
                    var p = document.createElement("P");
                    var btn = document.createElement("BUTTON");
                    var t = document.createTextNode(colour.name);
                    btn.onclick = createChangeColourFunction(colour.name);
                    btn.appendChild(t);
		    btn.style.width = "100%";
		    btn.style.background = colour.name;
     	            btn.className = "btn-primary";
		    p.appendChild(btn);
                    panel.appendChild(p);
                }
            } else {
                var t = document.createTextNode("ERROR: Cannot Connect to LightBoard!");
                panel.appendChild(t);
            }
        }
    }
    req.open("GET", url("colour"), true);
    req.send();
}

function createLoadSceneFunction(id) {
    return function() {
        loadScene(id);
    }
}

function createChangeColourFunction(name) {
    return function() {
        changeColour(name);
    }
}

function changeColour(colour) {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("colour?name="+colour), true);
    req.send();
}

function postMessage() {
    var message = document.getElementById('messageText').value;
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("message"), true);
    req.send(message);
}

function postMessageToAll() {
    console.log(boards);
    for ( var i = 0; i<boards.length; i++ ) {
        var url = boards[i];
        var message = document.getElementById('messageText').value;
        var req = new XMLHttpRequest();
        req.onreadystatechange = function() {
            if (req.readyState==4 && req.status!=200 ) {
//                window.alert("Cannot Connect to LightBoard [" + req.status + "]");
            }
        }
        console.log(url);
        req.open("POST", "http://"+url+":8001/lightboard/message", true);
        req.send(message);
    }
}

function loadScene(id) {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("scene/load") + "?id=" + id, true);
    req.send();
}

function cycleScene(id, cycle) {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("scene/cycle") + "?id=" + id + "&cycle=" + cycle, true);
    req.send();
}

function wake() {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("system/wake"), true);
    req.send();
}

function sleep() {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
        }
    }
    req.open("POST", url("system/sleep"), true);
    req.send();
}

function shutdown() {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status!=200 ) {
            window.alert("Cannot Connect to LightBoard [" + req.status + "]");
            scan();
        }
    }
    req.open("POST", url("system/shutdown"), true);
    req.send();
}

function url(path) {
    return "http://" + baseUrl + ":" + port + "/lightboard/" + path;
}
