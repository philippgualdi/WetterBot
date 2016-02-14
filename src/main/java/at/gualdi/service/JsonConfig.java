package at.gualdi.service;

public interface JsonConfig {
	
	/**
	 * Wetter.com api zugriff
	 */
	public static final String BASEURL = "http://api.wetter.com/forecast/weather/city/";
	public static final String PROJEKT = "/project/weatherbot";
	public static final String CHECKSUMME = "/cs/";
	public static final String OPTION = "/output/json";
	
	/**
	 * Zeiten der Abschnitte  pro Tag
	 */
	public static final String ZEITFRUEH = "06:00"; 
	public static final String ZEITMITTAG = "11:00";
	public static final String ZEITABEND = "17:00";
	public static final String ZEITNACHT = "23:00";
	
	public static final String CITY = "city";
	public static final String FORECAST = "forecast";
	
	/**
	 * Gültigkeitszeitraum der Prognose (24 Stunden, 1 Stunde, 
	 * 3 Stunden, ...) [int unsigned, 0-24] 
	 */
	public static final String GUELTIGKEITSZEITRAUM = "p";
	/**
	 * ode für Wetterzustand [int unsigned, 0-999] Siehe 
	 * „Definition Wetterzustände“  
	 */
	public static final String WETTERZUSTANDCODE = "w";
	/**
	 * Änderung des Luftdrucks [float 1 Nachkommastelle]
	 */
	public static final String NIEDERSCHLAGSWAHRSCHEINLICHKEIT = "pc";
	/**
	 * Windrichtung in Grad [int, 0=N, 90=O, 180=S, 270=W]
	 */
	public static final String WINDRICHTUNG = "wd";
	/**
	 * Gindgeschwindigkeit in Kilometer/Stunde [float unsigned,
	 *  1 Nachkommastelle] 
	 */
	public static final String WINDGESCHWINDIKEIT = "ws";
	/**
	 * Mindesttemperatur
	 */
	public static final String MINDESTTEMPERATUR= "tn";
	/**
	 * Maximaltemperatur
	 */
	public static final String MAXIMALTEMPERATUR = "tx";
}
