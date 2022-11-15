package svc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

import db.InitDB;
import vo.VO;

public class User {
	public boolean login(Scanner in) {
		boolean result;
		Connection con = null;
		Statement st = null;
		
		try {
			con = InitDB.getConnection();
			st = con.createStatement();
			
			System.out.print("아이디: ");
			String id = in.nextLine();
			System.out.print("비밀번호: ");
			String pw = in.nextLine();
			
			String sql = String.format("SELECT * FROM MemberBoard WHERE MemberId = '%s' and MemberPw = '%s'", id, pw);
			ResultSet rs = st.executeQuery(sql);
			
			result = rs.next();
			
			con.close();
			st.close();
			rs.close();
			
			if(result) {
				VO.setMyID(id);
				System.out.print("\n로그인 완료  ");
				System.out.println(VO.isAdmin() ? "어드민" : "일반유저");
			}else System.out.println("\n로그인 실패! 다시 입력해 주세요.\n");
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("\n로그인 실패! 다시 입력해 주세요.\n");
			return false;
		}
	}
	
	public void register(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		
		String id = Exce.lengthExce(in, "아이디(4~15글자): ", "4글자 이상 15글자 이하로 작성해주세요!", 4, 15);
		String pw = Exce.lengthExce(in, "비밀번호(4~16글자): ", "4글자 이상 16글자 이하로 작성해주세요!", 4, 16);
		String nick = Exce.lengthExce(in, "닉네임(4~8글자): ", "4글자 이상 8글자 이하로 작성해주세요!", 4, 8);
		String birth = Exce.lengthExce(in, "생년월일('ex) 030321'): ", "형식에 맞게 입력해주세요!", 6, 6);
		String email = Exce.lengthExce(in, "이메일: ", "올바른 이메일 형식이 아닙니다!", -1, -1);
		String intro = Exce.lengthExce(in, "한줄 자기소개(5~30글자): ", "5글자 이상 30글자 이하로 작성해주세요!", 5, 30);
		
		String sql = "{ call user_create(?, ?, ?, ?, ?, ?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.setString(2, pw);
			cstmt.setString(3, nick);
			cstmt.setString(4, birth);
			cstmt.setString(5, email);
			cstmt.setString(6, intro);
			
			int rowCount = cstmt.executeUpdate();
			
			if(rowCount == 1) System.out.println("\n유저 회원가입 완료!\n");
			
			cstmt.close();
			con.close();
		} catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("\n이미 있는 아이디이여서 회원가입 실패!\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void select(Scanner in) {
		Connection con = InitDB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = String.format("SELECT * FROM MemberBoard WHERE MemberId = '%s'", VO.getMyID());
		String sql_A = "SELECT * FROM MemberBoard";
		
		try {
			pstmt = con.prepareStatement(VO.isAdmin() ? sql_A : sql);
			rs = pstmt.executeQuery();
			
			System.out.println("================================================================================");
			while(rs.next()) {
				System.out.printf("아이디: %s      닉네임: %s      생년월일: %s      이메일: %s      자기소개: %s\n", 
						rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
			}
			System.out.println("================================================================================");
		
			con.close();
			pstmt.close();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		String updateId;
		String updateValue;
		String[] colunmList = {"MemberPw", "MemberNickname", "MemberBirth", "MemberEmail", "MemberIntro"};
		String msg = "1. MemberPw\n2. MemberNickname\n3. MemberBirth\n4. MemberEmail\n5. MemberIntro\n>> ";
		
		if(VO.isAdmin()) {
			select(in);
			System.out.print("수정할 아이디: ");
			updateId = in.nextLine();
		}else updateId = VO.getMyID();
		
		String updateColumn = Exce.updateColumnSelectExce(in, colunmList, msg, colunmList.length); // 컬럼 오류 제어
		
		if(updateColumn.equals(colunmList[0])) { // 패스워드
			updateValue = Exce.lengthExce(in, "수정할 비밀번호(4~16글자): ", "4글자 이상 16글자 이하로 작성해주세요!", 4, 16);
		}else if(updateColumn.equals(colunmList[1])) { // 닉네임
			updateValue = Exce.lengthExce(in, "수정할 닉네임(4~8글자): ", "4글자 이상 8글자 이하로 작성해주세요!", 4, 8);
		}else if(updateColumn.equals(colunmList[2])) { // 생년월일
			updateValue = Exce.lengthExce(in, "수정할 생년월일('ex) 030321'): ", "형식에 맞게 입력해주세요!", 6, 6);
		}else if(updateColumn.equals(colunmList[3])) { // 이메일
			updateValue = Exce.lengthExce(in, "수정할 이메일: ", "올바른 이메일 형식이 아닙니다!", -1, -1);
		}else{ // 자기소개
			updateValue = Exce.lengthExce(in, "수정할 한줄 자기소개(5~30글자): ", "5글자 이상 30글자 이하로 작성해주세요!", 5, 30);
		}
			
		String sql = "{ call user_update(?, ?, ?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, updateId);
			cstmt.setString(2, updateColumn);
			cstmt.setString(3, updateValue);

			int rowCount = cstmt.executeUpdate();
			System.out.println(rowCount > 0 ? "수정 완료" : "수정 실패");
			
			cstmt.close();
			con.close();
		}catch(Exception e) {
			System.out.println("수정 실패");
			e.printStackTrace();
		}
	}
	
	public void delete(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		String sql = "{ call user_delete(?) }";
		String id_A = null;
		
		if(VO.isAdmin()) {
			select(in);
			System.out.print("삭제할 아이디 입력: ");
			id_A = in.nextLine();
		}
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, VO.isAdmin() ? id_A : VO.getMyID());
			
			int rowCount = cstmt.executeUpdate();
			
			System.out.println(rowCount > 0 ? "삭제 완료" : "삭제 실패");
			
			cstmt.close();
			con.close();
			
			VO.setMyID(id_A == null ? null : id_A.equals("admin") ? null : VO.getMyID());
		}catch(Exception e) {
			System.out.println("삭제 실패");
			e.printStackTrace();
		}
	}
}
