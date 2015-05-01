package lightboard.scene.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;
import lightboard.scene.Scene;

public class WeatherForecastScene extends Scene {

    public WeatherForecastScene(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public void build() {
        TextZone temp = TextZone.fixed(getSurface());
        temp.setScrollTick(25);
        temp.addMessage(0, "Latest Report from MET Office:\nWeather will Happen. Probably.");

        registerZones(temp);
    }
}
