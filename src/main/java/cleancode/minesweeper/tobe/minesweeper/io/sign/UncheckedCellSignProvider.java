package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;
import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshotStatus;

public class UncheckedCellSignProvider implements CellSignProvidable {

	private static final String UNCHECKED_SIGN = "□";

	@Override
	public boolean supports(CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(CellSnapshotStatus.EMPTY);
	}

	@Override
	public String provide(CellSnapshot cellSnapShot) {
		return UNCHECKED_SIGN;
	}

}
