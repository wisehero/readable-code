package cleancode.studycafe.tobe.model.order;

import java.util.Optional;

import cleancode.studycafe.tobe.model.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;

public class StudyCafePassOrder {

	private final StudyCafeSeatPass seatPass;
	private final StudyCafeLockerPass lockerPass;

	public StudyCafePassOrder(StudyCafeSeatPass seatPass, StudyCafeLockerPass lockerPass) {
		this.seatPass = seatPass;
		this.lockerPass = lockerPass;
	}

	public static StudyCafePassOrder of(StudyCafeSeatPass seatPass, StudyCafeLockerPass lockerPass) {
		return new StudyCafePassOrder(seatPass, lockerPass);
	}

	public int getDiscountPrice() {
		return seatPass.getDiscountPrice();
	}

	public int getTotalPrice() {
		int lockerPassPrice = lockerPass == null ? 0 : lockerPass.getPrice();
		int totalPassPrice = seatPass.getPrice() + lockerPassPrice;
		return totalPassPrice - getDiscountPrice();
	}

	public StudyCafeSeatPass getSeatPass() {
		return this.seatPass;
	}

	public Optional<StudyCafeLockerPass> getLockerPass() {
		return Optional.ofNullable(lockerPass);
	}
}
