var baseUrl = "";
var port = "8001";
var boards = ["NONE"];
var currentScene;

function onLoad() {
    scanLightBoards();
    setInterval(function() { updateCurrentScene();}, 2000);
}


//////////////////////////
// Scan for and connect //
// to LightBoards       //
//////////////////////////

function scanLightBoards() {

    boards = new Array();
    baseUrl = "";
    document.getElementById("boardDetail").style.visibility = "hidden";

    var panel = document.getElementById("boardList");
    while (panel.firstChild) {
        panel.removeChild(panel.firstChild);
    }

    for ( var i=0; i<=255; i++ ) {
        pingLightBoard(i);
    }
}

function pingLightBoard(ipLSB) {

    var panel = document.getElementById("boardList");

    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if ( req.readyState==4 && req.status==200 ) {

            var url = "192.168.0."+ipLSB;
            var caption = ( req.responseText=="" ? url : req.responseText );
            var t;
            if ( req.responseText=="" ) {
                t = document.createTextNode(url);
            } else {
                t = document.createTextNode(req.responseText);
            }

            var btn = document.createElement("BUTTON");
            btn.id = url;
            btn.className = "btn-primary";
            btn.style.clear = "left";
            btn.style.margin = "5px";
            btn.style.height = "40px";
            btn.onclick = createLoadBoardFunction(ipLSB, caption);
            btn.appendChild(t);

            panel.appendChild(btn);
            boards.push(url);

        }
    }
    req.open("GET", "http://192.168.0."+ipLSB+":8001/lightboard/system/name", true);
    req.send();
}

function createLoadBoardFunction(ipLSB, name) {
    return function() {

        baseUrl = "192.168.0." + ipLSB;

    	for ( var i = 0; i<boards.length; i++ ) {
            var u = boards[i];
            var b = document.getElementById(u);
            b.className = "btn-primary";
        }

    	var btn = document.getElementById(baseUrl);
	    btn.className = "btn-success";

        document.getElementById("boardDetail").style.visibility = "visible";

        var panel = document.getElementById("boardIP");
        while (panel.firstChild) {
            panel.removeChild(panel.firstChild);
        }
        var t = document.createTextNode("'"+name+"' on "+baseUrl);
        panel.appendChild(t);

        loadScenes();
        loadColours();
        loadBuses();
    }
}


///////////
// Buses //
///////////

function loadBuses() {

    var panel = clearChildren("busList");

    get(baseUrl, "lightboard/bus",
        function(req) {

            var json = JSON.parse(req.responseText);
            var buses = json.buses;

            for ( var i=0; i<buses.length; i++ ) {

                var bus = buses[i];

                var c = document.createElement("INPUT");
                c.id="active"+baseUrl+bus.id;
                c.type="checkbox";
                c.style.width = "10%";
                c.checked = bus.active;
                c.onchange = enableBusFunction(bus.id, c);

                var btn = createWithText("BUTTON", bus.bus + " " + bus.destination);
                btn.id="bus"+baseUrl+bus.id;
                btn.className = "btn-primary";
                btn.style.width = "75%";
                btn.style.height = "40px";
                btn.style.textAlign = "left";
                btn.onclick = showBusDetailsFunction(bus);

                var deleteBtn = createWithText("BUTTON", "X");
                deleteBtn.id="deleteBus"+baseUrl+bus.id;
                deleteBtn.className = "btn-danger";
                deleteBtn.style.width = "15%";
                deleteBtn.style.height = "40px";
                deleteBtn.onclick = removeBusFunction(bus);


                panel.appendChild(c);
                panel.appendChild(btn);
                panel.appendChild(deleteBtn);
            }

            var addBtn = createWithText("BUTTON", "+");
            addBtn.className = "btn-success";
            addBtn.style.width = "15%";
            addBtn.style.height = "40px";
            addBtn.style.float = "right";
            addBtn.onclick = showBusDetailsFunction();

            panel.appendChild(addBtn);



        },
            errorAlert("Could not load buses.")
        );

}

