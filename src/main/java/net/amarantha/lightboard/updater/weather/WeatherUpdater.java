package net.amarantha.lightboard.updater.weather;

import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javalite.http.Get;
import org.javalite.http.Http;

public class WeatherUpdater extends Updater {

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London";

    public WeatherUpdater(TextZone zone) {
        super(zone);
    }

    @Override
    public void refresh() {

        Get get = Http.get(URL).doConnect();
        JSONObject json = JSONObject.fromObject(get.text());

        System.out.println(json);
        JSONArray weatherA = (JSONArray)json.get("weather");
        JSONObject weather = (JSONObject)weatherA.get(0);
        System.out.println(weather.get("main"));

    }
}
