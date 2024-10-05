package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;

public class Minesweeper implements GameInitializable, GameRunnable {

	private final GameBoard gameBoard;
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private GameStatus gameStatus;

	public Minesweeper(GameConfig gameConfig) {
		this.inputHandler = gameConfig.getInputHandler();
		this.outputHandler = gameConfig.getOutputHandler();
		gameBoard = new GameBoard(gameConfig.getGameLevel());
		gameStatus = GameStatus.IN_PROGRESS;
	}

	@Override
	public void initialize() {
		gameBoard.initializeGame();
		gameStatus = GameStatus.IN_PROGRESS;
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (gameBoard.isInProgress()) {
			try {
				outputHandler.showBoard(gameBoard);

				CellPosition cellPosition = getCellInputFromUser();
				UserAction userActionInput = getUserActionInputFromUser();
				actOnCell(cellPosition, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
			}
		}

		outputHandler.showBoard(gameBoard);

		if (gameBoard.isWinStatus()) {
			outputHandler.showGameWinningComment();
		}
		if (gameBoard.isLoseStatus()) {
			outputHandler.showGameLosingComment();
		}
	}

	private void actOnCell(CellPosition cellPosition, UserAction userAction) {

		if (doesUserChooseToPlantFlag(userAction)) {
			gameBoard.flagAt(cellPosition);
			return;
		}

		if (doesUserChooseToOpenCell(userAction)) {
			gameBoard.openAt(cellPosition);
			return;
		}
		throw new GameException("잘못된 번호를 선택하셨습니다.");
	}

	private void changeGameStatusToLose() {
		gameStatus = GameStatus.LOSE;
	}

	private boolean doesUserChooseToOpenCell(UserAction userAction) {
		return userAction == UserAction.OPEN;
	}

	private boolean doesUserChooseToPlantFlag(UserAction userAction) {
		return userAction == UserAction.FLAG;
	}

	private UserAction getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserActionFromUser();
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();
		if (gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new GameException("잘못된 입력입니다.");
		}
		return cellPosition;
	}
}
