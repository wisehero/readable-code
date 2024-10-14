package cleancode.studycafe.tobe.model.pass;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import cleancode.studycafe.tobe.model.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.provider.SeatPassProvider;

class StudyCafeSeatPassesTest {

	private final SeatPassProvider seatPassProvider = new SeatPassFileReader();

	@ParameterizedTest
	@EnumSource(StudyCafePassType.class)
	@DisplayName("이용권 타입에 따라 이용권을 찾아내는지 확인한다.")
	void test1(StudyCafePassType passType) {
		StudyCafeSeatPasses seatPasses = seatPassProvider.getSeatPasses();
		List<StudyCafeSeatPass> passBy = seatPasses.findPassBy(passType);

		assertThat(passBy)
			.isNotEmpty()
			.allMatch(pass -> pass.getPassType() == passType);
	}

	@Test
	@DisplayName("이용권이 라커 이용권과 동일한 타입인지 확인한다.")
	void test2() {
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(
			StudyCafePassType.FIXED,
			4,
			250000);
		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(
			StudyCafePassType.FIXED,
			4,
			250000,
			0.1);

		studyCafeSeatPass.isSamePassType(studyCafeLockerPass.getPassType());
	}

	@Test
	@DisplayName("이용권이 라커 이용권과 동일한 타입 및 기간인지 확인한다.")
	void test3() {
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(
			StudyCafePassType.FIXED,
			4,
			250000);
		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(
			StudyCafePassType.FIXED,
			4,
			250000,
			0.1);

		boolean sameDurationType = studyCafeSeatPass.isSameDurationType(studyCafeLockerPass);

		assertThat(sameDurationType).isTrue();
	}

	@Test
	@DisplayName("이용권이 라커 이용권과 동일한 타입 및 기간인지 확인한다. - 실패 테스트, 타입이 다른 경우")
	void test4() {
		// given
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(
			StudyCafePassType.HOURLY,
			4,
			250000);
		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(
			StudyCafePassType.FIXED,
			4,
			250000,
			0.1);

		// when
		boolean sameDurationType = studyCafeSeatPass.isSameDurationType(studyCafeLockerPass);

		// then
		assertThat(sameDurationType).isFalse();
	}

	@Test
	@DisplayName("이용권이 라커 이용권과 동일한 타입 및 기간인지 확인한다. - 실패 테스트, 기간이 다른 경우")
	void test5() {
		// given
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(
			StudyCafePassType.FIXED,
			12,
			250000);
		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(
			StudyCafePassType.FIXED,
			4,
			250000,
			0.1);

		// when
		boolean sameDurationType = studyCafeSeatPass.isSameDurationType(studyCafeLockerPass);

		// then
		assertThat(sameDurationType).isFalse();
	}

	@Test
	@DisplayName("고정석이 아니면 사물함을 사용할 수 없다.")
	void test6() {
		// given
		StudyCafeSeatPass hourlyPass = StudyCafeSeatPass.of(
			StudyCafePassType.HOURLY,
			4,
			25000,
			0.1);
		StudyCafeSeatPass weeklyPass = StudyCafeSeatPass.of(
			StudyCafePassType.WEEKLY,
			4,
			25000,
			0.1);
		StudyCafeSeatPass fixedPass = StudyCafeSeatPass.of(
			StudyCafePassType.FIXED,
			4,
			25000,
			0.1);
		// when
		boolean hourlyCannotUseLocker = hourlyPass.cannotUseLocker();
		boolean weeklyCannotUseLocker = weeklyPass.cannotUseLocker();
		boolean fixedCannotUseLocker = fixedPass.cannotUseLocker();

		// then
		assertThat(hourlyCannotUseLocker).isTrue();
		assertThat(weeklyCannotUseLocker).isTrue();
		assertThat(fixedCannotUseLocker).isFalse();
	}
}