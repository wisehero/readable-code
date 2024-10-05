package cleancode.minesweeper.tobe.minesweeper.board.cell;

import java.util.Objects;

public class CellSnapshot {

	private final CellSnapshotStatus status;
	private final int nearbyLandMineCount;

	public CellSnapshot(CellSnapshotStatus status, int nearbyLandMineCount) {
		this.status = status;
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	public static CellSnapshot of(CellSnapshotStatus status, int nearbyLandMineCount) {
		return new CellSnapshot(status, nearbyLandMineCount);
	}

	public static CellSnapshot ofEmpty() {
		return of(CellSnapshotStatus.EMPTY, 0);
	}

	public static CellSnapshot ofFlag() {
		return of(CellSnapshotStatus.FLAG, 0);
	}

	public static CellSnapshot ofLandMine() {
		return of(CellSnapshotStatus.LAND_MINE, 0);
	}

	public static CellSnapshot ofNumber(int nearbyLandMineCount) {
		return CellSnapshot.of(CellSnapshotStatus.NUMBER, nearbyLandMineCount);
	}

	public static CellSnapshot ofUnchecked() {
		return of(CellSnapshotStatus.UNCHECKED, 0);
	}

	public CellSnapshotStatus getStatus() {
		return status;
	}

	public int getNearbyLandMineCount() {
		return nearbyLandMineCount;
	}

	public boolean isSameStatus(CellSnapshotStatus cellSnapshotStatus) {
		return this.status == cellSnapshotStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CellSnapshot snapShot = (CellSnapshot)o;
		return nearbyLandMineCount == snapShot.nearbyLandMineCount && status == snapShot.status;
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(status);
		result = 31 * result + nearbyLandMineCount;
		return result;
	}
}
