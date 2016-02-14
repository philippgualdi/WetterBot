package at.gualdi;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import at.gualdi.arbeiten.ArbeiterBot;

public class Main {
	
	private static int wetterstatus = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
        	//telegramBotsApi.registerBot(new LoggerBot());
            telegramBotsApi.registerBot(new ArbeiterBot(wetterstatus));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

}
