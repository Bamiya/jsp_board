package svc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.InitDB;

public class Views {
	public void update(String boardId) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		
		String sql = "{ call views_update(?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, boardId);
			
			cstmt.executeUpdate();
			
			cstmt.close();
			con.close();
		}catch(Exception e) {
			System.out.println("조회수 업데이트 실패");
			e.printStackTrace();
		}
	}
}
