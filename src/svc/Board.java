package svc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import db.InitDB;
import vo.VO;

public class Board {
	public void onlySelect(Scanner in) { // 그저 게시물 조회를 위한 메thㅓ드
		Connection con = InitDB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comment comment = new Comment();
		Views view = new Views();
		String sql = "SELECT * FROM board ORDER BY BOARDID";
		int cnt = 0;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				cnt++;
				if(rs.getString(7) == null || (rs.getString(7) != null && rs.getString(2).equals(VO.getMyID()) || VO.isAdmin())) {
					System.out.println("=====================================");
					System.out.println("조회수: " + rs.getString(8));
					System.out.println("\n게시물 아이디: " + rs.getString(1));
					System.out.println("제목: " + rs.getString(4));
					System.out.println("글쓴이 닉네임: " + rs.getString(3));
					System.out.println("마지막으로 쓴 날짜: " + rs.getString(6));
					System.out.println("\n=====================================");
				}else{
					System.out.println("=====================================");
					System.out.println("조회수: " + rs.getString(8));
					System.out.println("\n게시물 아이디: " + rs.getString(1));
					System.out.println("비공개 게시물 입니다.");
					System.out.println("\n=====================================");
				}
			}
			
			if(cnt == 0) {
				System.out.println("현재 게시물이 없음");
				return;
			}
			
			System.out.print("조회할 게시물 아이디를 입력: ");
			String boardId = in.nextLine();
				
			sql = String.format("SELECT * FROM board WHERE boardId = '%s'  ORDER BY BOARDID", boardId);
				
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			cnt = 0;
					
