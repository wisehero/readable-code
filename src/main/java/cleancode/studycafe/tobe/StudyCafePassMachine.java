package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.locker.StudyCafeLockerPasses;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			ioHandler.askPassTypeSelection();

			// 이용권을 어떤 것을 선택할 지에 대해 입력을 받기
			StudyCafeSeatPass selectedPass = selectPass();

			Optional<StudyCafeLockerPass> optionalStudyCafeLockerPass = selectLockerPass(selectedPass);
			optionalStudyCafeLockerPass.ifPresentOrElse(
				lockerPass -> ioHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> ioHandler.showPassOrderSummary(selectedPass)
			);

		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafeSeatPass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		List<StudyCafeSeatPass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafeSeatPass> findPassCandidatesBy(StudyCafePassType studyCafePassType) {
		StudyCafeSeatPasses allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.findPassBy(studyCafePassType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafeSeatPass selectedPass) {
		if (selectedPass.cannotUseLocker()) {
			return Optional.empty();
		}

		Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidateBy(selectedPass);
		if (lockerPassCandidate.isPresent()) {
			StudyCafeLockerPass lockerPass = lockerPassCandidate.get();

			boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);
			if (isLockerSelected) {
				return Optional.of(lockerPass);
			}

			return Optional.empty();
		}
		return Optional.empty();
	}

	private Optional<StudyCafeLockerPass> findLockerPassCandidateBy(StudyCafeSeatPass selectedPass) {
		StudyCafeLockerPasses allPasses = studyCafeFileHandler.readLockerPasses();

		return allPasses.findLockerPassBy(selectedPass);
	}
}
