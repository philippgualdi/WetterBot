package at.gualdi;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class LoggerBot extends TelegramLongPollingBot{

	
	private static String BOT_TOKEN = "160840724:AAFgDO9C_3DdY_kokQABQXBLj6yQodyASyI";
	private static String BOT_NAME = "Kontroller";
	
	
	public LoggerBot() {
		// TODO Auto-generated constructor stub
		 super();
		 System.out.println("Logger gestartet!!!");
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
	
	private void handleIncomingMessage(Message message) {
		System.out.println("ChatID: " + message.getChatId()+ " Message Text: " + message.getText());
		System.out.println(" Name " + message.getFrom().getLastName() + " Vorname: " +message.getFrom().getFirstName() + " Benutzername: " + message.getFrom().getUserName());
		System.out.println("Nachricht wurde erfolgreich gesendet: " + messageReturn(message)); 
		System.out.println("FORWARD:"+ message.getForwardFrom().getFirstName() + " : " + message.getForwardFrom().getLastName());
	}
	 
	private boolean messageReturn(Message message){
		boolean ret = false;
		SendMessage send = new SendMessage();
		send.setChatId(message.getChatId().toString());
		//send.setReplayToMessageId(message.getMessageId());
		send.enableMarkdown(true);
		send.setText(message.getText());
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

	
	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return BOT_TOKEN;
	}

	
}