			while(rs.next()) {
				if(!VO.isAdmin()) {
					if(rs.getString(7) != null && !rs.getString(2).equals(VO.getMyID())) {
						System.out.println("비밀글 입니다.");					
						System.out.print("비밀번호를 입력해주세요: ");
						String password = in.nextLine();
						if(!password.equals(rs.getString(7))) {
							System.out.println("비밀번호가 틀렸습니다!");
							break;
						}
					}	
				}
				cnt++;
				System.out.println("=====================================");
				System.out.printf("\n조회수: %s\n\n글쓴이 닉네임: %s\n제목: %s\n내용: %s\n마지막으로 쓴 날짜: %s\n", 
						rs.getString(8),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				System.out.println("\n\t--댓글--");
				comment.select(rs.getString(1));
				System.out.println("\n=====================================");
			}
			
			if(cnt > 0) {
				view.update(boardId);
			}
			
			con.close();
			pstmt.close();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String select(Scanner in) { // board ID를 return 시키는 board select 함수
		Connection con = InitDB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comment comment = new Comment();
		Views view = new Views();
		String sql = "SELECT * FROM board ORDER BY BOARDID";
		int cnt = 0;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				cnt++;
				if(rs.getString(7) == null || (rs.getString(7) != null && rs.getString(2).equals(VO.getMyID()) || VO.isAdmin())) {
					System.out.println("=====================================");
					System.out.println("조회수: " + rs.getString(8));
					System.out.println("\n게시물 아이디: " + rs.getString(1));
					System.out.println("제목: " + rs.getString(4));
					System.out.println("글쓴이 닉네임: " + rs.getString(3));
					System.out.println("마지막으로 쓴 날짜: " + rs.getString(6));
					System.out.println("\n=====================================");
				}else{
					System.out.println("=====================================");
					System.out.println("조회수: " + rs.getString(8));
					System.out.println("\n게시물 아이디: " + rs.getString(1));
					System.out.println("비공개 게시물 입니다.");
					System.out.println("\n=====================================");
				}
			}
			
			if(cnt == 0) {
				System.out.println("게시물이 없음");
				return null;
			}
			
			System.out.print("조회할 게시물 아이디를 입력: ");
			String boardId = in.nextLine();
			sql = String.format("SELECT * FROM board WHERE boardId = '%s'  ORDER BY BOARDID", boardId);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			cnt = 0;
					
			while(rs.next()) {
				if(!VO.isAdmin()) {
					if(rs.getString(7) != null && !rs.getString(2).equals(VO.getMyID())) {
						System.out.println("비밀글 입니다.");					
						System.out.print("비밀번호를 입력해주세요: ");
						String password = in.nextLine();
						if(!password.equals(rs.getString(7))) {
							System.out.println("비밀번호가 틀렸습니다!");
							boardId = null;
							break;
						}
					}
				}
				// 이떄!!!!!!!!!!!!!!! 조회수를 +1
				cnt++;
				System.out.println("=====================================");
				System.out.printf("\n조회수: %s\n\n글쓴이 닉네임: %s\n제목: %s\n내용: %s\n마지막으로 쓴 날짜: %s\n", 
						rs.getString(8),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
				System.out.println("\n\t--댓글--");
				comment.select(rs.getString(1));
				System.out.println("\n=====================================");
			}
			
			if(cnt > 0) {
				view.update(boardId);
			}
			
			con.close();
			pstmt.close();
			rs.close();
			return boardId;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean select_by_id() { // 얘는 자기의 게시물만 불러옴(내용 X)
		Connection con = InitDB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int cnt = 0;
		
		if(!VO.isAdmin()) {
			sql = String.format("SELECT * FROM board WHERE MemberId = '%s' ORDER BY BOARDID", VO.getMyID());	
		}else {
			sql = "SELECT * FROM board  ORDER BY BOARDID";			
		}
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("=====================================");
				System.out.println("조회수: " + rs.getString(8));
				System.out.println("\n게시물 아이디: " + rs.getString(1));
				System.out.println("제목: " + rs.getString(4));
				System.out.println("글쓴이 닉네임: " + rs.getString(3));
				System.out.println("마지막으로 쓴 날짜: " + rs.getString(6));
				System.out.println("\n=====================================");
				cnt++;
			}
		
			con.close();
			pstmt.close();
			rs.close();
			
			if(cnt == 0) {
				System.out.println("게시물이 없음!");
				return false;
			}else {
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void create(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		
		String insertSub = Exce.lengthExce(in, "제목을 입력하세요(1~50글자): ", "1글자 이상 50글자 이하로 작성해주세요!", 1, 50);
		String insertCon = Exce.lengthExce(in, "내용을 입력하세요(10~500글자): ", "10글자 이상 500글자 이하로 작성해주세요!", 10, 500);
		String insertPw = Exce.lengthExce(in, "비밀번호를 입력하세요(최대 8글자, 공백 시 공개): ", "4글자 이상 8글자 이하로 작성해주세요!", 0, 8);
		
		String sql = "{ call board_create(?, ?, ?, ?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, VO.getMyID());
			cstmt.setString(2, insertSub);
			cstmt.setString(3, insertCon);
			cstmt.setString(4, insertPw);
			
			int rowCount = cstmt.executeUpdate();
			
			System.out.println(rowCount > 0 ? "작성 완료" : "작성 실패");
			
			cstmt.close();
			con.close();
		}catch(Exception e) {
			System.out.println("작성 실패");
			e.printStackTrace();
		}
	}
	
	public void update(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		String[] colunmList = {"Subject", "Contents"};
		
		if(!select_by_id()) return;
		System.out.print("수정할 게시판의 번호: ");
		String boardId = in.nextLine();
		String boardValue;
		String boardColumn = Exce.updateColumnSelectExce(in, colunmList, "1. Subject\n2. Contents\n>> ", colunmList.length); // 컬럼 오류 제어
		if(colunmList[0].equals(boardColumn)) {
			boardValue = Exce.lengthExce(in, "제목을 입력하세요(1~50글자): ", "1글자 이상 50글자 이하로 작성해주세요!", 1, 50);
		}else {
			boardValue = Exce.lengthExce(in, "내용을 입력하세요(10~500글자): ", "10글자 이상 500글자 이하로 작성해주세요!", 10, 500);
		}
		
		String sql = "{ call board_update(?,?,?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, boardId);
			cstmt.setString(2, boardColumn);
			cstmt.setString(3, boardValue);
			
			int rowCount = cstmt.executeUpdate();

			System.out.println(rowCount > 0 ? "수정 완료" : "수정 실패");
			
			con.close();
			cstmt.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		
		if(!select_by_id()) return;
		System.out.print("삭제할 게시판의 번호를 입력하세요 :");
		String boardDelete = in.nextLine();
		
		String sql = "{ call board_delete(?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, boardDelete);
			
			int rowCount = cstmt.executeUpdate();
			
			System.out.println(rowCount > 0 ? "삭제 완료" : "삭제 실패");
			
			cstmt.close();
			con.close();
			
		}catch(Exception e) {
			System.out.println("삭제 실패");
			e.printStackTrace();
		}
	}
}
