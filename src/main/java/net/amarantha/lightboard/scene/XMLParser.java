package net.amarantha.lightboard.scene;

import net.amarantha.lightboard.entity.Tag;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    protected interface Callback {
        void call(Tag tag, Node node);
    }

    ///////////////
    // Iterators //
    ///////////////

    protected void iterateAttributes(Node parentNode, Callback callback) {
        NamedNodeMap attr = parentNode.getAttributes();
        for ( int i=0; i<attr.getLength(); i++ ) {
            Node node = attr.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    protected void iterateChildren(Node parentNode, Callback callback) {
        NodeList children = parentNode.getChildNodes();
        for ( int i=0; i<children.getLength(); i++ ) {
            Node node = children.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    protected void iterateElements(Element parentElement, Tag tag, Callback callback) {
        NodeList children = parentElement.getElementsByTagName(tag.getName());
        for ( int i=0; i<children.getLength(); i++ ) {
            Node node = children.item(i);
            String nodeName = node.getNodeName();
            callback.call(Tag.getByName(nodeName), node);
        }
    }

    ///////////////
    // Utilities //
    ///////////////

    protected String stringValue(Node node) {
        String value = applySpecialConstants(node.getTextContent());
        for ( Map.Entry<String, String> entry : constants.entrySet() ) {
            value = value.replaceAll(":"+entry.getKey(), entry.getValue());
        }
        return value;
    }

    protected int integerValue(Node node) {
        Integer result;
        try {
            result = Integer.parseInt(stringValue(node));
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    protected double doubleValue(Node node) {
        Double result;
        try {
            result = Double.parseDouble(stringValue(node));
        } catch (NumberFormatException e) {
            result = 0.0;
        }
        return result;
    }

    ///////////////
    // Constants //
    ///////////////

    private Map<String, String> constants = new HashMap<>();

    protected void setConstant(String name, String value) {
        constants.put(name, value);
    }

    protected String getConstant(String name) {
        return constants.get(name);
    }

    /**
     * Override this to apply special constant logic
     */
    protected String applySpecialConstants(String value) {
        return value;
    }

}
