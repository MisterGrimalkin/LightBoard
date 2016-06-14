package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.module.Zone;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.AbstractZone;
import net.amarantha.lightboard.zone.MessageGroup;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractScene {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Inject private Sync sync;
    @Inject private SceneLoader sceneLoader;

    public abstract void build();

    private long tick = 20;

    public void setTick(long tick) {
        this.tick = tick;
    }

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
        if ( !paused ) {
            long now = System.currentTimeMillis();
            for (Entry<String, AbstractZone> zoneEntry : zones.entrySet()) {
                AbstractZone zone = zoneEntry.getValue();
                if (!zone.isStandalone() && now - zoneLastTicked.get(zoneEntry.getKey()) > zone.getTick()) {
                    zone.tick();
                }
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
        for ( Entry<String, MessageGroup> entry : groups.entrySet() ) {
            entry.getValue().saveMessages();
        }
    }

    private Map<String, MessageGroup> groups = new HashMap<>();

    public void registerGroup(MessageGroup group) {
        if ( groups.get(group.getId())!=null ) {
            throw new IllegalStateException("Duplicate Group ID");
        }
        groups.put(group.getId(), group);
    }

    public MessageGroup getGroup(String id) {
        return groups.get(id);
    }

    public Collection<MessageGroup> getGroups() {
        return groups.values();
    }

    public void exitScene() {
        try {
            sceneLoader.skip();
        } catch (XMLSceneException e) {
            e.printStackTrace();
        }
    }
}