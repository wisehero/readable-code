package cleancode.minesweeper.tobe.io.sign;

import cleancode.minesweeper.tobe.cell.CellSnapshot;

public interface CellSignProvidable {

	boolean supports(CellSnapshot cellSnapShot);

	String provide(CellSnapshot cellSnapShot);
}
