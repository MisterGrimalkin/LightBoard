package net.amarantha.lightboard.surface;

import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.util.Sync;

public class SelfTest {

    double testX = 0;
    double testY = 0;
    int testDeltaX = 1;
    int testDeltaY = 1;
    int testBounces = 0;
    boolean testInvert = false;

    private LightBoardSurface surface;

    public SelfTest(LightBoardSurface surface) {
        this.surface = surface;
    }

    public void run() {

        double speed = 0.5;

        final Pattern testPat =
                new Pattern(3,
                        "#-#" +
                                "-#-" +
                                "#-#"
                );

        Sync.addTask(new Sync.Task(20L) {
            @Override
            public void runTask() {

                surface.clearSurface();

                int floorX = (int) Math.floor(testX);
                int floorY = (int) Math.floor(testY);

                surface.fillRegion(surface.safeRegion(0, floorY, surface.getCols(), 1));
                surface.fillRegion(surface.safeRegion(floorX, 0, 1, surface.getRows()));

                surface.outlineRegion(surface.safeRegion(floorX + 2, floorY + 2, 3, 3));
                surface.fillRegion(surface.safeRegion(floorX - 4, floorY - 4, 3, 3));
                surface.clearPoint(floorX - 3, floorY - 3);

                surface.drawPattern(floorX - 4, floorY + 2, testPat);
                surface.drawPattern(floorX + 2, floorY - 4, testPat);

                surface.drawPoint(floorX - 4, floorY - 5);
                surface.drawPoint(floorX - 4, floorY + 5);
                surface.drawPoint(floorX + 4, floorY - 5);
                surface.drawPoint(floorX + 4, floorY + 5);
                surface.drawPoint(floorX - 5, floorY - 4);
                surface.drawPoint(floorX - 5, floorY + 4);
                surface.drawPoint(floorX + 5, floorY - 4);
                surface.drawPoint(floorX + 5, floorY + 4);

                if (testInvert) {
                    surface.invertRegion(surface.boardRegion);
                }

                if (floorX < 0 || floorX >= surface.getCols()) {
                    testX = floorX - testDeltaX;
                    testDeltaX = -testDeltaX;
                    testBounces++;
                }

                if (floorY < 0 || floorY >= surface.getRows()) {
                    testY = floorY - testDeltaY;
                    testDeltaY = -testDeltaY;
                    testBounces++;
                }

                if (testBounces > 9) {
                    testInvert = !testInvert;
                    testBounces = 0;
                }

                testX = testX + (testDeltaX * speed);
                testY = testY + (testDeltaY * speed);

            }
        });

    }
}
