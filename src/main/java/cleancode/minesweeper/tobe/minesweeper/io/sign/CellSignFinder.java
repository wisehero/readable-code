package cleancode.minesweeper.tobe.minesweeper.io.sign;

import java.util.List;

import cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot;

public class CellSignFinder {
	private static final List<CellSignProvidable> CELL_SIGN_PROVIDERS = List.of(
		new NumberCellSignProvider(),
		new LandMineCellSignProvider(),
		new UncheckedCellSignProvider(),
		new EmptyCellSignProvider(),
		new FlagCellSignProvider()
	);

	public String findCellSignFrom(CellSnapshot cellSnapshot) {
		return CELL_SIGN_PROVIDERS.stream()
			.filter(provider -> provider.supports(cellSnapshot))
			.findFirst()
			.map(provider -> provider.provide(cellSnapshot))
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀입니다."));
	}
}
