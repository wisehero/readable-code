package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.io.StudyCafeIOHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

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
			StudyCafePass selectedPass = selectPass();

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

	private StudyCafePass selectPass() {
		StudyCafePassType passType = ioHandler.askPassTypeSelecting();
		List<StudyCafePass> passCandidates = findPassCandidatesBy(passType);

		return ioHandler.askPassSelecting(passCandidates);
	}

	private List<StudyCafePass> findPassCandidatesBy(StudyCafePassType studyCafePassType) {
		List<StudyCafePass> allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.stream()
			.filter(studyCafePass -> studyCafePass.isSamePassType(studyCafePassType))
			.toList();
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafePass selectedPass) {
		if (selectedPass.cannotUseLocker()) {
			return Optional.empty();
		}

		StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate != null) {
			boolean isLockerSelected = ioHandler.askLockerPass(lockerPassCandidate);

			if (isLockerSelected) {
				return Optional.of(lockerPassCandidate);
			}
		}
		return Optional.empty();
	}

	private StudyCafeLockerPass findLockerPassCandidateBy(StudyCafePass selectedPass) {
		List<StudyCafeLockerPass> allPasses = studyCafeFileHandler.readLockerPasses();

		return allPasses.stream()
			.filter(lockerPass -> lockerPass.isSameDurationType(lockerPass)
			)
			.findFirst()
			.orElse(null);
	}
}
