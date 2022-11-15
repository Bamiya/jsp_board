package svc;

import java.util.Scanner;

public class Exce {
	
	final static String REG = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"; //�̸��� ��ȿ���˻�
	
	static public String lengthExce(Scanner in, String msg, String errMsg, int min, int max) {
		String str;
		
		while(true) {
			if(min == -1 && max == -1) { // �̸��ϸ� �ش�
				System.out.print(msg);
				str = in.nextLine();
				
				if(str.matches(REG)) return str;
				else System.out.println(errMsg);
			}
			
			System.out.print(msg);
			str = in.nextLine();
			
			if(str.length() >= min && str.length() <= max) {
				if(min == max) { // ������ϸ� �ش�
					try {
						Integer.parseInt(str);
					}catch(Exception e) {
						System.out.println(errMsg);
						continue;
					}
				}
				break;
			}
			System.out.println(errMsg);
		}
		return str;
	}
	
	static public String updateColumnSelectExce(Scanner in, String[] ColumnArr, String msg, int lastN) {
		
		System.out.println("������ ���");
		
		while(true) {
			System.out.print(msg);
			try {
				int ch = Integer.parseInt(in.nextLine());
				if(ch >= 1 && ch <= lastN) return ColumnArr[ch - 1];
			}catch(Exception e) {
				System.out.println("1~" + lastN + "���� ���ڷ� �Է����ּ���!");
				continue;
			}
			System.out.println("1~" + lastN + "���� ���ڷ� �Է����ּ���!");
		}	
	}
}
