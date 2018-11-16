package strategy;
import java.util.Map;
import java.util.Set;

import reversi.Board;
import reversi.Square;
import reversi.Player;
import reversi.Strategy;
/**
 * Uses the MiniMax algorithm to play a move in a game of Tic Tac Toe.
 */
public class Basis implements Strategy{

	private int[][] scoreBoard = {
			{200, -2, 20, 4, 4, 10, -2, 200},
			{-2, -40, 2, 2, 2, 2, -40, -2},
			{20, 2, 10, 4, 4, 10, 2, 10},
			{4, 2, 4, 0, 0, 4, 2, 4},
			{4, 2, 4, 0, 0, 4, 2, 4},
			{20, 2, 10, 4, 4, 10, 2, 20},
			{-2, -40, 2, 2, 2, 2, -40, -2},
			{200, -2, 20, 4, 4, 20, -2, 200}};

	private static double maxPly;

	@Override
	public Square chooseSquare(Board board) {
		//look four moves ahead
		Node choose = run(board, 4);
		return choose.square;
	}

	/**
	 * Execute the algorithm.
	 * @param player        the player that the AI will identify as
	 * @param board         the Tic Tac Toe board to play on
	 * @param maxPly        the maximum depth
	 */
	public Node run (Board board, double maxPly) {
		if (maxPly < 1) {
			throw new IllegalArgumentException("Maximum depth must be greater than 0.");
		}

		Basis.maxPly = maxPly;
		return miniMax(board, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * The meat of the algorithm.
	 * @param player        the player that the AI will identify as
	 * @param board         the Reversi board to play on
	 * @param currentPly    the current depth
	 * @return              the score of the board
	 */
	private  Node miniMax (Board board, int currentPly, double alpha, double beta) {
		if (currentPly++ == maxPly || board.isComplete()) {
			Node current = new Node(null, score(board));
			return current;
		}
		if (board.getCurrentPlayer() == Player.BLACK) {
			return getMax(board, currentPly, alpha, beta);
		} else {
			return getMin(board, currentPly, alpha, beta);
		}

	}

	/**
	 * Play the move with the highest score.
	 * @param player        the player that the AI will identify as
	 * @param board         the Tic Tac Toe board to play on
	 * @param currentPly    the current depth
	 * @return              the score of the board
	 */
	private Node getMax (Board board, int currentPly, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);
		Set<Square> currentPossibleSquares = board.getCurrentPossibleSquares();
		//if there is no move, return 1000 as the score to make sure that it will not be chosen
		if(currentPossibleSquares.size() == 0)
			return new Node(bestMove, 1000);

		for (Square theMove : currentPossibleSquares) {

			Board newBoard = board.play(theMove);
			Node current = miniMax(newBoard, currentPly, alpha, beta);

			if(current.score > alpha) {
				alpha = current.score;
				bestMove = theMove;
			}

			//if there is a corner, we will take the corner instead of other moves
			int row = theMove.getRow();
			int column = theMove.getColumn();
			if((row == 0 && column == 0 ) || (row == 0 && column == 7) || (row == 7 && column == 0) || (row == 7 && column == 7)) {
				break;
			}
			//alpha-beta pruning 
			if(alpha >= beta)
				break;
		}

		return new Node(bestMove, alpha);
	}

	/**
	 * Play the move with the lowest score.
	 * @param player        the player that the AI will identify as
	 * @param board         the Tic Tac Toe board to play on
	 * @param currentPly    the current depth
	 * @return              the score of the board
	 */
	private Node getMin (Board board, int currentPly, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);
		Set<Square> currentPossibleSquares = board.getCurrentPossibleSquares();
		//if there is no move, return -1000 as the score to make sure that it will not be chosen
		if(currentPossibleSquares.size() == 0)
			return new Node(bestMove, -1000);

		for (Square theMove : currentPossibleSquares) {

			Board newBoard = board.play(theMove);
			Node current = miniMax(newBoard, currentPly, alpha, beta);

			if(current.score < beta) {
				beta = current.score;
				bestMove = theMove;
			}

			//if there is a corner, we will take the corner instead of other moves
			int row = theMove.getRow();
			int column = theMove.getColumn();
			if((row == 0 && column == 0 ) || (row == 0 && column == 7) || (row == 7 && column == 0) || (row == 7 && column == 7)) {
				break;
			}
			//alpha-beta pruning 
			if(alpha >= beta)
				break;
		}
		return new Node(bestMove, beta);
	}

	/**
	 * Get the score of the board.
	 * @param player        the play that the AI will identify as
	 * @param board         the Tic Tac Toe board to play on
	 * @return              the score of the board
	 */
	private int score (Board board) {
		//if board complete, and the black wins, we return a integer.max_value to indicate that
		if (board.isComplete() && board.getWinner() == Player.BLACK) {
			return Integer.MAX_VALUE;
		} 
		//if board complete, and the white wins, we return a integer.min_value to indicate that
		else if (board.isComplete() && board.getWinner() == Player.WHITE) {
			return Integer.MIN_VALUE;
		} 
		//if board not complete, we will calculate the score for the current board
		else {
			return currentScore(board);
		}
	}

	/**
	 * This is a helper score method to calculate score while it is not the end
	 * @param board
	 * @return the current score
	 */
	private int currentScore(Board board) {
		//the difference between the number of black chesses and the number of white chesses
		Map<Player, Integer> currentM = board.getPlayerSquareCounts();
		int blackCount = currentM.get(Player.BLACK);
		int whiteCount = currentM.get(Player.WHITE);
		int score = 2*(blackCount-whiteCount);

		int whiteScore = 0;
		int blackScore = 0;
		Map<Square, Player> PlayerSqr = board.getSquareOwners();

		Set<Square> AllSqr= PlayerSqr.keySet();
		for(Square sqr: AllSqr) {
			if(PlayerSqr.get(sqr) == Player.BLACK) {
				//how many positional score black chess has gain
				blackScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			}
			else {
				//how many positional score white chess has gain
				whiteScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			}
		}


		//opening, flip less cheese but gain more good position
		if(blackCount+whiteCount <= 50) {
			return blackScore - whiteScore - score;
		}
//		//midGame, minimize opponent choices
//		else if(blackCount+whiteCount > 15 && blackCount+whiteCount <= 50) {
//			int opponentChoice = 2*board.getCurrentPossibleSquares().size();
//			return blackScore - whiteScore - score - opponentChoice;
//		}
		//endGame, flip more chesses at the end
		//if(blackCount+whiteCount > 54)
		else {
			return blackScore - whiteScore + score;
		}
	}

	/**
	 * A private class contains a square, and the score that will get if make that move
	 *
	 */
	private class Node{
		Square square;
		double score;

		/**
		 * Node Class Constructor
		 * @param square
		 * @param score the score that will get if make that move
		 */
		private Node(Square square, double score) {
			this.square = square;
			this.score = score;
		}

	}

}