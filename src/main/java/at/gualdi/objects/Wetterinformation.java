package at.gualdi.objects;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class Wetterinformation {

	
	protected String stadtCode = null;
	
	/**
	 * SQL DATUM
	 */
	protected Date datum = null;

	/**
	 * SQL TIME
	 */
	protected Time start  = null;
	
	/**
	 * Code: 1-999 Wetterzustand
	 */
	protected int wetterzustand = -1;
	
	
	protected int gueltigkeit = -1;
	protected int niederschlagswahrscheinlichkeit = -1;
	protected int windrichtung = -1;
	protected int windgeschwindigkeit = -1;
	protected int temperaturMin = -1;
	protected int temperaturMax = -1;

	public Wetterinformation(){
		
	}
	
	public int getWetterzustand() {
		return wetterzustand;
	}

	public void setWetterzustand(int wetterzustand) {
		this.wetterzustand = wetterzustand;
	}
	
	public String getStadtCode() {
		return stadtCode;
	}
	
	public void setStadtCode(String stadtCode) {
		this.stadtCode = stadtCode;
	}
	
	public Date getDatum() {
		return datum;
	}
	
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
	public Time getStart() {
		return start;
	}
	
	public void setStart(Time start) {
		this.start = start;
	}
	
	public int getGueltigkeit() {
		return gueltigkeit;
	}
	public void setGueltigkeit(int gueltigkeit) {
		this.gueltigkeit = gueltigkeit;
	}
	public int getNiederschlagswahrscheinlichkeit() {
		return niederschlagswahrscheinlichkeit;
	}
	public void setNiederschlagswahrscheinlichkeit(int niederschlagswahrscheinlichkeit) {
		this.niederschlagswahrscheinlichkeit = niederschlagswahrscheinlichkeit;
	}
	public int getWindrichtung() {
		return windrichtung;
	}
	public void setWindrichtung(int windrichtung) {
		this.windrichtung = windrichtung;
	}
	public int getWindgeschwindigkeit() {
		return windgeschwindigkeit;
	}
	public void setWindgeschwindigkeit(int windgeschwindigkeit) {
		this.windgeschwindigkeit = windgeschwindigkeit;
	}
	public int getTemperaturMin() {
		return temperaturMin;
	}
	public void setTemperaturMin(int temperaturMin) {
		this.temperaturMin = temperaturMin;
	}
	public int getTemperaturMax() {
		return temperaturMax;
	}
	public void setTemperaturMax(int temperaturMax) {
		this.temperaturMax = temperaturMax;
	}
	
	
	public String baueStringAbschnitt(){
		SimpleDateFormat formater = new SimpleDateFormat("hh:mm");
		String starts = formater.format(start);
		formater = new SimpleDateFormat("dd.MM.yyyy");
		String datums = formater.format(datum);
		
		String ret = "";
		ret += "Wetterinformation für " + stadtCode +  "\nStartdatum: "
		+ datums + " Startzeit: " + starts +
		" Gültikgeit: " + gueltigkeit + " Stunden, Regenwahr.: " +
		niederschlagswahrscheinlichkeit + "% Windrichtung: " + windrichtung
		+ "° Windgesch: " + windgeschwindigkeit + "km/h\n Temperatur min/max: " +
		temperaturMin + " °C/ " + temperaturMax  +" °C";
		
		return ret;
	}
}
