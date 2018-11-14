package strategy;

import java.util.*;
import reversi.*;

public class Group3 implements Strategy{

	private static double maxDepth;
	int[][] scoreBoard = {
			{500, -500, 250, 200, 200, 250, -500, 500},
			{-500, -500, 100, -100, -100, 100, -500, -500},
			{250, 100, 150, 100, 100, 150, 100, 250},
			{200, -100, 100, 0, 0, 100, -100, 200},
			{200, -100, 100, 0, 0, 100, -100, 200},
			{250, 100, 250, 100, 100, 250, 100, 250},
			{-500, -500, 100, -100, -100, 100, -500, -500},
			{500, -500, 250, 200, 200, 250, -500, 500}};

	
	@Override
	public Square chooseSquare(Board board) {
		Node choose = run(board.getCurrentPlayer(), board, 2);
		return choose.square;
	}

	
	public Node run (Player player, Board board, double maxDepth) {
		if (maxDepth < 1) {
			throw new IllegalArgumentException("Maximum depth must be greater than 0.");
		}
		Group3.maxDepth = maxDepth;
		return miniMax(player, board, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	
	private  Node miniMax (Player player, Board board, int currentDepth, double alpha, double beta) {
		if (currentDepth++ == maxDepth || board.isComplete()) {
			Node current = new Node(null, score(player, board));
			return current;
		}
		
		if (board.getCurrentPlayer() == Player.BLACK) {
			return getMax(player, board, currentDepth, alpha, beta);
		} else {
			return getMin(player, board, currentDepth, alpha, beta);
		}
	}

	
	private Node getMax (Player player, Board board, int currentDepth, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);
		
		for (Square theMove : board.getCurrentPossibleSquares()) {
			Board old = board.play(theMove);
			Node current = miniMax(player, old, currentDepth, alpha, beta);
			
			if (current.score > alpha) {
				alpha = current.score;
				bestMove = theMove;
			}
			
			if (alpha >= beta)
				break;
		}
		
		if(bestMove != new Square(-1, -1))
			return new Node(bestMove, alpha);
		
		return new Node(null, 1000);
	}

	
	private Node getMin (Player player, Board board, int currentDepth, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);
		
		for (Square theMove : board.getCurrentPossibleSquares()) {
			Board old = board.play(theMove);
			Node current = miniMax(player, old, currentDepth, alpha, beta);

			if (current.score < beta) {
				beta = current.score;
				bestMove = theMove;
			}

			if (alpha >= beta)
				break;
		}
		
		if (bestMove != new Square(-1, -1))
			return new Node(bestMove, beta);
		
		return new Node(null, -1000);
	}

	
	private int score (Player player, Board board) {
		if (board.isComplete() && board.getWinner() == Player.BLACK) {
			return Integer.MAX_VALUE;
		} else if (board.isComplete() && board.getWinner() == Player.WHITE) {
			return Integer.MIN_VALUE;
		} else {
			return blackScoreBoard(player, board);
		}
	}

	
	private int blackScoreBoard(Player player, Board board) {
		Map<Player, Integer> currentM = board.getPlayerSquareCounts();
		int blackCount = currentM.get(Player.BLACK);
		int whiteCount = currentM.get(Player.WHITE);
		int score = blackCount-whiteCount;

		int whiteScore = 0;
		int blackScore = 0;
		Map<Square, Player> PlayerSqr = board.getSquareOwners();

		Set<Square> AllSqr= PlayerSqr.keySet();
		for(Square sqr: AllSqr) {
			if(PlayerSqr.get(sqr) == Player.BLACK) {
				blackScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			}
			else {
				whiteScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			}
		}
		return blackScore - whiteScore - score;
	}


	private class Node{
		Square square;
		double score;

		private Node(Square square, double score) {
			this.square = square;
			this.score = score;
		}
	}

}