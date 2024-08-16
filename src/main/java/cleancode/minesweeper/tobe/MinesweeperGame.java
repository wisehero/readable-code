package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

	private static final int BOARD_ROW_SIZE = 8;
	public static final int BOARD_COL_SIZE = 10;
	public static final Scanner SCANNER = new Scanner(System.in);
	private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
	private static final int LAND_MINE_COUNT = 10;

	private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public static void main(String[] args) {
		showGameStartComments();
		initializeGame();

		while (true) {

			try {
				showBoard();

				if (doesUserWinTheGame()) {
					System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
					break;
				}
				if (doesUserLostTheGame()) {
					System.out.println("지뢰를 밟았습니다. GAME OVER!");
					break;
				}

				String cellInput = getCellInputFromUser();
				String userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (AppException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("프로그램에 문제가 생겼습니다.");
			}
		}
	}

	private static void actOnCell(String cellInput, String userActionInput) {
		int selectedColumnIndex = getSelectedColumnIndex(cellInput);
		int selectedRowIndex = getSelectedRowIndex(cellInput);

		if (doesUserChooseToPlantFlag(userActionInput)) {
			BOARD[selectedRowIndex][selectedColumnIndex].flag();
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
				BOARD[selectedRowIndex][selectedColumnIndex].open();
				changeGameStatusToLose();
				return;
			}

			open(selectedRowIndex, selectedColumnIndex);
			checkIfGameIsOver();
			return;
		}
		throw new AppException("잘못된 번호를 선택하셨습니다.");
	}

	private static boolean doesUserChooseToOpenCell(String userAction) {
		return userAction.equals("1");
	}

	private static boolean doesUserChooseToPlantFlag(String userAction) {
		return userAction.equals("2");
	}

	private static void changeGameStatusToLose() {
		gameStatus = -1;
	}

	private static boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
		return BOARD[selectedRowIndex][selectedColumnIndex].isLandMine();
	}

	private static int getSelectedRowIndex(String cellInput) {
		char cellInputRow = cellInput.charAt(1);
		return convertRowFrom(cellInputRow);
	}

	private static int getSelectedColumnIndex(String cellInput) {
		char cellInputCol = cellInput.charAt(0);
		return convertColumnFrom(cellInputCol);
	}

	private static String getUserActionInputFromUser() {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
		return SCANNER.nextLine();
	}

	private static String getCellInputFromUser() {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
		return SCANNER.nextLine();
	}

	private static boolean doesUserLostTheGame() {
		return gameStatus == -1;
	}

	private static boolean doesUserWinTheGame() {
		return gameStatus == 1;
	}

	private static void checkIfGameIsOver() {
		boolean isAllChecked = isAllCellChecked();
		if (isAllChecked) {
			gameStatus = 1;
		}
	}

	private static boolean isAllCellChecked() {
		return Arrays.stream(BOARD)
			.flatMap(Arrays::stream)
			.allMatch(Cell::isChecked);
	}

	private static int convertRowFrom(char cellInputRow) {
		int rowIndex = Character.getNumericValue(cellInputRow) - 1;
		if (rowIndex >= BOARD_ROW_SIZE) {
			throw new AppException("잘못된 입력입니다.");
		}

		return rowIndex;
	}

	private static int convertColumnFrom(char cellInputCol) {
		return switch (cellInputCol) {
			case 'a' -> 0;
			case 'b' -> 1;
			case 'c' -> 2;
			case 'd' -> 3;
			case 'e' -> 4;
			case 'f' -> 5;
			case 'g' -> 6;
			case 'h' -> 7;
			case 'i' -> 8;
			case 'j' -> 9;
			default -> throw new AppException("잘못된 값입니다.");
		};
	}

	private static void showBoard() {
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			System.out.printf("%d  ", row + 1);
			for (int col = 0; col < BOARD_COL_SIZE; col++) {
				System.out.print(BOARD[row][col].getSign() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void initializeGame() {
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_ROW_SIZE; col++) {
				BOARD[row][col] = Cell.create();
			}
		}

		for (int i = 0; i < LAND_MINE_COUNT; i++) {
			int col = new Random().nextInt(10);
			int row = new Random().nextInt(8);
			BOARD[row][col].turnOnLandMine();
		}

		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_COL_SIZE; col++) {
				if (isLandMineCell(row, col)) {
					continue;
				}
				int count = countNearByLandMines(row, col);
				BOARD[row][col].updateNearbyLandMineCount(count);
			}
		}
	}

	private static int countNearByLandMines(int row, int col) {
		int count = 0;
		if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
			count++;
		}
		if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
			count++;
		}
		if (row - 1 >= 0 && col + 1 < 10 && isLandMineCell(row - 1, col + 1)) {
			count++;
		}
		if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
			count++;
		}
		if (col + 1 < 10 && isLandMineCell(row, col + 1)) {
			count++;
		}
		if (row + 1 < 8 && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
			count++;
		}
		if (row + 1 < 8 && isLandMineCell(row + 1, col)) {
			count++;
		}
		if (row + 1 < 8 && col + 1 < 10 && isLandMineCell(row + 1, col + 1)) {
			count++;
		}
		return count;
	}

	private static void showGameStartComments() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("지뢰찾기 게임 시작!");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	private static void open(int row, int col) {
		if (row < 0 || row >= 8 || col < 0 || col >= 10) {
			return;
		}
		if (BOARD[row][col].isOpened()) {
			return;
		}
		if (isLandMineCell(row, col)) {
			return;
		}

		BOARD[row][col].open();

		if (BOARD[row][col].hasLandMineCount()) {
			return;
		}

		open(row - 1, col - 1);
		open(row - 1, col);
		open(row - 1, col + 1);
		open(row, col - 1);
		open(row, col + 1);
		open(row + 1, col - 1);
		open(row + 1, col);
		open(row + 1, col + 1);
	}
}
