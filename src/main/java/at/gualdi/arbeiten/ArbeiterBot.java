package at.gualdi.arbeiten;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import at.gualdi.Main;
import at.gualdi.database.DatabaseManager;
import at.gualdi.objects.Benutzer;
import at.gualdi.objects.Wetterinformation;
import at.gualdi.service.JsonLoader;

public class ArbeiterBot extends TelegramLongPollingBot implements ArbeitConfig{

	/**
	 * Automatzustand aktuell
	 */
	private int status = -1;
	
	
	private static final int START = 0;
	
	
	private static final int  WETTERORTE = 2;
	private static final int  WETTERZEIT = 3;
	
	public ArbeiterBot(int status) {
		// TODO Auto-generated constructor stub
		super();
		this.status = status;
		System.out.println("ArbeiterBot wurde gestartet!!!");
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText() || message.hasLocation()) {
                handleIncomingMessage(message);
            }
        }
	}

	/**
	 * Methdoe verarbeitet einkommende Nachrichten
	 * @param message
	 */
	private void handleIncomingMessage(Message message) {
		// TODO Auto-generated method stub
		
		System.out.println("INCOMMIng MESSAGE Status: " + this.status);
		if(message.getText().startsWith(Command.STARTCOMMAND)){
			benutzerErzeugen(message);
		} else if(message.getText().startsWith(Command.WETTERCOMMAND) || this.status == WETTERORTE || this.status == WETTERZEIT){
			wetterVerarbeiten(message);
		}else if(message.getText().startsWith(Command.ZEITCOMMAND)){
			// Nachfragen welche Zeit die Wetterinformation geholt werden soll.
			System.out.println("Status Zeit command");
			
		} else if(message.getText().startsWith(Command.STOPPCOMMAND)){
			
		}
	}
	
	/**
	 * Methode wird aufgerufen, wenn der Benutzer das erste mal 
	 * 
	 * @param message eingegangene Nachricht
	 */
	private void benutzerErzeugen(Message message){
		Benutzer benutzer = new Benutzer();
		User user = message.getFrom();
		benutzer.setName(user.getLastName());
		benutzer.setVorname(user.getFirstName());
		benutzer.setBenutzername(user.getUserName());
		benutzer.setSprache("");
		benutzer.setChatId(message.getChatId());
		DatabaseManager dbm = DatabaseManager.getInstance();
		int antwort = dbm.erstelleBenutzer(benutzer);
		System.out.println("DB antworten Benutzereinfügen: " + antwort);
	}
	
	/**
	 * Methode wird aufgerufen wenn WetterCommand oder der Status etwas mit dem Wetter zu tun hat.
	 * 
	 * @param message eingegangene Nachricht von Benutzer
	 */
	private void wetterVerarbeiten(Message message){
		String text = message.getText();
		System.out.println("WETTERVORBEREITEN");
		if(this.status == START){
			// Nachfragen welcher Ort die Wetterinformation geholt werden soll.
			this.status = this.WETTERORTE;
			System.out.println("Status WEtTERORTE");
			fragenNachWetterOrte(""+message.getChatId());
		} else if(this.status == WETTERORTE){
			this.status = this.WETTERZEIT;
			//Wetterort wird geholt und zurückgeschickt
			wetterInformationenOrt(message.getChatId().toString(), "", "ATAT20125", "7ce145257fa7f440e6be7b29f23f5726");
			System.out.println("Status WETTERZEIT");			
		} else if(this.status == WETTERZEIT){
			this.status = this.START;
			//Wetterort wird geholt  Ob aktuelles wetter
			wetterInformationenOrt(message.getChatId().toString(), message.getText().trim(), "ATAT20125", "7ce145257fa7f440e6be7b29f23f5726");
			System.out.println("Status WETTERZEIT: " +text);	
		}	
	}
	
	/**
	 * Methode wird aufgerufen, wenn das WetterCommand ausgeführt wurde und nach den gespeicherten Orten .
	 * 
	 * @param chatId Benutzer an den die nachricht gesendeet werden soll.
	 * @return Message mit den Orten die ausgewählt werden können.
	 */
	private SendMessage fragenNachWetterOrte(String chatId){
		SendMessage ret = new SendMessage();
		ret.setChatId(chatId);
		ret.setReplayMarkup(keyboardWetterOrte());
		return ret;
	}
	
	/**
	 * Methode Wetterinformationen zu laden von einem bestimmten Ort.
	 * 
	 * @param option 
	 * @param Ort
	 * @param checksumme
	 */
	private void wetterInformationenOrt(String chatId, String option, String ort, String checksumme){
		JsonLoader jsonLoader = JsonLoader.getInstance();
		List<Wetterinformation>  wetter = jsonLoader.ladeWetterVonOrt(ort, checksumme);
		DatabaseManager db = DatabaseManager.getInstance();
		db.erstelleWetterinformationen(wetter);
		if(option.length() == 0){
			antwortenWetterMessage(chatId, wetter.get(3).baueStringAbschnitt()); 
		}else if(option.startsWith(Command.WETTEROPTIONAKTUELL)){
			antwortenWetterMessage(chatId, wetter.get(3).baueStringAbschnitt()); 
		} else if(option.startsWith(Command.WETTEROPTIONNACHER)){
			antwortenWetterMessage(chatId, wetter.get(0).baueStringAbschnitt()); 
		}
		
	}
	
	
	private boolean antwortenWetterMessage(String chatId, String text){
		boolean ret = false;
		SendMessage send = new SendMessage();
		send.setChatId(chatId);
		send.setReplayMarkup(null);
		//send.setReplayToMessageId(message.getMessageId());
		send.enableMarkdown(true);
		send.setText(text);
		try {
			sendMessage(send);
			ret = true;
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret  = false;
		}
		return ret;
	 }
	
	
	private static ReplyKeyboardMarkup keyboardWetterOrte(){
		ReplyKeyboardMarkup ret = new ReplyKeyboardMarkup();
        List<List<String>> keyboard = new ArrayList<>();
        List<String> row = new ArrayList<>();
        row.add("Graz");
        row.add("Graz Flughafen");
        ret.setKeyboard(keyboard);
        keyboard.add(row);
        row = new ArrayList<>();
        row.add("Unterprämsteten");
        ret.setResizeKeyboard(true);
        ret.setOneTimeKeyboad(true);
        ret.setSelective(true);
        ret.setKeyboard(keyboard);
		return ret;	
	}


	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return ArbeitConfig.BOT_NAME;
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return ArbeitConfig.BOT_TOKEN;
	}

}
