package net.amarantha.lightboard.updater.weather;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.Sync;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javalite.http.Get;
import org.javalite.http.Http;

public class WeatherUpdater extends Updater {

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=London";

    @Inject
    protected WeatherUpdater(Sync sync) {
        super(sync);
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
