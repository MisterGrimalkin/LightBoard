package net.amarantha.lightboard.entity;

public enum Tag {

    DEFINE("define"),

    TEXT_ZONE("textzone"),
    IMAGE_ZONE("imagezone"),

    DEFAULT_COLOUR("default-colour"),

    GROUP("group"),

    STANDALONE("standalone"),
    TICK("tick"),

    DOMINO("domino"),
    TRIGGER("trigger"),
    ACTION("action"),
    TARGET("target"),
    PROGRESS("progress"),

    OUTLINE("outline"),

    ID("id"),
    NAME("name"),
    SRC("src"),

    ALIGN_H("align-horizontal"),
    ALIGN_V("align-vertical"),

    REGION("region"),
    LEFT("left"),
    TOP("top"),
    WIDTH("width"),
    HEIGHT("height"),

    OFFSET("offset"),
    X("x"),
    Y("Y"),
    CANVAS_LAYER("canvas-layer"),

    IN_TRANSITION("in-transition"),
    OUT_TRANSITION("out-transition"),
    DURATION("duration"),
    EDGE("edge"),

    DISPLAY_TIME("display-time"),

    AUTO_START("auto-start"),
    AUTO_OUT("auto-out"),
    AUTO_NEXT("auto-next"),

    FONT("font"),
    MESSAGE("message"),

    UNKNOWN("unknown"),

    XML_TEXT("#text")
    ;

    private String name;

    Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Tag getByName(String name) {
        for ( Tag tag : values() ) {
            if ( tag.getName().equalsIgnoreCase(name) ) {
                return tag;
            }
        }
//        System.err.println(name);
        return UNKNOWN;
    }

}
