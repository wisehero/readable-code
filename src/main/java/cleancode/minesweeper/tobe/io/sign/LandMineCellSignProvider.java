package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;

public class LandMineCellSignProvider implements CellSignProvidable {

	private static final String LAND_MINE_SIGN = "☼";

	@Override
	public boolean supports(CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(CellSnapshotStatus.LAND_MINE);
	}

	@Override
	public String provide(CellSnapshot cellSnapShot) {
		return LAND_MINE_SIGN;
	}

}
