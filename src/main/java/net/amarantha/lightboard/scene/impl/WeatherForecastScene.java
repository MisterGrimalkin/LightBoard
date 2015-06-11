package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.TextZone;

public class WeatherForecastScene extends Scene {

    public WeatherForecastScene(LightBoardSurface surface) {
        super(surface, "Weather");
    }

    @Override
    public void build() {
        TextZone temp = TextZone.fixed(getSurface());
        temp.setScrollTick(25);
        temp.addMessage(0, "Latest Report from MET Office:\nWeather will Happen. Probably.");

        registerZones(temp);
    }
}
