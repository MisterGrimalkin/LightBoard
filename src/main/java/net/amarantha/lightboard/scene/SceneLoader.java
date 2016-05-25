package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.amarantha.lightboard.entity.*;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.AbstractZone;
import net.amarantha.lightboard.zone.AbstractZone.Domino;
import net.amarantha.lightboard.zone.ImageZone;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.AbstractTransition;
import net.amarantha.lightboard.zone.transition.Scroll;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.amarantha.lightboard.entity.Tag.*;

@Singleton
public class SceneLoader {

    private AbstractScene currentScene;

    @Inject private Sync sync;
    @Inject private Injector injector;
    @Inject private LightBoardProperties props;
    @Inject private LightBoardSurface surface;

    public AbstractScene getCurrentScene() {
        return currentScene;
    }

    private long tick = 10;

    public void start() {
        surface.clearSurface();
        if ( currentScene==null ) {
            loadScene("splash");
        } else {
            currentScene.start();
        }
        sync.addTask(new Sync.Task(tick) {
            @Override
            public void runTask() {
                if ( currentScene!=null ) {
                    currentScene.tick();
                }
            }
        });
    }

    public void stop() {
        currentScene.stop();
    }

    ////////////
    // Parser //
    ////////////

    public boolean loadScene(String name) {
        AbstractScene scene = loadSceneFromFile("scenes/"+name+".xml");
        if ( scene==null ) {
            return false;
        } else {
            scene.setName(name);
            if ( currentScene!=null ) {
                currentScene.stop();
            }
            currentScene = scene;
            surface.clearSurface();
            currentScene.init();
            currentScene.start();
            return true;
        }
    }

    private AbstractScene loadSceneFromFile(String filename) {

        AbstractScene scene = injector.getInstance(AbstractScene.class);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Element sceneElement = db.parse(filename).getDocumentElement();

            iterateElements(sceneElement, TEXT_ZONE, (tag, node)->{
                TextZone zone = injector.getInstance(TextZone.class);
                apply(zone, node);
                scene.registerZone(zone);
            });

            iterateElements(sceneElement, IMAGE_ZONE, (tag, node)->{
                ImageZone zone = injector.getInstance(ImageZone.class);
                apply(zone, node);
                scene.registerZone(zone);
            });

            iterateElements(sceneElement, DOMINO, (tag,node)-> setDomino(scene, node));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }

