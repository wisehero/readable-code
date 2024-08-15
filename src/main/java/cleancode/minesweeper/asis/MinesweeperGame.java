package cleancode.minesweeper.asis;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

	private static final int BOARD_ROW_SIZE = 8;
	public static final int BOARD_COL_SIZE = 10;
	private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
	private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
	private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
	private static final int LAND_MINE_COUNT = 10;
	public static final String FLAG_SIGN = "⚑";
	public static final String LAND_MINE_SIGN = "☼";
	public static final String CLOSED_CELL_SIGN = "□";
	public static final String OPENED_CELL_SIGN = "■";

	private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public static void main(String[] args) {
		showGameStartComments();
		Scanner scanner = new Scanner(System.in);
		initializeGame();
		while (true) {
			System.out.println("   a b c d e f g h i j");
			showBoard();
			if (doesUserWinTheGame()) {
				System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
				break;
			}
			if (doesUserLostTheGame()) {
				System.out.println("지뢰를 밟았습니다. GAME OVER!");
				break;
			}

			String cellInput = getCellInputFromUser(scanner);
			String userAction = getUserActionInputFromUser(scanner);

			int selectedColumnIndex = getSelectedColumnIndex(cellInput);
			int selectedRowIndex = getSelectedRowIndex(cellInput);

			if (doesUserChooseToPlantFlag(userAction)) {
				BOARD[selectedRowIndex][selectedColumnIndex] = FLAG_SIGN;
				checkIfGameIsOver();
			} else if (doesUserChooseToOpenCell(userAction)) {
				if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
					BOARD[selectedRowIndex][selectedColumnIndex] = LAND_MINE_SIGN;
					changeGameStatusToLose();
					continue;
				} else {
					open(selectedRowIndex, selectedColumnIndex);
				}
				checkIfGameIsOver();

			} else {
				System.out.println("잘못된 번호를 선택하셨습니다.");
			}
		}
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
		return LAND_MINES[selectedRowIndex][selectedColumnIndex];
	}

	private static int getSelectedRowIndex(String cellInput) {
		char cellInputRow = cellInput.charAt(1);
		return convertRowFrom(cellInputRow);
	}

	private static int getSelectedColumnIndex(String cellInput) {
		char cellInputCol = cellInput.charAt(0);
		return convertColumnFrom(cellInputCol);
	}

	private static String getUserActionInputFromUser(Scanner scanner) {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
		return scanner.nextLine();
	}

	private static String getCellInputFromUser(Scanner scanner) {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
		return scanner.nextLine();
	}

	private static boolean doesUserLostTheGame() {
		return gameStatus == -1;
	}

	private static boolean doesUserWinTheGame() {
		return gameStatus == 1;
	}

	private static void checkIfGameIsOver() {
		boolean isAllOpened = isAllCellOpened();
		if (isAllOpened) {
			gameStatus = 1;
		}
	}

	private static boolean isAllCellOpened() {
		boolean isAllOpened = true;
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_COL_SIZE; col++) {
				if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
					isAllOpened = false;
				}
			}
		}
		return isAllOpened;
	}

	private static int convertRowFrom(char cellInputRow) {
		return Character.getNumericValue(cellInputRow) - 1;
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
			default -> -1;
		};
	}

	private static void showBoard() {
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			System.out.printf("%d  ", row + 1);
			for (int col = 0; col < BOARD_COL_SIZE; col++) {
				System.out.print(BOARD[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void initializeGame() {
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_ROW_SIZE; col++) {
				BOARD[row][col] = CLOSED_CELL_SIGN;
			}
		}
		for (int i = 0; i < LAND_MINE_COUNT; i++) {
			int col = new Random().nextInt(10);
			int row = new Random().nextInt(8);
			LAND_MINES[row][col] = true;
		}
		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_COL_SIZE; col++) {
				int count = 0;
				if (!isLandMineCell(row, col)) {
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
					NEARBY_LAND_MINE_COUNTS[row][col] = count;
					continue;
				}
				NEARBY_LAND_MINE_COUNTS[row][col] = 0;
			}
		}
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
		if (!BOARD[row][col].equals("□")) {
			return;
		}
		if (isLandMineCell(row, col)) {
			return;
		}
		if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
			BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
			return;
		} else {
			BOARD[row][col] = OPENED_CELL_SIGN;
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
