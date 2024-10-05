package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameRunnable;

public class AnotherGame implements GameRunnable {

	// initialize가 필요 없는 게임
	public void initialize() {
		// 피료없습
	}

	@Override
	public void run() {
		System.out.println("AnotherGame run");
	}
}
