import java.util.HashMap;
import java.util.Map;

public class Nim {
	public static final int HUMAN = 0;
	public static final int COMPUTER = 1;
	public static final int UNCLEAR = 2;

	final static int NUM_ROWS = 3;
	private final static int SZ_ROW0 = 5;
	private final static int SZ_ROW1 = 3;
	private final static int SZ_ROW2 = 1;
	private static final int HUMAN_WIN = 0;
	private static final int COMPUTER_WIN = 3;

	// These fields represent the actual position at any time.
	private int[] heap = new int[NUM_ROWS];
	private int nextPlayer;
	private Map<Position4, Integer> store = new HashMap<Position4, Integer>();

	private final class Position4 {
		private int[] heap;

		public Position4(int[] theHeap) {
			heap = new int[3];
			for (int i = 0; i < 3; i++)
				heap[i] = theHeap[i];
		}

		public boolean equals(Object rhs) {
			if (!(rhs instanceof Position4))
				return false;
			
			for (int i = 0; i < 3; i++)
					if (heap[i] != ((Position4) rhs).heap[i])
						return false;
			return true;
		}

		public int hashCode() {
			int hashVal = 0;

			for (int i = 0; i < 3; i++)
					hashVal = hashVal * 4 + heap[i];

			return hashVal;
		}
	}
	/**
	 * Construct an instance of the Nim3 Game
	 */
	public Nim() {
	}

	/**
	 * Set up the position ready to play.
	 */
	public void init() {
		heap[0] = SZ_ROW0;
		heap[1] = SZ_ROW1;
		heap[2] = SZ_ROW2;
		nextPlayer = COMPUTER;
	}

	/**
	 * Who has won the game? Returns one of True or False s
	 * @param side of player making the move
	 * @returns true if the player is the winner and false otherwise.
	 */
	public boolean winner(int side) {
		if (nextPlayer == side && getStarsLeft() == 0) {
			return true;
		}
		return false;
	}
	public Best chooseMove(int side, int depth) {
		int opp; // The other side
		Best reply; // Opponent's best reply
		int bestRow = -1; // Initialize running value with out-of-range value
		int starRemoved;
		int bestStar = 0;
		int value;
		Position4 thisPosition = new Position4(heap);
		int nimSum = heap[0] ^ heap[1] ^ heap[2];
	
		// Initialize running values with out-of-range values (good software practice)
		// Here also to ensure that *some* move is chosen, even if a hopeless case
		if (side == COMPUTER) {
			opp = HUMAN;
			value = HUMAN_WIN - 1; // impossibly low value
		} else {
			opp = COMPUTER;
			value = COMPUTER_WIN + 1; // impossibly high value
		}

		for (int row = 0; row < 3; row++) {
			if ((heap[row]^nimSum) < heap[row] ) {
				starRemoved = heap[row] - (heap[row]^nimSum);
				makeMove(side, row, starRemoved);
				reply = chooseMove(opp, depth+1);
				undoMove(opp, row, starRemoved);	
				// Update if side gets better position
				if (side == COMPUTER && reply.val > value || side == HUMAN && reply.val < value) {
					value = reply.val;
					bestRow = row;		
					bestStar = starRemoved;
				}
			}
		store.put(thisPosition, value);
		}
		return new Best(value, bestRow, bestStar);
	}
	/**
	 * Make a move
	 * 
	 * @param side of player making move
	 * @param row 
	 * @param number of stars taken.
	 * @returns false if move is illegal.
	 */
	public boolean makeMove(int side, int row, int number) {
		if (side != nextPlayer) { 
			return false;  // wrong player played
		}
		if (!isLegal(row, number)) {
			return false;
		} else {
			nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);
			heap[row] = heap[row] - number;
		}
		return true;
	}
	
	public boolean undoMove(int side, int row, int number) {
		if (!isLegal(row, number)) {
			return false;
		}
		else {
			nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);
			heap[row] = heap[row] + number;
		}
		return true;
	}

	/**
	 * This method displays current position
	 * @return 
	 */
	public void position()  {
		StringBuilder board = new StringBuilder("");

		for (int i = 0; i < NUM_ROWS; i++) {
			char c = (char) ((int) 'A' + i);
			board.append(c + ": ");
			for (int j = heap[i]; j > 0; j--) {
				board.append("* ");
			}
			board.append('\n');
		}

		System.out.println(board.toString());
	}

	/**
	 * Compute the total number of stars left.
	 */
	private int getStarsLeft() {
		return (heap[0] + heap[1] + heap[2]);
	}

	private boolean isLegal(int row, int stars) {
		return row >= -1 && row <= 2 && stars >= 1 && stars <= heap[row];
	}

	/**
	 * What are the rules of the game? How are moves entered interactively?
	 * 
	 * @return a String with this information.
	 */
	public String help() {
		StringBuffer s = new StringBuffer("\nNim is the name of the game.\n");
		s.append("The board contains three ");
		s.append("rows of stars.\nA move removes stars (at least one) ");
		s.append("from a single row.\nThe player who takes the last star loses.\n");
		s.append("Type Xn (or xn) at the terminal to remove n stars from row X.\n");
		s.append("? displays the current position, q quits.\n");
		return s.toString();
	}
}