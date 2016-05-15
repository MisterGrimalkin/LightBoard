package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Now;
import net.amarantha.lightboard.utility.PropertyManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static net.amarantha.lightboard.utility.Now.timeOnly;

@Singleton
public class OldSceneManager {

    @Inject private Now now;
    @Inject private PropertyManager props;

    @Inject private LightBoardSurface surface;
    @Inject private LightBoard board;

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
//            surface.clearSurface();
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

    private Date wake;
    private Date sleep;

    public void startScenes() {
        loadTimes();
        for ( Scene scene : scenes.values() ) {
            scene.start();
            scene.pause();
        }
        advanceScene();
    }

    private void loadTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String wakeString = props.getString("wakeTime", "0500");
        String sleepString = props.getString("sleepTime", "0200");
        try {
            wake = sdf.parse(wakeString);
            sleep = sdf.parse(sleepString);
        } catch (ParseException e) {
            wake = null;
            sleep = null;
            e.printStackTrace();
        }
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
                if ( !sleeping && sleep!=null && timeOnly(sleep).equals(now.time()) ) {
                    sleep();
                }
                if ( sleeping && wake!=null && timeOnly(wake).equals(now.time()) ) {
                    wake();
                }
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
        System.out.println("Sleep");
        board.sleep();
        currentScene.pause();
        sleeping = true;
    }

    public void wake() {
        System.out.println("Wake");
        loadTimes();
        board.wake();
        currentScene.resume();
        sleeping = false;
    }
}
