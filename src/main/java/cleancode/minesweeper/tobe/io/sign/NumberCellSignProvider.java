package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;

public class NumberCellSignProvider implements CellSignProvidable {

	@Override
	public boolean supports(CellSnapshot cellSnapShot) {
		return cellSnapShot.isSameStatus(CellSnapshotStatus.NUMBER);
	}
	@Override
	public String provide(CellSnapshot cellSnapShot) {
		return String.valueOf(cellSnapShot.getNearbyLandMineCount());
	}

}
