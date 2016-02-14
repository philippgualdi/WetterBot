package at.gualdi.objects;

public class Benutzer {
	
	protected int id = -1;
	protected String vorname = "";
	protected String name = "";
	protected String benutzername = "";
	protected long chatId = -1;
	protected boolean aktiv = false;
	protected String sprache = "";
	
	public Benutzer(){
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		if(vorname != null)
			this.vorname = vorname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null)
			this.name = name;
	}

	public String getBenutzername() {
		return benutzername;
	}

	public void setBenutzername(String benutzername) {
		if(benutzername != null)
			this.benutzername = benutzername;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public boolean isAktiv() {
		return aktiv;
	}

	public void setAktiv(boolean aktiv) {
		this.aktiv = aktiv;
	}

	public String getSprache() {
		return sprache;
	}

	public void setSprache(String sprache) {
		if(sprache != null)
			this.sprache = sprache;
	}

	
}
