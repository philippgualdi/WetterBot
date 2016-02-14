package at.gualdi.arbeiten;

public interface Command {
	public static final String PROJEKTAPICODE = "8a77ade8cdbb391a86b2667048cf992e";
	public static final String PROJEKTNAME = "weatherbot";
	
	public static final String BEGINNCOMMAND = "/";
	
	/**
	 * Benutzer gestartet oder gestoppt;
	 */
	public static final String STARTCOMMAND = BEGINNCOMMAND + "start";
	public static final String STOPPCOMMAND = BEGINNCOMMAND + "stop";
	
	public static final String WETTERCOMMAND = BEGINNCOMMAND + "wetter";
	public static final String WETTEROPTIONAKTUELL = "aktuell";
	public static final String WETTEROPTIONNACHER = "nacher";
	
	public static final String ZEITCOMMAND = BEGINNCOMMAND + "zeit";
}