function enableBusFunction(id, checkbox) {
    return function() {
        var action = "disable";
        if ( checkbox.checked ) {
            action = "enable";
        }
        post(baseUrl, "lightboard/bus/"+action+"?id=" + id,
            function() {
                // OK
            },
            function() {
                loadBuses();
            }
        );
    }
}

function showBusDetailsFunction(bus) {
    return function() {
        element("mask").style.display = "block";
        element("addBusPanel").style.display = "block";
        if ( bus ) {
            clearChildren("addBusPanelHeader").appendChild(createWithText("H3", "Edit Bus Stop"));
            editingId = bus.id;
            element("stopCodeField").value = bus.stop;
            element("busField").value = bus.bus;
            element("destinationField").value = bus.destination;
            element("offsetField").value = bus.offset;
        } else {
            clearChildren("addBusPanelHeader").appendChild(createWithText("H3", "Add Bus Stop"));
            editingId = null;
            element("stopCodeField").value = "";
            element("busField").value = "";
            element("destinationField").value = "";
            element("offsetField").value = 0;
        }
    }
}

function closeBusDetails() {
    element("mask").style.display = "none";
    element("addBusPanel").style.display = "none";
    editingId = null;
}

var editingId;

function saveBusDetails() {
    if ( editingId ) {
        post(baseUrl, "lightboard/bus/update?id=" + editingId
            + "&stopCode=" + element("stopCodeField").value
            + "&busNo=" + element("busField").value
            + "&destination=" + element("destinationField").value
            + "&offset=" + element("offsetField").value,
            function() {
                closeBusDetails();
                loadBuses();
            },
            function() {
                window.alert("Could not create bus stop");
            }
        )
    } else {
        post(baseUrl, "lightboard/bus/add?stopCode=" + element("stopCodeField").value
            + "&busNo=" + element("busField").value
            + "&destination=" + element("destinationField").value
            + "&offset=" + element("offsetField").value,
            function() {
                closeBusDetails();
                loadBuses();
            },
            function() {
                window.alert("Could not create bus stop");
            }
        )
    };
}

function removeBusFunction(bus) {
    return function() {
        var reply = window.confirm("Delete bus stop: " + bus.bus + " to " + bus.destination + " (stop " + bus.stop + ")?");
        if ( reply===true ) {
            post(baseUrl, "lightboard/bus/remove?id=" + bus.id,
                function() {
                    loadBuses();
                },
                function() {
                    window.alert("Could not remove bus stop " + bus.id);
                }
            );
        }
    }
}

////////////
// Scenes //
////////////

function loadScenes() {

    var panel = document.getElementById("sceneList");
    while (panel.firstChild) {
        panel.removeChild(panel.firstChild);
    }

    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if ( req.readyState==4 ) {
            if ( req.status==200 ) {

                var json = JSON.parse(req.responseText);
                document.getElementById("cycleMode").checked = json.cycleMode;

                var scenes = json.scenes;
                for ( var i=0; i<scenes.length; i++ ) {

                    var scene = scenes[i];
                    var t = document.createTextNode(scene.sceneName);

                    var btn = document.createElement("BUTTON");
                    btn.id="scene"+baseUrl+scene.sceneId;
                    btn.appendChild(t);
                    btn.className = "btn-primary";
                    btn.style.width = "90%";
                    btn.style.height = "40px";
                    btn.onclick = createLoadSceneFunction(scene.sceneId);

                    var c = document.createElement("INPUT");
                    c.type="checkbox";
                    c.style.float = "right";
                    c.style.margin = "12px 0 0 0";

                    var p = document.createElement("P");
                    p.appendChild(btn);
                    p.appendChild(c);
                    panel.appendChild(p);

                    c.checked = scene.inCycle;
                    c.onchange = createCycleSceneFunction(scene.sceneId, c);

                }
            } else {
                var t = document.createTextNode("Could not load scenes from LightBoard");
                panel.appendChild(t);
            }
        }
    }
    req.open("GET", url("scene"), true);
    req.send();
}

