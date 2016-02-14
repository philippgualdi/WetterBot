package at.gualdi.test;

import java.util.Iterator;
import java.util.List;

import javax.json.JsonObject;

import org.json.JSONObject;
import org.json.simple.JSONValue;

import at.gualdi.objects.Wetterinformation;
import at.gualdi.service.JsonConfig;
import at.gualdi.service.JsonLoader;

public class JSONBotTester {

	public static void main(String[] args){
		String url = "http://api.wetter.com/forecast/weather/city/ATXXX0119/project/weatherbot/cs/019fec1fcb4b461f3104c6913b1ea626/output/json";
		JsonLoader loader = JsonLoader.getInstance();
		JSONObject down = loader.getJsonFromUrl(url);
		System.out.println(down.toString());
		List<Wetterinformation> wetter = loader.ladeWetterVonJsonTag(down);
	}
}
