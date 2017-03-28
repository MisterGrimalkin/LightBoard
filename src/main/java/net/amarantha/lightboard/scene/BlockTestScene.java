package net.amarantha.lightboard.scene;

import com.google.inject.Singleton;
import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;

import javax.inject.Inject;

@Singleton
public class BlockTestScene extends AbstractScene {

    @Inject private LightBoardSurface surface;
    @Inject private Sync sync;

    @Override
    public void build() {

        sync.addTask(new Sync.Task(50L) {
            @Override
            public void runTask() {
                animationLoop();
            }
        });
    }

    int blockWidth = 8;
    int blockHeight = 11;

    Colour colour = Colour.RED;
    int xPos = 0;
    int yPos = 0;

    private void animationLoop() {
        drawShape();
    }

    private void drawShape() {
        surface.clearSurface();
        surface.fillRegion(0, surface.safeRegion(xPos*blockWidth, yPos*blockHeight, blockWidth, blockHeight), colour);
    }

    public void tap(int button) {
        int newX = xPos;
        int newY = yPos;
        switch ( button ) {
            case 0:
                newX = 0;
                newY = 0;
                break;
            case 1:
                newX = 1;
                newY = 0;
                break;
            case 2:
                newX = 0;
                newY = 1;
                break;
            case 3:
                newX = 1;
                newY = 1;
                break;
        }
        if ( newX==xPos && newY==yPos ) {
            cycleColour();
        }
        xPos = newX;
        yPos = newY;
    }

    private void cycleColour() {
        if ( colour.equals(Colour.RED) ) {
            colour = Colour.YELLOW;
        } else if ( colour.equals(Colour.YELLOW) ) {
            colour = Colour.GREEN;
        } else {
            colour = Colour.RED;
        }

    }





}
