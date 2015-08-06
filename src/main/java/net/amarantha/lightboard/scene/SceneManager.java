package net.amarantha.lightboard.scene;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class SceneManager {

    private Integer scenePointer = null;
    private Integer currentSceneId = 0;
    private Scene currentScene = null;

    private Map<Integer, Integer> scenePointers = new HashMap<>();
    private Map<Integer, Scene> scenes = new HashMap<>();

    public void addScene(int id, Scene scene) {
        addScene(id, scene, null, false);
    }
    public void addScene(int id, Scene scene, Integer duration, boolean includeInCycle) {
        scenePointers.put(scenePointers.size(), id);
        scenes.put(id, scene);
        scene.setSceneDuration(duration);
        scene.setIncludeInCycle(includeInCycle);
        scene.build();
    }

    public Integer getCurrentSceneId() {
        return currentSceneId;
    }

    public Map<Integer, Scene> getScenes() {
        return scenes;
    }

    public void reloadScene() {
        loadScene(scenePointers.get(scenePointer));
    }

    public void advanceScene() {
        if ( cycleMode ) {
            if (scenes.isEmpty()) {
                throw new IllegalStateException("Must specify at least one scene");
            }
            if (scenePointer == null) {
                scenePointer = 1;
            }
            if (currentScene == null || !currentScene.isBlocking()) {
                scenePointer++;
                if (scenePointer >= scenePointers.size()) {
                    scenePointer = 1;
                }
                loadScene(scenePointers.get(scenePointer), true);
            }
        }
    }
    public boolean loadScene(Integer id) {
        return loadScene(id, false);
    }

    public boolean loadScene(Integer id, boolean skipIfNotInCycle) {
        currentSceneId = id;
        Scene newScene = scenes.get(id);
        if ( newScene!=null ) {
            if ( skipIfNotInCycle && !newScene.isIncludeInCycle() ) {
                advanceScene();
            } else {
                if (currentScene != null) {
                    currentScene.pause();
                }
                currentScene = newScene;
                currentScene.resume();
                if (scenePointer == null) {
                    for (Map.Entry<Integer, Integer> entry : scenePointers.entrySet()) {
                        if (entry.getValue().equals(id)) {
                            scenePointer = entry.getKey();
                        }
                    }
                }
                sceneLoaded = System.currentTimeMillis();
                return true;
            }
        }
        return false;
    }

    private long sceneLoaded;

    public void startScenes() {
        for ( Scene scene : scenes.values() ) {
            scene.start();
            scene.pause();
        }
        advanceScene();
    }

    private boolean cycleMode = false;

    public boolean getCycleMode() {
        return cycleMode;
    }

    public void setCycleMode(boolean mode) {
        cycleMode = mode;
    }


    public void cycleScenes() {
        cycleMode = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                if (currentScene != null && currentScene.getSceneDuration() != null
                        && now - sceneLoaded >= currentScene.getSceneDuration() && !sleeping) {
                    advanceScene();
                }
            }
        }, 0, 5000);
    }

    private boolean sleeping = false;

    public void sleep() {
        currentScene.pause();
        sleeping = true;
    }

    public void wake() {
        currentScene.resume();
        sleeping = false;
    }
}
