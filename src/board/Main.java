package board;

import java.util.Scanner;

import controller.Controller;
import vo.VO;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String inp = null;
		Controller controller = new Controller();
		
		System.out.println("< 미니 게시판 >\n");
		
		do {
			System.out.print("1. 회원가입    ");
			System.out.print("2. 로그인    ");
			System.out.print("3. 종료");
			
			System.out.print("\n>> ");
			inp = in.nextLine();
			
			if(inp.equals("1")) controller.register(in);
			else if(inp.equals("2") && controller.login(in)) {
				MENU : while(true) {


					System.out.println("\n======= 메뉴 ======= ");
					System.out.println("1.   유저 조회");
					System.out.println("2.   유저 수정");
					System.out.println("-----------------------------");
					System.out.println("3.   게시판 조회");
					System.out.println("4.   게시판 생성");
					System.out.println("5.   게시판 수정");
					System.out.println("6.   게시판 삭제");
					System.out.println("-----------------------------");
					System.out.println("7.   댓글 생성");
					System.out.println("8.   댓글 삭제");
					System.out.println("-----------------------------");
					System.out.println("9.   회원 탈퇴");
					System.out.print("10.   로그아웃");
					
					System.out.print("\n>> ");
					inp = in.nextLine();
					
					switch(inp) {
					case "1":
						controller.user_select(in);
						break;
					case "2":
						controller.user_update(in);
						break;
					case "3":
						controller.board_select(in);
						break;
					case "4":
						controller.board_create(in);
						break;
					case "5":
						controller.board_update(in);
						break;
					case "6":
						controller.board_delete(in);
						break;
					case "7":
						controller.comment_create(in);
						break;
					case "8":
						controller.comment_delete(in);
						break;
					case "9":
						controller.user_delete(in);
						if(VO.getMyID() == null) break MENU;
						break;
					case "10":
						VO.setMyID(null);
						break MENU;
					}
				}
				System.out.println("\n로그아웃 완료\n");
			}
			else if(inp.equals("3")) break;
		}while(true);
	}
}