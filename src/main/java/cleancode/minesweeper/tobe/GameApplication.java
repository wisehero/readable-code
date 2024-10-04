package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.gamelevel.Middle;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameApplication {

	public static void main(String[] args) {
		GameLevel gameLevel = new Middle();

		GameConfig gameConfig = new GameConfig(gameLevel, new ConsoleInputHandler(), new ConsoleOutputHandler());

		Minesweeper minesweeper = new Minesweeper(gameConfig);
		minesweeper.initialize();
		minesweeper.run();
	}
}
