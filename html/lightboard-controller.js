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

var requestCount = 0;

function scanLightBoards() {

    hide("boardDetail");

    requestCount = 255;
    for ( var i=0; i<=requestCount; i++ ) {
        pingLightBoard(i);
    }

}

function pingLightBoard(ipLSB) {

    get("192.168.0."+ipLSB, "lightboard/system/name",
        function(req) {
            if ( baseUrl==="" ) {
                baseUrl = "192.168.0."+ipLSB;
                clearChildren("lightBoardIp");
                addChild("lightBoardIp",
                    createWithText("H4", "Connected to \'" + req.responseText + "\' on " + baseUrl)
                );
                show("boardDetail");
                loadScenes();
                loadColours();
            }
        },
        function(req) {
            if ( --requestCount==0 && baseUrl==="" ) {
                clearChildren("lightBoardIp").appendChild(createWithText("H4", "No active LightBoards found"));
            }
        }
    );

}


////////////
// Scenes //
////////////

function loadScenes() {

    var panel = clearChildren("sceneList");

    get(baseUrl, "lightboard/scene",
        function(req) {
            var json = JSON.parse(req.responseText);
            element("cycleMode").checked = json.cycleMode;
            var scenes = json.scenes;
            for ( var i=0; i<scenes.length; i++ ) {
                var scene = scenes[i];

                var btn = createWithText("BUTTON", scene.sceneName);
                btn.id="scene"+baseUrl+scene.sceneId;
                btn.className = "btn-primary";
                btn.style.width = "90%";
                btn.onclick = createLoadSceneFunction(scene.sceneId);

                var c = document.createElement("INPUT");
                c.type="checkbox";
                c.style.float = "right";
                c.style.margin = "12px 0 0 0";
                c.checked = scene.inCycle;
                c.onchange = createCycleSceneFunction(scene.sceneId, c);

                var p = document.createElement("P");
                p.appendChild(btn);
                p.appendChild(c);
                panel.appendChild(p);
            }
        },
        errorInElement(panel, "Could not load scenes")
    );
}

function loadScene(id) {
    post(baseUrl, "lightboard/scene/load?id="+id, null, errorAlert("Could not change scene"));
}

function cycleScene(id, cycle) {
    post(baseUrl, "lightboard/scene/cycle?id=" + id + "&cycle=" + cycle, null, errorAlert("Could not change scene cycle state"));
}

function sendCycleMode() {
    var cycle = element("cycleMode").checked;
    if ( cycle ) {
        post(baseUrl, "lightboard/scene/cycle-on", null, errorAlert("Could not activate cycle mode"));
    } else {
        post(baseUrl, "lightboard/scene/cycle-off", null, errorAlert("Could not deactivate cycle mode"));
    }
}

function createLoadSceneFunction(id) {
    return function() { loadScene(id); }
}

function createCycleSceneFunction(sceneId, checkbox) {
    return function() { cycleScene(sceneId, checkbox.checked); }
}


////////////////////
// Scene Tracking //
////////////////////

function changeTrackScene() {
    if ( !element("trackScene").checked ) {
        var oldBtn = element("scene"+baseUrl+currentScene);
        if ( oldBtn != null ) {
            oldBtn.className = "btn-primary";
        }
    }
}

function updateCurrentScene() {
    if ( element("trackScene").checked ) {
        get(baseUrl, "lightboard/scene/current",
            function(req) {
                var oldBtn = element("scene"+baseUrl+currentScene);
                if ( oldBtn != null ) {
                    oldBtn.className = "btn-primary";
                }
                var newBtn = element("scene"+baseUrl+req.responseText);
                currentScene = req.responseText;
                if ( newBtn != null ) {
                    newBtn.className = "btn-success";
                }
            },
            null
        );
    }
}


//////////////////
// Colour Modes //
//////////////////

function loadColours() {
    var panel = clearChildren("colourList");
    get(baseUrl, "lightboard/colour",
        function(req) {
            var json = JSON.parse(req.responseText);
            var colours = json.colours;
            for ( var i=0; i<colours.length; i++ ) {

                var colour = colours[i];

                var btn = document.createElement("BUTTON");
                btn.className = "btn-primary";
                btn.style.width = "100%";
                btn.style.background = colour.name;
                btn.onclick = createChangeColourFunction(colour.name);

                var t = document.createTextNode(colour.name);
                btn.appendChild(t);

                var p = document.createElement("P");
                p.appendChild(btn);
                panel.appendChild(p);
            }
        },
        errorInElement(panel, "Could not load colours")
    );
}


function changeColour(colour) {
    post(baseUrl, "lightboard/colour?name="+colour, null, errorAlert("Could not change colour"));
}

function createChangeColourFunction(name) {
    return function() { changeColour(name); }
}


///////////////////////
// Control Functions //
///////////////////////

function postMessage() {
    var msg = element("messageText").value;
    post(baseUrl, "lightboard/message", null, errorAlert("Could not post message"), msg);
}

function wake() {
    post(baseUrl, "lightboard/system/wake", null, errorAlert("Could not wake LightBoard"));
}

function sleep() {
    post(baseUrl, "lightboard/system/sleep", null, errorAlert("Could not put LightBoard to sleep"));
}

function shutdown() {
    post(baseUrl, "lightboard/system/shutdown",
        function(req) {
            setTimeout(function() { location.reload(); }, 3000);
        },
        errorAlert("Could not shutdown LightBoard")
    );
}
