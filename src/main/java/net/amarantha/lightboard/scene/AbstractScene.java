package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.AbstractZone;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AbstractScene {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Inject private Sync sync;

    // Not actually abstract, but must be implemented for non-XML scenes
    public void build() {}

    private long tick = 20;

    public void init() {
        build();
        try {
            Field[] fields = getClass().getDeclaredFields();
            for ( Field field : fields ) {
                if ( field.isAnnotationPresent(Zone.class) ) {
                    field.setAccessible(true);
                    registerZone((AbstractZone)field.get(this));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        sync.addTask(new Sync.Task(tick) {
//            @Override
//            public void runTask() {
//                if ( !paused ) {
//                    tick();
//                }
//
//            }
//        });

    }

    public void start() {
        paused = false;
        for ( Entry<String, AbstractZone> zoneEntry : zones.entrySet() ) {
            if ( zoneEntry.getValue().isAutoStart() ) {
                zoneEntry.getValue().in();
            }
        }
    }

    public void tick() {
        long now = System.currentTimeMillis();
        for ( Entry<String, AbstractZone> zoneEntry : zones.entrySet() ) {
            AbstractZone zone = zoneEntry.getValue();
            if ( !zone.isStandalone() && now - zoneLastTicked.get(zoneEntry.getKey()) > zone.getTick() ) {
                zone.tick();
            }
        }
    }

    private Map<String, AbstractZone> zones = new HashMap<>();
    private Map<String, Long> zoneLastTicked = new HashMap<>();

    private boolean paused = true;

    public void registerZone(AbstractZone zone) {
        if ( zones.get(zone.getId())!=null ) {
            throw new IllegalStateException("Duplicate Zone ID");
        }
        zones.put(zone.getId(), zone);
        zoneLastTicked.put(zone.getId(), System.currentTimeMillis());
        zone.init();
    }

    public AbstractZone getZone(String id) {
        return zones.get(id);
    }

    public void stop() {
        paused = true;
        for ( Entry<String, AbstractZone> entry : zones.entrySet() ) {
            entry.getValue().pause();
        }
    }

}
