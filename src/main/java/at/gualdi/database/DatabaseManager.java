package at.gualdi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.*;

import at.gualdi.objects.Benutzer;
import at.gualdi.objects.Wetterinformation;

public class DatabaseManager {

	private static volatile DatabaseManager instanz;
	private static MysqlConnectionPoolDataSource ds  = null;
	
	/*
	 * Private, statische Konstanten
	 */
	private static final String MYSQLTREIBER = "com.mysql.jdbc.Driver";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final String SERVERNAME = "localhost";
	private static final String DATABASENAME = "wetterbot";
	private static final int CONNECTIONTIMEOUT = 5;
	
	private DatabaseManager(){
		try {
			Class.forName(MYSQLTREIBER);
			ds = new MysqlConnectionPoolDataSource();
			ds.setUser(USER);
			ds.setPassword(PASSWORD);
			ds.setServerName(SERVERNAME);
			ds.setDatabaseName(DATABASENAME);
			ds.setLoginTimeout(CONNECTIONTIMEOUT);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	public int erstelleBenutzer(Benutzer benutzer){
		int ret = -1;
		Connection con  = null;
		PreparedStatement pstmt = null;
		try{
			con = ds.getConnection();
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("INSERT INTO benutzer(bname,bfamilienname,"
					+ "bbenutzername,bchatid,baktiv,bsprache)VALUES(?,?,?,?,?,?);");
			
			pstmt.setString(1, benutzer.getVorname());
			pstmt.setString(2, benutzer.getName());
			pstmt.setString(3, benutzer.getBenutzername());
			pstmt.setLong(4, benutzer.getChatId());
			pstmt.setBoolean(5, true);
			pstmt.setString(6, benutzer.getSprache());
			ret = pstmt.executeUpdate();
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e) {
			}
		}		
		return ret;
	}
	
	public int erstelleWetterinformationen(List<Wetterinformation> wetter){
		int ret = -1;
		int woid = -1;
		Connection con  = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String einfuegen = "INSERT INTO wetterinformation(woid,weid,wdatum,wstart,wgueltigkeit,wniederschlagswahrscheinlichkeit,wwindrichtung,wwindgeschwindigkeit,wtemperaturmin,wtemperaturmax)"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?);";
		String ortFinden = "SELECT woid FROM wetterort WHERE wocode =\"";
		int i = 0;
		try{
			con = ds.getConnection();
			con.setTransactionIsolation(con.TRANSACTION_READ_COMMITTED);
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(einfuegen);
			stmt = con.createStatement();
			while(i < wetter.size()){
				System.out.println("StadtCode: " + ortFinden + wetter.get(i).getStadtCode() + "\";");
				rs = stmt.executeQuery(ortFinden + wetter.get(i).getStadtCode() + "\";");
				rs.next();
				woid = rs.getInt(1);
				
				pstmt.setInt(1, woid);
				pstmt.setInt(2, wetter.get(i).getWetterzustand());
				pstmt.setDate(3, wetter.get(i).getDatum());
				pstmt.setTime(4, wetter.get(i).getStart());
				pstmt.setInt(5, wetter.get(i).getGueltigkeit());
				pstmt.setInt(6, wetter.get(i).getNiederschlagswahrscheinlichkeit());
				pstmt.setInt(7, wetter.get(i).getWindrichtung());
				pstmt.setInt(8, wetter.get(i).getWindgeschwindigkeit());
				pstmt.setInt(9, wetter.get(i).getTemperaturMin());
				pstmt.setInt(10, wetter.get(i).getTemperaturMax());
				
				pstmt.executeUpdate();
				i++;
				con.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
				stmt.close();
				pstmt.close();
			} catch (SQLException e) {
			}
		}		
		return ret;
	}
	
	
	public int erstelleLog(String klasse, String status, String text, String fehler){
		int ret = -1;
		Connection con  = null;
		PreparedStatement pstmt = null;
		try{
			con = ds.getConnection();
			con.setTransactionIsolation(con.TRANSACTION_READ_UNCOMMITTED);
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("INSERT INTO log(lklasse,lstatus,"
					+ "ltext,lfehler)VALUES(?,?,?,?);");
			pstmt.setString(1, klasse);
			pstmt.setString(2, status);
			pstmt.setString(3, text);
			pstmt.setString(4, fehler);
			ret = pstmt.executeUpdate();
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e) {
			}
		}		
		return ret;
	}
	
	
	/**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static DatabaseManager getInstance() {
        final DatabaseManager currentInstance;
        if (instanz == null) {
            synchronized (DatabaseManager.class) {
                if (instanz == null) {
                    instanz = new DatabaseManager();
                }
                currentInstance = instanz;
            }
        } else {
            currentInstance = instanz;
        }
        return currentInstance;
    }
    
    
    
}
