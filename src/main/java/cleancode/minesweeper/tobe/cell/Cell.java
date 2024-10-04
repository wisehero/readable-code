package cleancode.minesweeper.tobe.cell;

public interface Cell {

	boolean isLandMine();

	boolean hasLandMineCount();

	CellSnapShot getSnapShot();

	void flag();

	void open();

	boolean isChecked();

	boolean isOpened();
}
