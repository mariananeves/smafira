package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SQLConnector {
	private String USER = "";
	private String PASSW = "";
	private String serverPath = "";
	private String dbName = "";
	private String driver = "";
	private final String LOGINFILEPATH = "resources/dbLogin";	
	public enum DBType{
		POSTGRESQL("postgresql"),
		MYSQL("mysql");
		
		String name;
		private DBType(String name) {
			this.name = name;
		}
	}
	private String dbType = "";
		
	
	public SQLConnector(DBType dbType){
		this.dbType = dbType.name;
		init();
	}
	
	private void init(){
		getDBData();
	}
	
	private void loadDriver(){
		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		Connection conn = null;
			
		loadDriver();
		try {
			conn = DriverManager.getConnection(serverPath, USER, PASSW);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	private void getDBData(){		
		ResourceBundle rb = ResourceBundle.getBundle(LOGINFILEPATH);
		serverPath = rb.getString(dbType + ".db.server.url");
		dbName = rb.getString(dbType + ".db.name");
		serverPath += dbName;
		USER = rb.getString(dbType + ".db.login.user");
		PASSW = rb.getString(dbType + ".db.login.passw");
		driver = rb.getString(dbType + ".db.driver");
		
//		System.out.println("serverPath: " + serverPath
//				+ "\nuser: " + USER
//				+ "\npassw: " + PASSW
//				+ "\ndriver: " + driver);
	}
	
	
}
