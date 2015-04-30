package lightboard.scene;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;

public class WeatherForecastScene extends Scene {

    public WeatherForecastScene(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public void build() {
        TextZone temp = TextZone.fixed(getSurface());
        temp.setScrollTick(25);
        temp.addMessage(0, "This Scene is for Weather Data");

        registerZones(temp);
    }
}
