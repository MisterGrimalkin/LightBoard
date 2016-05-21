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
    private OldScene currentOldScene = null;

    private Map<Integer, Integer> scenePointers = new HashMap<>();
    private Map<Integer, OldScene> scenes = new HashMap<>();

    public void addScene(int id, OldScene oldScene) {
        addScene(id, oldScene, null, false);
    }
    public void addScene(int id, OldScene oldScene, Integer duration, boolean includeInCycle) {
        scenePointers.put(scenePointers.size(), id);
        scenes.put(id, oldScene);
        oldScene.setSceneDuration(duration);
        oldScene.setIncludeInCycle(includeInCycle);
        oldScene.build();
    }

    public Integer getCurrentSceneId() {
        return currentSceneId;
    }

    public Map<Integer, OldScene> getScenes() {
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
            if (currentOldScene == null || !currentOldScene.isBlocking()) {
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
        OldScene newOldScene = scenes.get(id);
        if ( newOldScene !=null ) {
//            surface.clearSurface();
            if ( skipIfNotInCycle && !newOldScene.isIncludeInCycle() ) {
                advanceScene();
            } else {
                if (currentOldScene != null) {
                    currentOldScene.pause();
                }
                currentOldScene = newOldScene;
                currentOldScene.resume();
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
        for ( OldScene oldScene : scenes.values() ) {
            oldScene.start();
            oldScene.pause();
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
                if (currentOldScene != null && currentOldScene.getSceneDuration() != null
                        && now - sceneLoaded >= currentOldScene.getSceneDuration() && !sleeping) {
                    advanceScene();
                }
            }
        }, 0, 5000);
    }

    private boolean sleeping = false;

    public void sleep() {
        System.out.println("Sleep");
        board.sleep();
        currentOldScene.pause();
        sleeping = true;
    }

    public void wake() {
        System.out.println("Wake");
        loadTimes();
        board.wake();
        currentOldScene.resume();
        sleeping = false;
    }
}
