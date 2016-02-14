package at.gualdi.service;

import at.gualdi.database.DatabaseManager;

public class LoggerBot {

	private static final String ERROR = "error";
	private static final String WARNING = "warning";
	private static final String INFO = "info";
	private static final String DEBUG = "debug";
	
	public LoggerBot(){
		
	}
	
	public static void error(String klasse, String text, Exception fehler){
		DatabaseManager db = DatabaseManager.getInstance();
		int ret = db.erstelleLog(klasse, ERROR, fehler.toString(), text);
		
	}
	
	public static void warning(String klasse, String text){
		DatabaseManager db = DatabaseManager.getInstance();
		int ret = db.erstelleLog(klasse, WARNING, "", text);
		
	}
	
	public static void information(String klasse, String text){
		DatabaseManager db = DatabaseManager.getInstance();
		int ret = db.erstelleLog(klasse, INFO, "", text);
		
	}
	
	public static void debug(String klasse, String text){
		DatabaseManager db = DatabaseManager.getInstance();
		int ret = db.erstelleLog(klasse, DEBUG, "", text);
		
	}
}
