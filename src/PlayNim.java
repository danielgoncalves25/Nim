import java.util.Scanner;

public class PlayNim {
	public PlayNim() {
		g = new Nim();
		g.init();
	}

	public void doHumanMove() {
		boolean legal;
		int rowInt = 0;
		g.position();
		do {
			System.out.println("Your Turn ");
			System.out.println("row :");
			String rowString = scan.next();
			
			if (rowString.equals("A"))
				rowInt = 0;
			else if (rowString.equals("B"))
				rowInt = 1;
			else
				rowInt = 2;
			
			System.out.println("Number of Stars : ");
			int col = scan.nextInt();
			legal = g.makeMove(Nim.HUMAN, rowInt, col);
		} while (!legal);
	}
	
	public void doComputerMove() {
		Best compMove = g.chooseMove(Nim.COMPUTER, 0);
		System.out.println("Computer plays ROW = " + compMove.row + " Stars removed =  " + compMove.column);
		g.makeMove(Nim.COMPUTER, compMove.row, compMove.column);
	}

	boolean checkAndReportStatus() {
		System.out.println();
		if (g.winner(Nim.COMPUTER)) {
			System.out.println("Computer Won :(");
			System.out.println("Try Again");
			return false; // game is done
		}
		if (g.winner(Nim.HUMAN)) {
			System.out.println("Congratulations You WON!!");
			return false; // game is done
		}
		
		return true;
	}

	// do one round of playing the game, return true at end of game
	public boolean getAndMakeMoves() {
		// let computer go first...
		doComputerMove();
		// System.out.println("count = " + t.getCount());
		if (!checkAndReportStatus())
			return false; // game over
		doHumanMove();
		if (!checkAndReportStatus())
			return false; // game over
		return true;
	}
	
	void playOneGame() {
		boolean continueGame = true;
		while (continueGame) {
			continueGame = getAndMakeMoves();
		}
	}

	public static void main(String[] args) {
		PlayNim ui = new PlayNim();
		System.out.println("Type A, B, or C for row");
		System.out.println("Start of game:");
		ui.playOneGame();

	}

	private Nim g; // g for game
	private Scanner scan = new Scanner(System.in);
}
