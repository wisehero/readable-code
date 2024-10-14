package cleancode.studycafe.tobe.model.pass;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudyCafePassTypeTest {

	@Test
	@DisplayName("고정석은 사물함을 사용할 수 있는 이용권 타입이다.")
	void test1() {
		// given
		StudyCafePassType fixed = StudyCafePassType.FIXED;

		// when
		boolean result = fixed.isLockerType();

		// then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("고정석이 아닌 이용권은 사물함을 사용할 수 없다.")
	void test2() {
		// given
		StudyCafePassType hourly = StudyCafePassType.HOURLY;
		StudyCafePassType weekly = StudyCafePassType.WEEKLY;

		// when
		boolean result1 = hourly.isNotLockerType();
		boolean result2 = weekly.isNotLockerType();

		// then
		assertThat(result1).isTrue();
		assertThat(result2).isTrue();
	}

	@Test
	@DisplayName("사물함 사용 가능 이용권과 그렇지 않은 이용권은 isLockerType()과 isNotLockerType()의 결과가 서로 반대이다.")
	void test3() {
		for (StudyCafePassType passType : StudyCafePassType.values()) {
			assertThat(passType.isLockerType()).isNotEqualTo(passType.isNotLockerType());
		}

	}
}