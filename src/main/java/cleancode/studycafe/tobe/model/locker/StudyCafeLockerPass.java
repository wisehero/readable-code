package cleancode.studycafe.tobe.model.locker;

import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafePass;

public class StudyCafeLockerPass implements StudyCafePass {

	private final StudyCafePassType passType;
	private final int duration;
	private final int price;

	private StudyCafeLockerPass(StudyCafePassType passType, int duration, int price) {
		this.passType = passType;
		this.duration = duration;
		this.price = price;
	}

	public static StudyCafeLockerPass of(StudyCafePassType passType, int duration, int price) {
		return new StudyCafeLockerPass(passType, duration, price);
	}

	public boolean isSameDuration(int duration) {
		return this.duration == duration;
	}

	public boolean isSamePassType(StudyCafePassType passType) {
		return this.passType == passType;
	}

	@Override
	public StudyCafePassType getPassType() {
		return passType;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public int getPrice() {
		return price;
	}

	public String display() {
		if (passType == StudyCafePassType.HOURLY) {
			return String.format("%s시간권 - %d원", duration, price);
		}
		if (passType == StudyCafePassType.WEEKLY) {
			return String.format("%s주권 - %d원", duration, price);
		}
		if (passType == StudyCafePassType.FIXED) {
			return String.format("%s주권 - %d원", duration, price);
		}
		return "";
	}
}
