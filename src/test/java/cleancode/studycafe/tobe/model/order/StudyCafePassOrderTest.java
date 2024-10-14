package cleancode.studycafe.tobe.model.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cleancode.studycafe.tobe.model.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;

class StudyCafePassOrderTest {

	@Test
	@DisplayName("2주권 이상 결제 시 10% 할인을 받는다.")
	void test1() {

		// given
		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 4, 250000, 0.1);
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(StudyCafePassType.FIXED, 4, 10000);
		StudyCafePassOrder studyCafePassOrder = StudyCafePassOrder.of(studyCafeSeatPass, studyCafeLockerPass);

		// when
		int totalPrice = studyCafePassOrder.getTotalPrice();

		// then
		assertThat(totalPrice).isEqualTo(235000);
	}

	@Test
	@DisplayName("12주권 결제 시 15% 할인을 받는다.")
	void test2() {

		StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 12, 700000, 0.15);
		StudyCafeLockerPass studyCafeLockerPass = StudyCafeLockerPass.of(StudyCafePassType.FIXED, 12, 30000);
		StudyCafePassOrder studyCafePassOrder = StudyCafePassOrder.of(studyCafeSeatPass, studyCafeLockerPass);

		int totalPrice = studyCafePassOrder.getTotalPrice();

		assertThat(totalPrice).isEqualTo(
			studyCafePassOrder.getSeatPass().getPrice() + studyCafePassOrder.getLockerPass().get().getPrice()
				- studyCafePassOrder.getDiscountPrice());
	}
}