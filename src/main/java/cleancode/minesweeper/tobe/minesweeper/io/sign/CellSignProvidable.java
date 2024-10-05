package cleancode.minesweeper.tobe.minesweeper.io.sign;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;

public interface CellSignProvidable {

	boolean supports(CellSnapshot cellSnapShot);

	String provide(CellSnapshot cellSnapShot);
}
