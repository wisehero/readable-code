package cleancode.minesweeper.tobe.cell;

public enum CellSnapshotStatus {
	EMPTY("□빈 셀"),
	FLAG("⚑깃발"),
	LAND_MINE("💥"),
	NUMBER("숫자 셀 "),
	UNCHECKED("체크되지 않은 셀");

	private final String description;

	CellSnapshotStatus(String description) {
		this.description = description;
	}
}
