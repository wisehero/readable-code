package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;

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
