package cleancode.minesweeper.tobe.minesweeper.board;

import java.util.List;
import java.util.Stack;

import cleancode.minesweeper.tobe.minesweeper.board.cell.Cell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.Cells;
import cleancode.minesweeper.tobe.minesweeper.board.cell.EmptyCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.LandMineCell;
import cleancode.minesweeper.tobe.minesweeper.board.cell.NumberCell;
import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;

public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;
	private GameStatus gameStatus;

	public GameBoard(GameLevel gameLevel) {
		board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
		landMineCount = gameLevel.getLandMineCount();
		initializeGameStatus();
	}

	public void initializeGame() {
		CellPositions cellPositions = CellPositions.from(board);
		initializeGameStatus();

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMinePositions);

		List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
		initializeNumberCells(numberPositionCandidates);
	}

	public void openAt(CellPosition cellPosition) {
		if (isLandMineCellAt(cellPosition)) {
			openOneCellAt(cellPosition);
			changeGameStatusToLose();
			return;
		}
		openSurroundedCells(cellPosition);
		checkIfGameIsOver();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();

		checkIfGameIsOver();
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int colSize = getColSize();
		int rowSize = getRowSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	public boolean isInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}

	public boolean isWinStatus() {
		return gameStatus == GameStatus.WIN;
	}

	public boolean isLoseStatus() {
		return gameStatus == GameStatus.LOSE;
	}

	public CellSnapshot getSnapShot(CellPosition cellPosition) {
		return findCell(cellPosition).getSnapShot();
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	private void initializeGameStatus() {
		gameStatus = GameStatus.IN_PROGRESS;
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition position : allPositions) {
			updateCellAt(position, new EmptyCell());
		}
	}

	private void initializeLandMineCells(List<CellPosition> landMinePositions) {
		for (CellPosition position : landMinePositions) {
			updateCellAt(position, new LandMineCell());
		}
	}

	private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
		for (CellPosition candidate : numberPositionCandidates) {
			int count = countNearbyLandMines(candidate);
			if (count != 0) {
				updateCellAt(candidate, new NumberCell(count));
			}
		}
	}

	private int countNearbyLandMines(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return (int)calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
			.filter(this::isLandMineCellAt)
			.count();
	}

	private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize,
		int colSize) {
		return RelativePosition.SURROUNDED_RELATIVE_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}

	private void updateCellAt(CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColIndex()] = cell;
	}

	private void openSurroundedCells(CellPosition cellPosition) {
		Stack<CellPosition> stack = new Stack<>();
		stack.push(cellPosition);

		while (!stack.isEmpty()) {
			openAndPushCellAt(stack);
		}

	}

	private void openAndPushCellAt(Stack<CellPosition> stack) {
		CellPosition currentCellPosition = stack.pop();
		if (isOpenedCell(currentCellPosition)) {
			return;
		}
		if (isLandMineCellAt(currentCellPosition)) {
			return;
		}

		openOneCellAt(currentCellPosition);

		if (doesCellHaveLandMineCount(currentCellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculateSurroundedPositions(currentCellPosition, getRowSize(),
			getColSize());
		for (CellPosition surroundedPosition : surroundedPositions) {
			stack.push(surroundedPosition);
		}
	}

	private void openOneCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		return findCell(cellPosition).isOpened();
	}

	private boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		return findCell(cellPosition).hasLandMineCount();
	}

	private void checkIfGameIsOver() {
		if (isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private boolean isAllCellChecked() {
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

}
