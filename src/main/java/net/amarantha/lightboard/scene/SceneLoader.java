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
import net.amarantha.lightboard.zone.ImageZone;
import net.amarantha.lightboard.zone.MessageGroup;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.AbstractTransition;
import net.amarantha.lightboard.zone.transition.Scroll;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.amarantha.lightboard.entity.Tag.*;

@Singleton
public class SceneLoader extends XMLParser {

    private AbstractScene currentScene;
    private AbstractScene lastScene;

    @Inject private Sync sync;
    @Inject private Injector injector;
    @Inject private LightBoardProperties props;
    @Inject private LightBoardSurface surface;
    @Inject private DominoFactory dominoFactory;

    public AbstractScene getCurrentScene() {
        return currentScene;
    }

    private long tick = 5;

    public void start() {
        surface.clearSurface();
        if ( currentScene==null ) {
            loadScene(props.getDefaultScene());
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
        if ( currentScene!=null ) {
            currentScene.stop();
        }
    }

    public void skip() {
        if ( lastScene!=null ) {
            startScene(lastScene);
        }
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
            startScene(scene);
            return true;
        }
    }

    private void startScene(AbstractScene scene) {
        if ( currentScene!=null ) {
            currentScene.stop();
        }
        lastScene = currentScene;
        currentScene = scene;
        surface.clearSurface();
        currentScene.init();
        currentScene.start();
    }

    private AbstractScene loadSceneFromFile(String filename) {

        AbstractScene scene = injector.getInstance(XMLScene.class);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Element sceneElement = db.parse(filename).getDocumentElement();

            iterateElements(sceneElement, DEFINE, (tag,node)->{
                Node nameAttr = node.getAttributes().getNamedItem(NAME.getName());
                if ( nameAttr!=null ) {
                    String name = nameAttr.getTextContent();
                    String value = node.getTextContent();
                    setConstant(name, value);
                }
            });

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

            iterateElements(sceneElement, GROUP, (tag,node)-> applyGroup(scene, node));

            iterateElements(sceneElement, DOMINO, (tag,node)-> dominoFactory.apply(scene, node));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }

        return scene;
    }

    private void applyGroup(AbstractScene scene, Node groupNode) {
        Node attrNode = groupNode.getAttributes().getNamedItem(ID.getName());
        if ( attrNode!=null ) {
            String zoneIdsStr = stringValue(groupNode);
            String[] zoneIds = zoneIdsStr.split(",");
            MessageGroup group = new MessageGroup(zoneIds);
            group.setId(attrNode.getTextContent());
            scene.registerGroup(group);
            for ( String id : zoneIds ) {
                TextZone zone = (TextZone)scene.getZone(id);
                zone.setGroup(group);
            }
        }
    }

    /////////////////
    // Applicators //
    /////////////////

    private void apply(AbstractZone zone, Node zoneNode) {

        iterateAttributes(zoneNode, (tag,node) -> {
            switch ( tag ) {
                case ID:
                    zone.setId(stringValue(node));
                    break;
                case SRC:
                    if ( zone instanceof ImageZone ) {
                        ((ImageZone)zone).setImage(stringValue(node));
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
                        ((TextZone)zone).addMessage(stringValue(node));
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
        switch ( stringValue(node) ) {
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
        switch ( stringValue(node) ) {
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
        switch ( stringValue(node) ) {
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

    private void applyTransition(AbstractZone zone, Node transitionNode, boolean in) {
        Class<? extends AbstractTransition> transitionClass = null;
        String className = props.getTransitionPackage()+"."+stringValue(transitionNode);
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
                            switch ( stringValue(node) ) {
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
                    zone.setOffsetX(integerValue(node));
                    break;
                case Y:
                    zone.setOffsetY(integerValue(node));
                    break;
            }
        });
    }

    private void applyFont(TextZone zone, Node fontNode) {
        String className = props.getFontPackage()+"."+stringValue(fontNode);
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

    @Override
    protected String applySpecialConstants(String value) {
        if ( value.contains(":webStatus") ) {
            String ip = props.getIp();
            if ( ip==null || ip.isEmpty() ) {
                ip = "{red}WEB SERVICE OFFLINE";
            } else {
                ip = "{red}WEB SERVICE {green}ONLINE {red}at {yellow}" + ip;
            }
            value = value.replaceAll(":webStatus", ip);
        }
        return value;
    }

}

