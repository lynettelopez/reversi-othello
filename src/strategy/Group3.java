package strategy;

import java.util.ArrayList;
import java.util.Map;

import reversi.Board;
import reversi.Move;
import reversi.Square;
import reversi.Strategy;
import reversi.Player;

/**
 * 
 * Uses the MiniMax algorithm to play a move in a game of Tic Tac Toe.
 * 
 */

public class Group3 implements Strategy {

	private static double maxPly;
	
	public void run(Board board, int maxPly) {
		run(board.getCurrentPlayer(), board, maxPly);
	}

	// Implement AlphaBeta pruning -- Update minimax, getMax & getMin methods
	
	/**
	 * 
	 * Execute the algorithm.
	 * @param player the player that the AI will identify as
	 * @param board  the Tic Tac Toe board to play on
	 * @param maxPly the maximum depth
	 * 
	 */
	public void run(Player player, Board board, double maxPly) {
		if (maxPly < 1) {
			throw new IllegalArgumentException("Maximum depth must be greater than 0.");
		}
		
		Group3.maxPly = maxPly;
		miniMax(player, board, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * 
	 * The meat of the algorithm.
	 * @param player     the player that the AI will identify as
	 * @param board      the Tic Tac Toe board to play on
	 * @param currentPly the current depth
	 * @return the score of the board
	 */
	private int miniMax(Player player, Board board, int currentPly, double alpha, double beta) {
		if (currentPly++ == maxPly || board.isComplete()) {
			return score(player, board);
		}

		if (board.getCurrentPlayer() == player) {
			return getMax(player, board, currentPly, alpha, beta);
		} 
		else {
			return getMin(player, board, currentPly, alpha, beta);
		}

	}

	private void retrieveBoard(Board board) {

		ArrayList<Move> moves = (ArrayList<Move>) board.getMoves();

		Board newBoard = new Board();

		for (int i = 0; i < moves.size() - 1; i++) {

			newBoard.play(moves.get(i).getSquare());

		}

		board = newBoard;

	}

	/**
	 * 
	 * Play the move with the highest score.
	 * 
	 * @param player     the player that the AI will identify as
	 * 
	 * @param board      the Tic Tac Toe board to play on
	 * 
	 * @param currentPly the current depth
	 * 
	 * @return the score of the board
	 * 
	 */

	private int getMax(Player player, Board board, int currentPly, double alpha, double beta) {

		double bestScore = Double.NEGATIVE_INFINITY;

		Square indexOfBestMove = new Square(-1, -1);

		for (Square theMove : board.getCurrentPossibleSquares()) {

			// Board modifiedBoard = board.getDeepCopy();//TODO change this to be able to
			// copy the current board

			board.play(theMove);

			int score = miniMax(player, board, currentPly, alpha, beta);

			if (score > alpha) {

				alpha = score;

				indexOfBestMove = theMove;

			}

			retrieveBoard(board);

			if (alpha >= beta)

				break;

			//

			// if (score >= bestScore) {

			// bestScore = score;

			// indexOfBestMove = theMove;

			// }

			//

			// if(bestScore >= beta) {

			// return (int)bestScore;

			// }

		}

		if (indexOfBestMove != new Square(-1, -1))

			board.play(indexOfBestMove);

		return (int) bestScore;

	}

	/**
	 * 
	 * Play the move with the lowest score.
	 * 
	 * @param player     the player that the AI will identify as
	 * 
	 * @param board      the Tic Tac Toe board to play on
	 * 
	 * @param currentPly the current depth
	 * 
	 * @return the score of the board
	 * 
	 */

	private int getMin(Player player, Board board, int currentPly, double alpha, double beta) {

		double bestScore = Double.POSITIVE_INFINITY;// beta

		Square indexOfBestMove = new Square(-1, -1);

		for (Square theMove : board.getCurrentPossibleSquares()) {

			board.play(theMove);

			int score = miniMax(player, board, currentPly, alpha, beta);

			if (score < beta) {

				beta = score;

				indexOfBestMove = theMove;

			}

			retrieveBoard(board);

			if (alpha >= beta)

				break;

		}

		if (indexOfBestMove != new Square(-1, -1))

			board.play(indexOfBestMove);

		return (int) bestScore;

	}

	/**
	 * 
	 * Get the score of the board.
	 * 
	 * @param player the play that the AI will identify as
	 * 
	 * @param board  the Tic Tac Toe board to play on
	 * 
	 * @return the score of the board
	 * 
	 */

	private int score(Player player, Board board) {

//		if (player == Player.) {
//			throw new IllegalArgumentException("Player must be X or O.");
//		}

		Player opponent = (player == Player.BLACK) ? Player.WHITE : Player.BLACK;

		if (board.isComplete() && board.getWinner() == player) {

			return Integer.MAX_VALUE;

		} else if (board.isComplete() && board.getWinner() == opponent) {

			return Integer.MIN_VALUE;

		} else {

			Map<Player, Integer> current = board.getPlayerSquareCounts();

			int blackCount = current.get("BLACK");

			int whiteCount = current.get("WHITE");

			return blackCount - whiteCount;

		}

	}

	@Override
	public Square chooseSquare(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

}