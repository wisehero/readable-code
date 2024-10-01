package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.gamelevel.Middle;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameApplication {

	public static void main(String[] args) {
		GameLevel gameLevel = new Middle();

		Minesweeper minesweeper = new Minesweeper(gameLevel, new ConsoleInputHandler(), new ConsoleOutputHandler());
		minesweeper.initialize();
		minesweeper.run();
	}
}
