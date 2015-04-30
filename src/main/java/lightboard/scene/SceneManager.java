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
        scenePointers.put(scenePointers.size(), id);
        scenes.put(id, scene);
        scene.build();
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
            loadScene(scenePointers.get(scenePointer));
        }
    }

    public static boolean loadScene(Integer id) {
        Scene newScene = scenes.get(id);
        if ( newScene!=null ) {
            if ( currentScene!=null ) {
                currentScene.pause();
            }
            currentScene = newScene;
            currentScene.resume();
            if ( scenePointer==null ) {
                for (Map.Entry<Integer,Integer> entry : scenePointers.entrySet() ) {
                    if ( entry.getValue().equals(id) ) {
                        scenePointer = entry.getKey();
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static void startScenes() {
        scenes.values().forEach((scene) -> { scene.start(); scene.pause(); });
    }

    public static void cycleScenes(int time) {
        cycleScenes((long)time);
    }

    public static void cycleScenes(Long time) {
        startScenes();
        advanceScene();
        Sync.addTask(new Sync.Task(time) {
            @Override
            public void runTask() {
                if ( !sleeping ) {
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
