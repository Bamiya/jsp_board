package svc;

import java.util.Scanner;

public class Exce {
	
	final static String REG = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"; //이메일 유효성검사
	
	static public String lengthExce(Scanner in, String msg, String errMsg, int min, int max) {
		String str;
		
		while(true) {
			if(min == -1 && max == -1) { // 이메일만 해당
				System.out.print(msg);
				str = in.nextLine();
				
				if(str.matches(REG)) return str;
				else System.out.println(errMsg);
			}
			
			System.out.print(msg);
			str = in.nextLine();
			
			if(str.length() >= min && str.length() <= max) {
				if(min == max) { // 생년월일만 해당
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
		
		System.out.println("수정할 목록");
		
		while(true) {
			System.out.print(msg);
			try {
				int ch = Integer.parseInt(in.nextLine());
				if(ch >= 1 && ch <= lastN) return ColumnArr[ch - 1];
			}catch(Exception e) {
				System.out.println("1~" + lastN + "까지 숫자로 입력해주세요!");
				continue;
			}
			System.out.println("1~" + lastN + "까지 숫자로 입력해주세요!");
		}	
	}
}
