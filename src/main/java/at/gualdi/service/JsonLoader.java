package at.gualdi.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

import at.gualdi.objects.Wetterinformation;

public class JsonLoader implements JsonConfig{

	private static volatile JsonLoader instanz = null;
	private static final String LOGKLASSE ="JsonLoader";
	
	private JsonLoader(){
	}
	
	/**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static JsonLoader getInstance() {
        final JsonLoader currentInstance;
        if (instanz == null) {
            synchronized (JsonLoader.class) {
                if (instanz == null) {
                    instanz = new JsonLoader();
                    LoggerBot.information(LOGKLASSE, "JsonLoader Objekt wurde erstellt");
                }
                currentInstance = instanz;
            }
        } else {
            currentInstance = instanz;
        }
        return currentInstance;
    }
    
    
    /**
     * Methode versucht eine URL auf zu ruft und in ein String mit Json inhalt. 
     * @param surl String mit URL
     * @return String mit Json inhalt
     */
    private String rufeUrl(String surl){
    	StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		BufferedReader bufferedReader = null;
		try {
			URL url = new URL(surl);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}			
				}
			}
		} catch (Exception e) {
			LoggerBot.error(LOGKLASSE, "rufeUrl(String surl), URL: " + surl, e);
		}finally {
			try {
				in.close();
				bufferedReader.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();
    }
    
    private JSONObject StringToJson(String jsonText){
          JSONObject json = new JSONObject(jsonText);
          return json;
    }
    
    /**
     * Methode um von Url zum JsonBject kommen
     * 
     * @param url
     * @return
     */
    public JSONObject getJsonFromUrl(String url){
    	JSONObject json = null;
    	String jsonText = rufeUrl(url);
    	if(jsonText != null)
    		json = StringToJson(jsonText);
    	return json;
    }
    
    /**
     * Methode Wetterinformationdynamisch von Ort und zugehörige checksumme laden
     * @param ort Standort der Wetterstation
     * @param checksumme 
     * @return
     */
    public  List<Wetterinformation> ladeWetterVonOrt(String ort, String checksumme){
    	List<Wetterinformation> ret = null;
    	LoggerBot.debug(LOGKLASSE, "Methode ladeWetterVonOrt wurde aufgerufen und verarbeitet");
    	String url = JsonLoader.BASEURL + ort + PROJEKT + CHECKSUMME + checksumme + OPTION;
    	JSONObject json = getJsonFromUrl(url);
    	ret = ladeWetterVonJsonTag(json);
    	return ret;
    }
    
    /**
     * Methode verarbeitet Json objekt zu einer Liste von Wetterinformation Objekten.
     * Es kommt in die List immer der nächste Abschnitt  mit aktuellen Wetter.
     * @param json JsonObjekt mit Wetterdaten für abschnitt
     * @return Liste mit Wetterabschnitten 
     */
    public List<Wetterinformation> ladeWetterVonJsonTag(JSONObject json){
	   List<Wetterinformation> ret = null;
	   JSONObject city = json.getJSONObject(JsonConfig.CITY);
	   
	   String stadtCode = city.getString("city_code");
	   JSONObject forecasts = city.getJSONObject(JsonConfig.FORECAST);
	   
	   // Aktuelles datum von Json
	   String datum = (String) forecasts.keys().next();
	   JSONObject date = forecasts.getJSONObject(datum);
	   System.out.println(date.toString());
	   
	   ret = new ArrayList<Wetterinformation>();
	   JSONObject abschnitt = null;
	   
	   Iterator iterator = date.keys();
	   while(iterator.hasNext()){
		   String next = (String) iterator.next();
		   try{
			   if(next.equals(JsonConfig.ZEITFRUEH)){
				   abschnitt = date.getJSONObject(next);
				   ret.add(bekommeAbschnittTag(JsonConfig.ZEITFRUEH, datum, stadtCode, abschnitt));
			   }else if(next.equals(JsonConfig.ZEITMITTAG)){
				   abschnitt = date.getJSONObject(next);
				   ret.add(bekommeAbschnittTag(JsonConfig.ZEITMITTAG, datum, stadtCode, abschnitt));
			   } else if (next.equals(JsonConfig.ZEITABEND)){
				   abschnitt = date.getJSONObject(next);
				   ret.add(bekommeAbschnittTag(JsonConfig.ZEITABEND, datum, stadtCode, abschnitt));
			   }else if (next.equals(JsonConfig.ZEITNACHT)){
				   abschnitt = date.getJSONObject(next);
				   ret.add(bekommeAbschnittTag(JsonConfig.ZEITNACHT, datum, stadtCode, abschnitt));
			   }
		   }catch(Exception e){
			   LoggerBot.error(LOGKLASSE, "Methode ladeWetterVonJsonTag(JSONObject json)", e);
		   }
	   }
	   return ret;
    }
   
    
    
    /**
     * Methode die eine Wetterperiode in Json format durchsucht und in ein Wetterinformation Objekt packt
     * @param start Startzeit im Tag
     * @param datum tag für Prognose
     * @param stadtCode eindeutige Wetterstation 
     * @param json JsonObject mit den Informationen
     * @return Wetterinformation object
     */
    private Wetterinformation bekommeAbschnittTag(String start, String datum, String stadtCode, JSONObject json){
		Wetterinformation ret = null;
    	ret = new Wetterinformation();
    	ret.setStadtCode(stadtCode);
    	ret.setDatum(stringToDatum(datum));
    	ret.setStart(stringToTime(start));
    	
    	String hilfe = json.getString(JsonConfig.GUELTIGKEITSZEITRAUM);
    	ret.setGueltigkeit(stringToInt(hilfe));
    	//System.out.println(ret.getGueltigkeit());
    	
    	hilfe = json.getString(JsonConfig.WETTERZUSTANDCODE);
    	ret.setWetterzustand(stringToInt(hilfe));
    	//System.out.println(ret.getWetterzustand());
    	
    	hilfe = json.getString(JsonConfig.MAXIMALTEMPERATUR);
    	ret.setTemperaturMax(stringToInt(hilfe));
    	//System.out.println(ret.getTemperaturMax());
    	
    	hilfe = json.getString(JsonConfig.MINDESTTEMPERATUR);
    	ret.setTemperaturMin(stringToInt(hilfe));
    	//System.out.println(ret.getTemperaturMin());
    	
    	hilfe = json.getString(JsonConfig.WINDGESCHWINDIKEIT);
    	ret.setWindgeschwindigkeit(stringToInt(hilfe));
    	//System.out.println(ret.getWindgeschwindigkeit());
    	
    	hilfe = json.getString(JsonConfig.WINDRICHTUNG);
    	ret.setWindrichtung(stringToInt(hilfe));
    	//System.out.println(ret.getWindrichtung());
    	
    	hilfe = json.getString(JsonConfig.NIEDERSCHLAGSWAHRSCHEINLICHKEIT);
    	//System.out.println(hilfe + " hallo "+ stringToInt(hilfe));
    	ret.setNiederschlagswahrscheinlichkeit(stringToInt(hilfe));
    	//System.out.println(ret.getNiederschlagswahrscheinlichkeit());
    	
    	return ret;
    }
    
    /**
     * Methode castet von "yyyy-MM-dd" String zu einen java.util.Date
     * @param datum String in format 
     * @return java.util.Date return 
     */
    public static java.sql.Date stringToDatum(String datum){
    	java.sql.Date ret = null;
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		java.util.Date date = formatter.parse(datum);
    		ret = new java.sql.Date(date.getTime());
    	} catch (ParseException e) {
    		LoggerBot.error(LOGKLASSE, "Methode stringToDatum(String datum)", e);
    	}
		return ret;
    }
    
    /**
     * Methode castet von "hh:mm" String zu einen java.util.Date
     * @param datum String in format 
     * @return java.util.Date return 
     */
    public static java.sql.Time stringToTime(String zeit){
    	java.sql.Time ret = null;
    	SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
    	try {
    		java.util.Date date = formatter.parse(zeit);
    		ret = new java.sql.Time(date.getTime());
    	} catch (ParseException e) {
    		LoggerBot.error(LOGKLASSE, "Methode stringToTime(String time)", e);
    	}
		return ret;
    }
    
    public static int stringToInt(String integer){
    	int ret = Integer.MIN_VALUE;
    	try{
    		ret = Integer.parseInt(integer);
    	}catch(NumberFormatException e){
    		LoggerBot.error(LOGKLASSE, "stringToInt(String integer)", e);
    	}
    	return ret;
    }
}
