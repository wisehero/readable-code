package cleancode.minesweeper.tobe.io;

import java.util.List;
import java.util.stream.IntStream;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;

public class ConsoleOutputHandler {

	public void showGameStartComments() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("지뢰찾기 게임 시작!");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	public void showBoard(GameBoard board) {
		String joiningAlphabet = generateColAlphabets(board);

		System.out.println("    " + joiningAlphabet);

		for (int row = 0; row < board.getRowSize(); row++) {
			System.out.printf("%2d  ", row + 1);
			for (int col = 0; col < board.getColSize(); col++) {
				System.out.print(board.getSign(row, col) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private String generateColAlphabets(GameBoard board) {
		List<String> alphabet = IntStream.range(0, board.getColSize())
			.mapToObj(index -> (char)('a' + index))
			.map(Object::toString)
			.toList();
		return String.join(" ", alphabet);
	}

	public void printGameWinningComment() {
		System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
	}

	public void printGameLosingComment() {
		System.out.println("지뢰를 밟았습니다. GAME OVER!");
	}

	public void printCommentForSelectingCell() {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
	}

	public void printCommentForUserAction() {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
	}

	public void printExceptionMessage(GameException e) {
		System.out.println(e.getMessage());
	}

	public void printSimpleMessage(String message) {
		System.out.println(message);
	}
}
