package cleancode.minesweeper.tobe.minesweeper.board.position;

public class CellPosition {

	private final int rowIndex;
	private final int colIndex;

	public CellPosition(int rowIndex, int colIndex) {
		if (rowIndex < 0 || colIndex < 0) {
			throw new IllegalArgumentException("올바르지 않은 좌표입니다.");
		}
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public static CellPosition of(int rowIndex, int colIndex) {
		return new CellPosition(rowIndex, colIndex);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CellPosition that = (CellPosition)o;
		return rowIndex == that.rowIndex && colIndex == that.colIndex;
	}

	@Override
	public int hashCode() {
		int result = rowIndex;
		result = 31 * result + colIndex;
		return result;
	}

	public boolean isColIndexMoreThanOrEqual(int colIndex) {
		return this.colIndex >= colIndex;
	}

	public boolean isRowIndexMoreThanOrEqual(int rowIndex) {
		return this.rowIndex >= rowIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public CellPosition calculatePositionBy(RelativePosition relativePosition) {
		if (this.canCalculatePositionBy(relativePosition)) {
			throw new IllegalArgumentException("올바르지 않은 좌표입니다.");
		}

		return CellPosition.of(rowIndex + relativePosition.getDeltaRow(), colIndex + relativePosition.getDeltaCol());
	}

	public boolean canCalculatePositionBy(RelativePosition relativePosition) {
		return rowIndex + relativePosition.getDeltaRow() >= 0 && colIndex + relativePosition.getDeltaCol() >= 0;
	}

	public boolean isRowIndexLessThan(int rowSize) {
		return this.rowIndex < rowSize;
	}

	public boolean isColIndexLessThan(int colSize) {
		return this.colIndex < colSize;
	}
}
