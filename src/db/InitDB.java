package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class InitDB {
	static {
		try {
			//driver 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "java", "java1234");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
}