        return scene;
    }

    /////////////////
    // Applicators //
    /////////////////

    private void apply(AbstractZone zone, Node zoneNode) {

        iterateAttributes(zoneNode, (tag,node) -> {
            switch ( tag ) {
                case ID:
                    zone.setId(node.getTextContent());
                    break;
                case SRC:
                    if ( zone instanceof ImageZone ) {
                        ((ImageZone)zone).setImage(node.getTextContent());
                    }
                    break;
            }
        });

        iterateChildren(zoneNode, (tag,node)->{
            switch ( tag ) {
                case FONT:
                    if ( zone instanceof TextZone ) {
                        applyFont((TextZone)zone, node);
                    }
                    break;
                case MESSAGE:
                    if ( zone instanceof TextZone ) {
                        String message = node.getTextContent();
                        message = applyVariables(message);
                        ((TextZone)zone).addMessage(message);
                    }
                    break;

                case REGION:
                    applyRegion(zone, node);
                    break;
                case OFFSET:
                    applyOffset(zone, node);
                    break;

                case IN_TRANSITION:
                    applyTransition(zone, node, true);
                    break;
                case OUT_TRANSITION:
                    applyTransition(zone, node, false);
                    break;
                case DISPLAY_TIME:
                    zone.setDisplayTime(integerValue(node));
                    break;

                case ALIGN_V:
                    applyAlignV(zone, node);
                    break;
                case ALIGN_H:
                    applyAlignH(zone, node);
                    break;

                case CANVAS_LAYER:
                    zone.setCanvasLayer(integerValue(node));
                    break;
                case OUTLINE:
                    applyOutline(zone, node);
                    break;

                case AUTO_START:
                    zone.setAutoStart(true);
                    break;
                case AUTO_OUT:
                    zone.setAutoOut(true);
                    break;
                case AUTO_NEXT:
                    zone.setAutoNext(true);
                    break;

                case TICK:
                    zone.setTick(integerValue(node));
                    break;
//                case STANDALONE:
//                    zone.setStandalone(true);
//                    break;
            }
        });
    }

    private void applyRegion(AbstractZone zone, Node regionNode) {
        final Map<Tag, Integer> region = new HashMap<>();
        iterateAttributes(regionNode, (tag,node)->{
            switch ( tag ) {
                case LEFT:
                    region.put(Tag.LEFT, integerValue(node));
                    break;
                case TOP:
                    region.put(Tag.TOP, integerValue(node));
                    break;
                case WIDTH:
                    region.put(Tag.WIDTH, integerValue(node));
                    break;
                case HEIGHT:
                    region.put(Tag.HEIGHT, integerValue(node));
                    break;
            }
        });
        Integer left = region.get(Tag.LEFT);
        Integer top = region.get(Tag.TOP);
        Integer width = region.get(Tag.WIDTH);
        Integer height = region.get(Tag.HEIGHT);
        if ( left!=null && top!=null && width!=null && height!=null ) {
            zone.setRegion(left, top, width, height);
        }
    }

    private void applyAlignH(AbstractZone zone, Node node) {
        switch ( node.getTextContent() ) {
            case "left":
                zone.setAlignH(AlignH.LEFT);
                break;
            case "middle":
            case "center":
            case "centre":
                zone.setAlignH(AlignH.CENTRE);
                break;
            case "right":
                zone.setAlignH(AlignH.RIGHT);
                break;
        }
    }

    private void applyAlignV(AbstractZone zone, Node node) {
        switch ( node.getTextContent() ) {
            case "top":
                zone.setAlignV(AlignV.TOP);
                break;
            case "middle":
            case "center":
            case "centre":
                zone.setAlignV(AlignV.MIDDLE);
                break;
            case "bottom":
                zone.setAlignV(AlignV.BOTTOM);
                break;
        }
    }

    private void applyOutline(AbstractZone zone, Node node) {
        switch ( node.getTextContent() ) {
            case Colour.RED_STR:
                zone.setOutline(Colour.RED);
                break;
            case Colour.YELLOW_STR:
                zone.setOutline(Colour.YELLOW);
                break;
            case Colour.GREEN_STR:
                zone.setOutline(Colour.GREEN);
                break;
        }
    }

    private String applyVariables(String message) {
        if ( message.contains(":webStatus") ) {
            String ip = props.getIp();
            if ( ip==null || ip.isEmpty() ) {
                ip = "{red}WEB SERVICE OFFLINE";
            } else {
                ip = "{red}WEB SERVICE {green}ONLINE {red}at {yellow}" + ip;
            }
            message = message.replaceAll(":webStatus", ip);
        }
        return message;
    }

    private void applyTransition(AbstractZone zone, Node transitionNode, boolean in) {
        Class<? extends AbstractTransition> transitionClass = null;
        String className = props.getTransitionPackage()+"."+transitionNode.getTextContent();
        try {
            transitionClass = (Class<? extends AbstractTransition>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find Transition " + className);
        }
        if ( transitionClass!=null ) {
            final AbstractTransition transition = injector.getInstance(transitionClass);
            iterateAttributes(transitionNode, (tag,node)->{
                switch ( tag ) {
                    case DURATION:
                        transition.setDuration(integerValue(node));
                        break;
                    case EDGE:
                        if ( transition instanceof Scroll ) {
                            Scroll scr = ((Scroll)transition);
                            switch ( node.getTextContent() ) {
                                case "top":
                                    scr.setEdge(Edge.TOP);
                                    break;
                                case "right":
                                    scr.setEdge(Edge.RIGHT);
                                    break;
                                case "bottom":
                                    scr.setEdge(Edge.BOTTOM);
                                    break;
                                case "left":
                                    scr.setEdge(Edge.LEFT);
                                    break;
                            }
                        }
                }
            });
            if ( in ) {
                zone.setInTransition(transition);
            } else {
                zone.setOutTransition(transition);
            }
        }
    }

    private void applyOffset(AbstractZone zone, Node offsetNode) {
        iterateAttributes(offsetNode, (tag,node) -> {
            switch ( tag ) {
                case X:
                    zone.setOffsetX(Integer.parseInt(node.getTextContent()));
                    break;
                case Y:
                    zone.setOffsetY(Integer.parseInt(node.getTextContent()));
                    break;
            }
        });
    }

    private void applyFont(TextZone zone, Node fontNode) {
        String className = props.getFontPackage()+"."+fontNode.getTextContent();
        Class<? extends Font> fontClass = null;
        try {
            fontClass = (Class<? extends Font>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find Font " + className);
        }
        if ( fontClass!=null ) {
            Font font = injector.getInstance(fontClass);
            zone.setFont(font);
        }
    }

    ///////////////
    // Iterators //
    ///////////////

    private void iterateAttributes(Node parentNode, Callback callback) {
        NamedNodeMap attr = parentNode.getAttributes();
        for ( int i=0; i<attr.getLength(); i++ ) {
            Node node = attr.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    private void iterateChildren(Node parentNode, Callback callback) {
        NodeList children = parentNode.getChildNodes();
        for ( int i=0; i<children.getLength(); i++ ) {
            Node node = children.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    private void iterateElements(Element parentElement, Tag tag, Callback callback) {
        NodeList children = parentElement.getElementsByTagName(tag.getName());
        for ( int i=0; i<children.getLength(); i++ ) {
            Node node = children.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    private interface Callback {
        void call(Tag tag, Node node);
    }

    ///////////////
    // Utilities //
    ///////////////

    private int integerValue(Node node) {
        Integer result = 0;
        try {
            result = Integer.parseInt(node.getTextContent());
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    private double doubleValue(Node node) {
        Double result = 0.0;
        try {
            result = Double.parseDouble(node.getTextContent());
        } catch (NumberFormatException e) {
            result = 0.0;
        }
        return result;
    }

    //////////////
    // Dominoes //
    //////////////

    private void setDomino(AbstractScene scene, Node dominoNode) {
        final DominoConfig dominoConfig = new DominoConfig();
        iterateChildren(dominoNode, (tag,node)->{
            switch (tag) {
                case TRIGGER:
                    dominoConfig.triggerZoneId = node.getTextContent();
                    iterateAttributes(node,(aTag,aNode)->{
                        switch ( aTag ) {
                            case ACTION:
                                dominoConfig.triggerZoneAction = aNode.getTextContent();
                                break;
                            case PROGRESS:
                                dominoConfig.triggerZoneProgress = doubleValue(aNode);
                                break;
                        }
                    });
                case TARGET:
                    dominoConfig.setTargetZoneIds(node.getTextContent());
                    iterateAttributes(node,(aTag,aNode)->{
                        switch ( aTag ) {
                            case ACTION:
                                dominoConfig.targetZoneAction = aNode.getTextContent();
                                break;
                        }
                    });
            }
        });
        if ( dominoConfig.isValid() ) {

            AbstractZone triggerZone = scene.getZone(dominoConfig.triggerZoneId);
            AbstractZone[] targetZones = new AbstractZone[dominoConfig.targetZoneIds.length];
            for ( int i=0; i<targetZones.length; i++ ) {
                targetZones[i] = scene.getZone(dominoConfig.targetZoneIds[i]);
            }
            Domino targetAction = dominoConfig.targetZoneAction.equals("in") ? Domino.IN : Domino.OUT;

            switch ( dominoConfig.triggerZoneAction ) {
                case "in":
                    if ( dominoConfig.triggerZoneProgress==null ) {
                        triggerZone.afterIn(targetAction, targetZones);
                    } else {
                        triggerZone.whenInAt(dominoConfig.triggerZoneProgress, targetAction, targetZones);
                    }
                    break;
                case "display":
                    triggerZone.afterDisplay(targetAction, targetZones);
                    break;
                case "out":
                    if ( dominoConfig.triggerZoneProgress==null ) {
                        triggerZone.afterOut(targetAction, targetZones);
                    } else {
                        triggerZone.whenOutAt(dominoConfig.triggerZoneProgress, targetAction, targetZones);
                    }
                    break;
            }
        }

    }

    private static class DominoConfig {
        String triggerZoneId;
        String triggerZoneAction;
        Double triggerZoneProgress;
        String[] targetZoneIds;
        String targetZoneAction;
        void setTargetZoneIds(String ids) {
            targetZoneIds = ids.split(",");
        }
        boolean isValid() {
            return triggerZoneId!=null && triggerZoneAction!=null && !triggerZoneAction.isEmpty()
                    && targetZoneIds!=null && targetZoneAction!=null && !targetZoneAction.isEmpty();
        }

        @Override
        public String toString() {
            return "DominoConfig{" +
                    "triggerZoneId=" + triggerZoneId +
                    ", triggerZoneAction='" + triggerZoneAction + '\'' +
                    ", triggerZoneProgress=" + triggerZoneProgress +
                    ", targetZoneIds=" + targetZoneIds +
                    ", targetZoneAction='" + targetZoneAction + '\'' +
                    '}';
        }
    }

}

