package net.amarantha.lightboard.scene;

import net.amarantha.lightboard.entity.Domino;
import net.amarantha.lightboard.zone.AbstractZone;
import org.w3c.dom.Node;

import static net.amarantha.lightboard.entity.Domino.IN;
import static net.amarantha.lightboard.entity.Domino.OUT;

public class DominoFactory extends XMLParser {

    public void apply(AbstractScene scene, Node dominoNode) {
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
            Domino targetAction = dominoConfig.targetZoneAction.equals("in") ? IN : OUT;

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
                    && targetZoneIds!=null && targetZoneIds.length>0 && targetZoneAction!=null && !targetZoneAction.isEmpty();
        }
    }

}
