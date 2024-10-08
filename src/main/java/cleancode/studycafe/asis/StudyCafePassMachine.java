package cleancode.studycafe.asis;

import cleancode.studycafe.asis.exception.AppException;
import cleancode.studycafe.asis.io.InputHandler;
import cleancode.studycafe.asis.io.OutputHandler;
import cleancode.studycafe.asis.io.StudyCafeFileHandler;
import cleancode.studycafe.asis.model.StudyCafeLockerPass;
import cleancode.studycafe.asis.model.StudyCafePass;
import cleancode.studycafe.asis.model.StudyCafePassType;

import java.util.List;
import java.util.Optional;

public class StudyCafePassMachine {

	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run() {
		try {
			outputHandler.showWelcomeMessage();
			outputHandler.showAnnouncement();

			outputHandler.askPassTypeSelection();

			// 이용권을 어떤 것을 선택할 지에 대해 입력을 받기
			StudyCafePass selectedPass = selectPass();

			Optional<StudyCafeLockerPass> optionalStudyCafeLockerPass = selectLockerPass(selectedPass);
			optionalStudyCafeLockerPass.ifPresentOrElse(
				lockerPass -> outputHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> outputHandler.showPassOrderSummary(selectedPass)
			);

		} catch (AppException e) {
			outputHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass selectPass() {
		StudyCafePassType passType = inputHandler.getPassTypeSelectingUserAction();

		List<StudyCafePass> passCandidates = findPassCandidatesBy(passType);
		outputHandler.showPassListForSelection(passCandidates);
		return inputHandler.getSelectPass(passCandidates);
	}

	private List<StudyCafePass> findPassCandidatesBy(StudyCafePassType studyCafePassType) {
		List<StudyCafePass> allPasses = studyCafeFileHandler.readStudyCafePasses();

		return allPasses.stream()
			.filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
			.toList();
	}

	private Optional<StudyCafeLockerPass> selectLockerPass(StudyCafePass selectedPass) {
		if (selectedPass.getPassType() != StudyCafePassType.FIXED) {
			return Optional.empty();
		}

		StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate != null) {
			outputHandler.askLockerPass(lockerPassCandidate);
			boolean isLockerSelected = inputHandler.getLockerSelection();

			if (isLockerSelected) {
				return Optional.of(lockerPassCandidate);
			}
		}
		return Optional.empty();
	}

	private StudyCafeLockerPass findLockerPassCandidateBy(StudyCafePass selectedPass) {
		List<StudyCafeLockerPass> allPasses = studyCafeFileHandler.readLockerPasses();

		return allPasses.stream()
			.filter(lockerPass ->
				lockerPass.getPassType() == selectedPass.getPassType()
					&& lockerPass.getDuration() == selectedPass.getDuration()
			)
			.findFirst()
			.orElse(null);
	}
}
