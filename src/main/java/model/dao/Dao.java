package model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Myynti;

public class Dao {
	private Connection con=null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep=null; 
	private String sql;
	private String db ="Myynti.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");    	
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); //Eclipsessa
    	//path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	public ArrayList<Myynti> listaaKaikki(){
		ArrayList<Myynti> asiakkaat = new ArrayList<Myynti>();
		sql = "SELECT * FROM asiakkaat";       
		try {
			con=yhdista();
			if(con!=null){ //jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);        		
        		rs = stmtPrep.executeQuery();   
				if(rs!=null){ //jos kysely onnistui
					//con.close();					
					while(rs.next()){
						Myynti myynti = new Myynti();
						myynti.setAsiakas_id(rs.getInt(1));
						myynti.setEtunimi(rs.getString(2));
						myynti.setSukunimi(rs.getString(3));
						myynti.setPuhelin(rs.getString(4));
						myynti.setSposti(rs.getString(5));	
						asiakkaat.add(myynti);
					}					
				}				
			}	
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return asiakkaat;
	}

public ArrayList<Myynti> listaaKaikki(String hakusana){
	ArrayList<Myynti> asiakkaat = new ArrayList<Myynti>();
	sql = "SELECT * FROM asiakkaat WHERE asiakas_id LIKE ? or etunimi LIKE ? or sukunimi LIKE ? or puhelin LIKE ? or sposti LIKE ?";       
	try {
		con=yhdista();
		if(con!=null){ 
			stmtPrep = con.prepareStatement(sql); 
			stmtPrep.setString(1, "%" + hakusana + "%");
			stmtPrep.setString(2, "%" + hakusana + "%");   
			stmtPrep.setString(3, "%" + hakusana + "%"); 
			stmtPrep.setString(4, "%" + hakusana + "%");
			stmtPrep.setString(5, "%" + hakusana + "%");
    		rs = stmtPrep.executeQuery();   
			if(rs!=null){ 
				//con.close();					
				while(rs.next()){
					Myynti myynti = new Myynti();
					myynti.setAsiakas_id(rs.getInt(1));
					myynti.setEtunimi(rs.getString(2));
					myynti.setSukunimi(rs.getString(3));
					myynti.setPuhelin(rs.getString(4));	
					myynti.setSposti(rs.getString(5));	
					asiakkaat.add(myynti);
				}					
			}				
		}	
		con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}		
	return asiakkaat;
}

public boolean lisaaAsiakas(Myynti myynti){
	boolean paluuArvo=true;
	sql="INSERT INTO asiakkaat (etunimi, sukunimi, puhelin, sposti) VALUES(?,?,?,?)";						  
	try {
		con = yhdista();
		stmtPrep=con.prepareStatement(sql); 
		stmtPrep.setString(1, myynti.getEtunimi());
		stmtPrep.setString(2, myynti.getSukunimi());
		stmtPrep.setString(3, myynti.getPuhelin());
		stmtPrep.setString(4, myynti.getSposti());
		stmtPrep.executeUpdate();
		// System.out.println("Uusin id on " + stmtPrep.getGeneratedKeys().getInt(1));
        con.close();
	} catch (Exception e) {				
		e.printStackTrace();
		paluuArvo=false;
	}				
	return paluuArvo;
}
	public boolean poistaAsiakas(int asiakas_id){
	boolean paluuArvo=true;
	sql="DELETE FROM asiakkaat WHERE asiakas_id=?";						  
	try {
		con = yhdista();
		stmtPrep=con.prepareStatement(sql); 
		stmtPrep.setInt(1, asiakas_id);			
		stmtPrep.executeUpdate();
        con.close();
	} catch (Exception e) {				
		e.printStackTrace();
		paluuArvo=false;
	}				
	return paluuArvo;
}	
	public Myynti etsiAsiakas(int asiakas_id) {
		Myynti myynti = null;
		sql = "SELECT * FROM asiakkaat WHERE asiakas_id=?";
		
		try {
			con=yhdista();
			if(con!=null){ 
				stmtPrep = con.prepareStatement(sql); 
				stmtPrep.setInt(1, asiakas_id);
	    		rs = stmtPrep.executeQuery();   
				if(rs.isBeforeFirst()){ 					
					rs.next();
						myynti = new Myynti();
						myynti.setAsiakas_id(rs.getInt(1));
						myynti.setEtunimi(rs.getString(2));
						myynti.setSukunimi(rs.getString(3));
						myynti.setPuhelin(rs.getString(4));	
						myynti.setSposti(rs.getString(5));	
					}					
				}				
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return myynti;
	}
	
	public boolean muutaAsiakas(Myynti myynti){
		boolean paluuArvo=true;
		sql="UPDATE asiakkaat SET etunimi=?, sukunimi=?, puhelin=?, sposti=? WHERE asiakas_id=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setString(1, myynti.getEtunimi());
			stmtPrep.setString(2, myynti.getSukunimi());
			stmtPrep.setString(3, myynti.getPuhelin());
			stmtPrep.setString(4, myynti.getSposti());
			stmtPrep.setInt(5, myynti.getAsiakas_id());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
}



	