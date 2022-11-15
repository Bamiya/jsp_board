package controller;

import java.util.Scanner;
import svc.Board;
import svc.Comment;
import svc.User;

public class Controller {
	User user = new User();
	Board board = new Board();
	Comment comment = new Comment();
	
	public void register(Scanner in) {
		System.out.println("- 회원가입 -\n");
		user.register(in);
	}
	
	public boolean login(Scanner in) {
		System.out.println("- 로그인 -\n");
		return user.login(in);
	}
	
	public void user_select(Scanner in) {
		System.out.println("- 유저 정보 조회 -\n");
		user.select(in);
	}
	
	public void user_update(Scanner in) {
		System.out.println("- 유저 정보 수정 -\n");
		user.update(in);
	}
	
	public void board_select(Scanner in) {
		System.out.println("- 게시물 조회 -\n");
		board.onlySelect(in);
	}
	
	public void board_create(Scanner in) {
		System.out.println("- 게시물 생성 -\n");
		board.create(in);
	}
	
	public void board_update(Scanner in) {
		System.out.println("- 게시물 업데이트 -\n");
		board.update(in);
	}
	
	public void board_delete(Scanner in) {
		System.out.println("- 게시물 삭제 -\n");
		board.delete(in);
	}
	
	public void comment_create(Scanner in) {
		System.out.println("- 댓글 생성 -\n");
		comment.create(in);
	}
	
	public void comment_delete(Scanner in) {
		System.out.println("- 댓글 삭제 -\n");
		comment.delete(in);
	}
	
	public void user_delete(Scanner in) {
		System.out.println("- 유저 탈퇴 -\n");
		user.delete(in);
	}
}