function createLoadSceneFunction(id) {
    return function() {
        loadScene(id);
    }
}

function createCycleSceneFunction(sceneId, checkbox) {
    return function() {
        cycleScene(sceneId, checkbox.checked);
    }
}

function loadScene(id) {
    var req = new XMLHttpRequest();
    req.open("POST", url("scene/load") + "?id=" + id, true);
    req.send();
}


///////////////////
// Scene Cycling //
///////////////////

function cycleScene(id, cycle) {
    var req = new XMLHttpRequest();
    req.open("POST", url("scene/cycle") + "?id=" + id + "&cycle=" + cycle, true);
    req.send();
}

function sendCycleMode() {
    var req = new XMLHttpRequest();
    var cycle = document.getElementById("cycleMode").checked;
    if ( cycle ) {
        req.open("POST", url("scene/cycle-on"), true);
    } else {
        req.open("POST", url("scene/cycle-off"), true);
    }
    req.send();
}


////////////////////
// Scene Tracking //
////////////////////

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
            if ( req.readyState==4 && req.status==200 ) {
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
        req.open("GET", url("scene/current"), true);
        req.send();
    }
}


//////////////////
// Colour Modes //
//////////////////

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

                    var btn = document.createElement("BUTTON");
                    btn.className = "btn-primary";
                    btn.style.width = "100%";
                    btn.style.height = "40px";
                    btn.style.background = colour.name;
                    btn.onclick = createChangeColourFunction(colour.name);

                    var t = document.createTextNode(colour.name);
                    btn.appendChild(t);

                    var p = document.createElement("P");
                    p.appendChild(btn);
                    panel.appendChild(p);
                }
            } else {
                var t = document.createTextNode("Could not load colours from LightBoard");
                panel.appendChild(t);
            }
        }
    }
    req.open("GET", url("colour"), true);
    req.send();
}

function createChangeColourFunction(name) {
    return function() {
        changeColour(name);
    }
}

function changeColour(colour) {
    var req = new XMLHttpRequest();
    req.open("POST", url("colour?name="+colour), true);
    req.send();
}


//////////////
// Messages //
//////////////

function postMessage() {
    var message = document.getElementById('messageText').value;
    var req = new XMLHttpRequest();
    req.open("POST", url("message"), true);
    req.send(message);
}

function postMessageToAll() {
    for ( var i = 0; i<boards.length; i++ ) {
        var url = boards[i];
        var message = document.getElementById('messageText').value;
        var req = new XMLHttpRequest();
        req.open("POST", "http://"+url+":8001/lightboard/message", true);
        req.send(message);
    }
}


///////////////////////
// Control Functions //
///////////////////////

function wake() {
    var req = new XMLHttpRequest();
    req.open("POST", url("system/wake"), true);
    req.send();
}

function sleep() {
    var req = new XMLHttpRequest();
    req.open("POST", url("system/sleep"), true);
    req.send();
}

function shutdown() {
    var reply = window.confirm("Close LightBoard software?");
    if ( reply===true ) {
        var req = new XMLHttpRequest();
        req.onreadystatechange = function() {
            if (req.readyState==4 && req.status==200 ) {
                setTimeout(function() {
                    location.reload();
                }, 3000);
            }
        }
        req.open("POST", url("system/shutdown"), true);
        req.send();
    }
}

function resetServerConnection() {
    var req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        if (req.readyState==4 && req.status==200 ) {
            scanForServer(function() { getServerBoardStatus(); });
            scanLightBoards();
        }
    }
    req.open("POST", url("system/ticket-server"), true);
    req.send();
}


function url(path) {
    return "http://" + baseUrl + ":" + port + "/lightboard/" + path;
}
