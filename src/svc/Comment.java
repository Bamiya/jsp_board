package svc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import db.InitDB;
import vo.VO;

public class Comment {
	public void select(String BoardId) {
		Connection con = InitDB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = String.format("SELECT * FROM comments WHERE BoardId = '%s' ORDER BY COMMENTID", BoardId);
		
		try {
			pstmt = con.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("\t------------------------------------------------------------");
				System.out.println("\t��� ���̵�: " + rs.getString(1));
				System.out.println("\t�ۼ���: " + rs.getString(3));
				System.out.println("\t����: " + rs.getString(5));
				System.out.println("\t�ۼ� �ð�: " + rs.getString(6));
				System.out.println("\t------------------------------------------------------------\n");
			}
		
			con.close();
			pstmt.close();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void create(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		Board board = new Board();
		
		String id = board.select(in);
		if(id == null) return;
		
		System.out.print("��� ���� �Է�:");
		String text = in.nextLine();
		
		String sql = "{ call comment_create(?, ? ,?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, VO.getMyID());
			cstmt.setString(2, id);
			cstmt.setString(3, text);
			
			int rowCount = cstmt.executeUpdate();
			
			System.out.println(rowCount > 0 ? "��� ���� �Ϸ�" : "��� ���� ����");
			
			cstmt.close();
			con.close();
			
		}catch(Exception e) {
			System.out.println("��� ���� ����");
			e.printStackTrace();
		}
	}
	
	public void delete(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		Board board = new Board();
		String boardId = board.select(in); 
		
		if(boardId == null) return;
		
		System.out.print("��� ���̵� �Է�: ");
		String commentId = in.nextLine();
		
		String sql = "{ call comment_delete(?, ?, ?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, commentId);
			cstmt.setString(2, VO.getMyID());
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
			cstmt.executeUpdate();
			
			String str = cstmt.getString(3);
			System.out.println(str != null ? "���� �Ϸ�" : "���� ����");
			
			cstmt.close();
			con.close();
		}catch(Exception e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}
}
