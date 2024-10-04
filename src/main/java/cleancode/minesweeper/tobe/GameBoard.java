package cleancode.minesweeper.tobe;

import java.util.List;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.Cells;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.position.CellPositions;
import cleancode.minesweeper.tobe.position.RelativePosition;

public class GameBoard {

	private final Cell[][] board;
	private final int landMineCount;

	public GameBoard(GameLevel gameLevel) {
		board = new Cell[gameLevel.getRowSize()][gameLevel.getColSize()];
		landMineCount = gameLevel.getLandMineCount();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();
	}

	public void openAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	public void openSurroundedCells(CellPosition cellPosition) {
		if (isOpenedCellAt(cellPosition)) {
			return;
		}
		if (isLandMineCellAt(cellPosition)) {
			return;
		}

		openAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		calculateSurroundedPositions(cellPosition, getRowSize(), getColSize())
			.forEach(this::openSurroundedCells);
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		return findCell(cellPosition).hasLandMineCount();
	}

	private boolean isOpenedCellAt(CellPosition cellPosition) {
		return findCell(cellPosition).isOpened();
	}

	public boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	public boolean isAllCellChecked() {
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int colSize = getColSize();
		int rowSize = getRowSize();

		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	public void initializeGame() {
		CellPositions cellPositions = CellPositions.from(board);

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMinePositions);

		List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
		initializeNumberCells(numberPositionCandidates);
	}

	private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
		for (CellPosition candidate : numberPositionCandidates) {
			int count = countNearbyLandMines(candidate);
			if (count != 0) {
				updateCellAt(candidate, new NumberCell(count));
			}
		}
	}

	private void initializeLandMineCells(List<CellPosition> landMinePositions) {
		for (CellPosition position : landMinePositions) {
			updateCellAt(position, new LandMineCell());
		}
	}

	private void initializeEmptyCells(CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for (CellPosition position : allPositions) {
			updateCellAt(position, new LandMineCell());
		}
	}

	private void updateCellAt(CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColIndex()] = cell;
	}

	public String getSign(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.getSign();
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	private int countNearbyLandMines(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		return (int)calculateSurroundedPositions(cellPosition, rowSize, colSize).stream()
			.filter(this::isLandMineCellAt)
			.count();
	}

	private static List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize,
		int colSize) {
		return RelativePosition.SURROUNDED_RELATIVE_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}
}
