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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.amarantha.lightboard.entity.Tag.*;

@Singleton
public class SceneLoader extends XMLParser {

    private AbstractScene currentScene;
    private String lastSceneName;

    @Inject private Sync sync;
    @Inject private Injector injector;
    @Inject private LightBoardProperties props;
    @Inject private LightBoardSurface surface;
    @Inject private DominoFactory dominoFactory;

    public AbstractScene getCurrentScene() {
        return currentScene;
    }

    public void start() {
        surface.clearSurface();
        if ( currentScene==null ) {
            try {
                loadScene(props.getDefaultScene());
            } catch (XMLSceneException e) {
                e.printStackTrace();
            }
        } else {
            currentScene.start();
        }
        sync.addTask(new Sync.Task(props.getSceneTick()) {
            @Override
            public void runTask() {
                if (!paused && currentScene != null) {
                    currentScene.tick();
                }
            }
        });
    }

    private boolean paused = false;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        if ( currentScene!=null ) {
            currentScene.stop();
        }
    }

    public void skip() throws XMLSceneException {
        if ( lastSceneName!=null ) {
            loadScene(lastSceneName);
        }
    }

    ////////////
    // Parser //
    ////////////

    public boolean loadScene(String name) throws XMLSceneException {
        AbstractScene scene = loadSceneFromFile("scenes/"+name+".xml");
        if ( scene==null ) {
            return false;
        } else {
            scene.setName(name);
            loadSceneMessages(scene);
            startScene(scene);
            return true;
        }
    }

    public void loadSceneMessages(AbstractScene scene) {
        for ( MessageGroup group : scene.getGroups() ) {
            group.setFilename("scenes/"+scene.getName()+"-"+group.getId()+".json");
            group.loadMessages();
        }
    }

    private void startScene(AbstractScene scene) {
        if ( currentScene!=null ) {
            currentScene.stop();
            if ( !scene.getName().equals(currentScene.getName()) ) {
                lastSceneName = currentScene.getName();
            }
        }
        currentScene = scene;
        surface.clearSurface();
        currentScene.init();
        currentScene.start();
    }

    public List<String> listSceneFiles() {
        List<String> result = new ArrayList<>();
        File dir = new File("scenes");
        if ( dir.isDirectory() ) {
            for ( File file : dir.listFiles() ) {
                String filename = file.getName();
                if ( filename.length()>4 && filename.substring(filename.length()-4).equals(".xml") ) {
                    result.add(filename.substring(0,filename.length()-4));
                }
            }
        }
        return result;
    }

    public AbstractScene loadSceneFromFile(String filename) throws XMLSceneException {

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

    private void applyGroup(AbstractScene scene, Node groupNode) throws XMLSceneException {
        Node attrNode = groupNode.getAttributes().getNamedItem(ID.getName());
        if ( attrNode!=null ) {
            String zoneIdsStr = stringValue(groupNode);
            String[] zoneIds = zoneIdsStr.split(",");
            MessageGroup group = new MessageGroup(zoneIds);
            group.setId(attrNode.getTextContent());
            scene.registerGroup(group);
            for ( String id : zoneIds ) {
                TextZone zone = (TextZone)scene.getZone(id);
                if ( zone==null ) {
                    throw new XMLSceneException("No zone found: " + id);
                }
                zone.setGroup(group);
            }
        } else {
            throw new XMLSceneException("No Group ID Specified");
        }
    }

    /////////////////
    // Applicators //
    /////////////////

    private void apply(AbstractZone zone, Node zoneNode) throws XMLSceneException {

        iterateAttributes(zoneNode, (tag,node) -> {
            switch ( tag ) {
                case ID:
                    zone.setId(stringValue(node));
                    break;
                case SRC:
                    if ( zone instanceof ImageZone ) {
                        ((ImageZone)zone).setImage("images/"+stringValue(node));
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
                case DEFAULT_COLOUR:
                    if ( zone instanceof TextZone ) {
                        ((TextZone)zone).setColourOverride(stringValue(node));
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

    private void applyRegion(AbstractZone zone, Node regionNode) throws XMLSceneException {
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
        } else {
            throw new XMLSceneException("Region incomplete");
        }
    }

    private void applyAlignH(AbstractZone zone, Node node) throws XMLSceneException {
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
            default:
                throw new XMLSceneException("Invalid H-Align value");
        }
    }

    private void applyAlignV(AbstractZone zone, Node node) throws XMLSceneException {
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
            default:
                throw new XMLSceneException("Invalid V-Align value");
        }
    }

    private void applyOutline(AbstractZone zone, Node node) throws XMLSceneException {
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
            default:
                throw new XMLSceneException("Unknown colour in outline");
        }
    }

    private void applyTransition(AbstractZone zone, Node transitionNode, boolean in) throws XMLSceneException {
        Class<? extends AbstractTransition> transitionClass = null;
        String className = props.getTransitionPackage()+"."+stringValue(transitionNode);
        try {
            transitionClass = (Class<? extends AbstractTransition>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new XMLSceneException("Could not find Transition " + className);
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
                        break;
                    case SPEED:
                        if ( transition instanceof Scroll ) {
                            ((Scroll) transition).setSpeed(integerValue(node));
                        }
                        break;
                }
            });
            if ( in ) {
                zone.setInTransition(transition);
            } else {
                zone.setOutTransition(transition);
            }
        }
    }

    private void applyOffset(AbstractZone zone, Node offsetNode) throws XMLSceneException {
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
        String fileToLoad = null;
        try {
            fontClass = (Class<? extends Font>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            fontClass = Font.class;
            fileToLoad = stringValue(fontNode);
        }
        if ( fontClass!=null ) {
            Font font = injector.getInstance(fontClass);
            zone.setFont(font);
            if ( fileToLoad!=null ) {
                font.loadFont(fileToLoad+".dat");
            }
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

