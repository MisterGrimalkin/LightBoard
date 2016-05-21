package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.AbstractZone;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractScene {

    @Inject private Sync sync;

    public abstract void build();

    private long tick = 20;

    public void init() {
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
        sync.addTask(new Sync.Task(tick) {
            @Override
            public void runTask() {
                if ( !paused ) {
                    tick();
                }

            }
        });

    }

    public void start() {
        paused = false;
        for ( Entry<Integer, AbstractZone> zoneEntry : zones.entrySet() ) {
            if ( zoneEntry.getValue().isAutoStart() ) {
                zoneEntry.getValue().in();
            }
        }
    }

    public void tick() {
        long now = System.currentTimeMillis();
        for ( Entry<Integer, AbstractZone> zoneEntry : zones.entrySet() ) {
            if ( now - zoneLastTicked.get(zoneEntry.getKey()) > zoneEntry.getValue().getTick() ) {
                zoneEntry.getValue().tick();
            }
        }
    }

    private Map<Integer, AbstractZone> zones = new HashMap<>();
    private Map<Integer, Long> zoneLastTicked = new HashMap<>();

    private boolean paused = true;

    private int zoneId = 0;

    public void registerZone(AbstractZone zone) {
        zones.put(zoneId, zone);
        zoneLastTicked.put(zoneId, System.currentTimeMillis());
        zone.init(false);
        zoneId++;
    }

}
