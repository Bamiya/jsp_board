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
		System.out.println("- ȸ������ -\n");
		user.register(in);
	}
	
	public boolean login(Scanner in) {
		System.out.println("- �α��� -\n");
		return user.login(in);
	}
	
	public void user_select(Scanner in) {
		System.out.println("- ���� ���� ��ȸ -\n");
		user.select(in);
	}
	
	public void user_update(Scanner in) {
		System.out.println("- ���� ���� ���� -\n");
		user.update(in);
	}
	
	public void board_select(Scanner in) {
		System.out.println("- �Խù� ��ȸ -\n");
		board.onlySelect(in);
	}
	
	public void board_create(Scanner in) {
		System.out.println("- �Խù� ���� -\n");
		board.create(in);
	}
	
	public void board_update(Scanner in) {
		System.out.println("- �Խù� ������Ʈ -\n");
		board.update(in);
	}
	
	public void board_delete(Scanner in) {
		System.out.println("- �Խù� ���� -\n");
		board.delete(in);
	}
	
	public void comment_create(Scanner in) {
		System.out.println("- ��� ���� -\n");
		comment.create(in);
	}
	
	public void comment_delete(Scanner in) {
		System.out.println("- ��� ���� -\n");
		comment.delete(in);
	}
	
	public void user_delete(Scanner in) {
		System.out.println("- ���� Ż�� -\n");
		user.delete(in);
	}
}
