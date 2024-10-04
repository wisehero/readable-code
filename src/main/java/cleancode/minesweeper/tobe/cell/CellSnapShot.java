package cleancode.minesweeper.tobe.cell;

import java.util.Objects;

public class CellSnapShot {

	private final CellSnapshotStatus status;
	private final int nearbyLandMineCount;

	public CellSnapShot(CellSnapshotStatus status, int nearbyLandMineCount) {
		this.status = status;
		this.nearbyLandMineCount = nearbyLandMineCount;
	}

	public static CellSnapShot of(CellSnapshotStatus status, int nearbyLandMineCount) {
		return new CellSnapShot(status, nearbyLandMineCount);
	}

	public static CellSnapShot ofEmpty() {
		return of(CellSnapshotStatus.EMPTY, 0);
	}

	public static CellSnapShot ofFlag() {
		return of(CellSnapshotStatus.FLAG, 0);
	}

	public static CellSnapShot ofLandMine() {
		return of(CellSnapshotStatus.LAND_MINE, 0);
	}

	public static CellSnapShot ofNumber(int nearbyLandMineCount) {
		return CellSnapShot.of(CellSnapshotStatus.NUMBER, nearbyLandMineCount);
	}

	public static CellSnapShot ofUnchecked() {
		return of(CellSnapshotStatus.UNCHECKED, 0);
	}

	public CellSnapshotStatus getStatus() {
		return status;
	}

	public int getNearbyLandMineCount() {
		return nearbyLandMineCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CellSnapShot snapShot = (CellSnapShot)o;
		return nearbyLandMineCount == snapShot.nearbyLandMineCount && status == snapShot.status;
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(status);
		result = 31 * result + nearbyLandMineCount;
		return result;
	}
}
