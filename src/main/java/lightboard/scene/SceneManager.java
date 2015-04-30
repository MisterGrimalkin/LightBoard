package lightboard.scene;

import lightboard.util.Sync;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static int scenePointer = -1;
    private static Scene currentScene = null;

    private static Map<Integer, Integer> scenePointers = new HashMap<>();
    private static Map<Integer, Scene> scenes = new HashMap<>();

    public static void addScene(int id, Scene scene) {
        scenePointers.put(scenePointers.size(), id);
        scenes.put(id, scene);
        scene.build();
    }

    public static void advanceScene() {
        if ( scenes.isEmpty() ) {
            throw new IllegalStateException("Must specify at least one scene");
        }
        if ( currentScene==null || ! currentScene.isBlocking() ) {
            scenePointer++;
            if (scenePointer >= scenePointers.size()) {
                scenePointer = 0;
            }
            loadScene(scenePointers.get(scenePointer));
        }
    }

    public static void loadScene(Integer id) {
        Scene newScene = scenes.get(id);
        if ( newScene!=null ) {
            if ( currentScene!=null ) {
                currentScene.pause();
            }
            currentScene = newScene;
            currentScene.resume();
        }
    }

    public static void cycleScenes(int time) {
        cycleScenes((long)time);
    }

    public static void cycleScenes(Long time) {
        scenes.values().forEach((scene) -> { scene.start(); scene.pause(); });
        advanceScene();
        Sync.addTask(new Sync.Task(time) {
            @Override
            public void runTask() {
                advanceScene();
            }
        });
    }

}
