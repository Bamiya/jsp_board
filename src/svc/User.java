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
			
			System.out.print("���̵�: ");
			String id = in.nextLine();
			System.out.print("��й�ȣ: ");
			String pw = in.nextLine();
			
			String sql = String.format("SELECT * FROM MemberBoard WHERE MemberId = '%s' and MemberPw = '%s'", id, pw);
			ResultSet rs = st.executeQuery(sql);
			
			result = rs.next();
			
			con.close();
			st.close();
			rs.close();
			
			if(result) {
				VO.setMyID(id);
				System.out.print("\n�α��� �Ϸ�  ");
				System.out.println(VO.isAdmin() ? "����" : "�Ϲ�����");
			}else System.out.println("\n�α��� ����! �ٽ� �Է��� �ּ���.\n");
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("\n�α��� ����! �ٽ� �Է��� �ּ���.\n");
			return false;
		}
	}
	
	public void register(Scanner in) {
		Connection con = InitDB.getConnection();
		CallableStatement cstmt = null;
		
		String id = Exce.lengthExce(in, "���̵�(4~15����): ", "4���� �̻� 15���� ���Ϸ� �ۼ����ּ���!", 4, 15);
		String pw = Exce.lengthExce(in, "��й�ȣ(4~16����): ", "4���� �̻� 16���� ���Ϸ� �ۼ����ּ���!", 4, 16);
		String nick = Exce.lengthExce(in, "�г���(4~8����): ", "4���� �̻� 8���� ���Ϸ� �ۼ����ּ���!", 4, 8);
		String birth = Exce.lengthExce(in, "�������('ex) 030321'): ", "���Ŀ� �°� �Է����ּ���!", 6, 6);
		String email = Exce.lengthExce(in, "�̸���: ", "�ùٸ� �̸��� ������ �ƴմϴ�!", -1, -1);
		String intro = Exce.lengthExce(in, "���� �ڱ�Ұ�(5~30����): ", "5���� �̻� 30���� ���Ϸ� �ۼ����ּ���!", 5, 30);
		
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
			
			if(rowCount == 1) System.out.println("\n���� ȸ������ �Ϸ�!\n");
			
			cstmt.close();
			con.close();
		} catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("\n�̹� �ִ� ���̵��̿��� ȸ������ ����!\n");
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
				System.out.printf("���̵�: %s      �г���: %s      �������: %s      �̸���: %s      �ڱ�Ұ�: %s\n", 
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
			System.out.print("������ ���̵�: ");
			updateId = in.nextLine();
		}else updateId = VO.getMyID();
		
		String updateColumn = Exce.updateColumnSelectExce(in, colunmList, msg, colunmList.length); // �÷� ���� ����
		
		if(updateColumn.equals(colunmList[0])) { // �н�����
			updateValue = Exce.lengthExce(in, "������ ��й�ȣ(4~16����): ", "4���� �̻� 16���� ���Ϸ� �ۼ����ּ���!", 4, 16);
		}else if(updateColumn.equals(colunmList[1])) { // �г���
			updateValue = Exce.lengthExce(in, "������ �г���(4~8����): ", "4���� �̻� 8���� ���Ϸ� �ۼ����ּ���!", 4, 8);
		}else if(updateColumn.equals(colunmList[2])) { // �������
			updateValue = Exce.lengthExce(in, "������ �������('ex) 030321'): ", "���Ŀ� �°� �Է����ּ���!", 6, 6);
		}else if(updateColumn.equals(colunmList[3])) { // �̸���
			updateValue = Exce.lengthExce(in, "������ �̸���: ", "�ùٸ� �̸��� ������ �ƴմϴ�!", -1, -1);
		}else{ // �ڱ�Ұ�
			updateValue = Exce.lengthExce(in, "������ ���� �ڱ�Ұ�(5~30����): ", "5���� �̻� 30���� ���Ϸ� �ۼ����ּ���!", 5, 30);
		}
			
		String sql = "{ call user_update(?, ?, ?) }";
		
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, updateId);
			cstmt.setString(2, updateColumn);
			cstmt.setString(3, updateValue);

			int rowCount = cstmt.executeUpdate();
			System.out.println(rowCount > 0 ? "���� �Ϸ�" : "���� ����");
			
			cstmt.close();
			con.close();
		}catch(Exception e) {
			System.out.println("���� ����");
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
			System.out.print("������ ���̵� �Է�: ");
			id_A = in.nextLine();
		}
		try {
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, VO.isAdmin() ? id_A : VO.getMyID());
			
			int rowCount = cstmt.executeUpdate();
			
			System.out.println(rowCount > 0 ? "���� �Ϸ�" : "���� ����");
			
			cstmt.close();
			con.close();
			
			VO.setMyID(id_A == null ? null : id_A.equals("admin") ? null : VO.getMyID());
		}catch(Exception e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}
}
