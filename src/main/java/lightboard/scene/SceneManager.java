package lightboard.scene;

import lightboard.util.Sync;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static Integer scenePointer = null;
    private static Scene currentScene = null;

    private static Map<Integer, Integer> scenePointers = new HashMap<>();
    private static Map<Integer, Scene> scenes = new HashMap<>();

    public static void addScene(int id, Scene scene) {
        addScene(id, scene, null, false);
    }
    public static void addScene(int id, Scene scene, Integer duration, boolean includeInCycle) {
        scenePointers.put(scenePointers.size(), id);
        scenes.put(id, scene);
        scene.setSceneDuration(duration);
        scene.setIncludeInCycle(includeInCycle);
        scene.build();
    }

    public static Map<Integer, Scene> getScenes() {
        return scenes;
    }

    public static void reloadScene() {
        loadScene(scenePointers.get(scenePointer));
    }

    public static void advanceScene() {
        if ( scenes.isEmpty() ) {
            throw new IllegalStateException("Must specify at least one scene");
        }
        if ( scenePointer==null ) {
            scenePointer = 1;
        }
        if ( currentScene==null || ! currentScene.isBlocking() ) {
            scenePointer++;
            if (scenePointer >= scenePointers.size()) {
                scenePointer = 1;
            }
            loadScene(scenePointers.get(scenePointer), true);
        }
    }
    public static boolean loadScene(Integer id) {
        return loadScene(id, false);
    }

    public static boolean loadScene(Integer id, boolean skipIfNotInCycle) {
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

    private static long sceneLoaded;

    public static void startScenes() {
        for ( Scene scene : scenes.values() ) {
            scene.start();
            scene.pause();
        }
        advanceScene();
    }

    public static void cycleScenes() {
        Sync.addTask(new Sync.Task(1000L) {
            @Override
            public void runTask() {
                long now = System.currentTimeMillis();
                if ( currentScene.getSceneDuration()!=null && now-sceneLoaded >= currentScene.getSceneDuration() && !sleeping ) {
                    advanceScene();
                }
            }
        });
    }

    private static boolean sleeping = false;

    public static void sleep() {
        currentScene.pause();
    }

    public static void wake() {
        currentScene.resume();
    }
}
