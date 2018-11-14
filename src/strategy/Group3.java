package strategy;

import java.util.*;
import reversi.*;

public class Group3 implements Strategy {

	private static double maxDepth;
	private int[][] scoreBoard = {
			{100, -20, 10, 2, 2, 10, -20, 100},
			{-20, -20, 1, 1, 1, 1, -20, -20},
			{10, 1, 10, 2, 2, 10, 1, 10},
			{2, 1, 2, 0, 0, 2, 1, 2},
			{2, 1, 2, 0, 0, 2, 1, 2},
			{10, 1, 10, 2, 2, 10, 1, 10},
			{-20, -20, 1, 1, 1, 1, -20, -20},
			{100, -20, 10, 2, 2, 10, -20, 100}};

	
	@Override
	public Square chooseSquare(Board board) {
		Node choose = run(board, 2);
		return choose.square;
	}

	
	public Node run(Board board, double maxDepth) {
		if (maxDepth < 1) {
			throw new IllegalArgumentException("Maximum depth must be greater than 0.");
		}
		Group3.maxDepth = maxDepth;
		return miniMax(board, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	
	private Node miniMax(Board board, int currentDepth, double alpha, double beta) {
		if (currentDepth++ == maxDepth || board.isComplete()) {
			Node current = new Node(null, score(board));
			return current;
		}

		if (board.getCurrentPlayer() == Player.BLACK) {
			return getMax(board, currentDepth, alpha, beta);
		} else {
			return getMin(board, currentDepth, alpha, beta);
		}
	}

	
	private Node getMax(Board board, int currentDepth, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);

		PriorityQueue<Node> currentPossSquaresSorted = new PriorityQueue<Node>(new NodeComparator());
		currentPossSquaresSorted = prioritizeMoves(board.getCurrentPossibleSquares());

		if (currentPossSquaresSorted.size() == 0) {
			return new Node(bestMove, 1000);
		}

		for (Node n : currentPossSquaresSorted) {
			Square theMove = n.square;
			Board old = board.play(theMove);
			Node current = miniMax(old, currentDepth, alpha, beta);

			if (current.score > alpha) {
				alpha = current.score;
				bestMove = theMove;
			}

			// if there is a corner, we will take the corner instead of other moves
			int row = theMove.getRow();
			int column = theMove.getColumn();
			if ((row == 0 && column == 0) || (row == 0 && column == 7) || (row == 7 && column == 0)
					|| (row == 7 && column == 7)) {
				break;
			}

			// if there's no need to check the rest, take what we have
			if (alpha >= beta)
				break;
		}

		return new Node(bestMove, alpha);
	}

	
	private Node getMin(Board board, int currentDepth, double alpha, double beta) {
		Square bestMove = new Square(-1, -1);
		
		PriorityQueue<Node> currentPossSquaresSorted = new PriorityQueue<Node>(new NodeComparator());
		currentPossSquaresSorted = prioritizeMoves(board.getCurrentPossibleSquares());

		if (currentPossSquaresSorted.size() == 0) {
			return new Node(bestMove, -1000);
		}

		for (Node n : currentPossSquaresSorted) {
			Square theMove = n.square;
			Board old = board.play(theMove);
			Node current = miniMax(old, currentDepth, alpha, beta);

			if (current.score < beta) {
				beta = current.score;
				bestMove = theMove;
			}

			// if there is a corner, we will take the corner instead of other moves
			int row = theMove.getRow();
			int column = theMove.getColumn();
			if ((row == 0 && column == 0) || (row == 0 && column == 7) || (row == 7 && column == 0)
					|| (row == 7 && column == 7)) {
				break;
			}

			// if there's no need to check the rest, take what we have
			if (alpha >= beta)
				break;
		}

		return new Node(bestMove, beta);
	}

	
	private PriorityQueue<Node> prioritizeMoves(Set<Square> s) {
		PriorityQueue<Node> result = new PriorityQueue<Node>(new NodeComparator());

		for (Square x : s) {
			int row = x.getRow();
			int col = x.getColumn();
			double scoreForX = scoreBoard[row][col];
			Node y = new Node(x, scoreForX);
			result.add(y);
		}

		return result;
	}

	
	private int score(Board board) {
		if (board.isComplete() && board.getWinner() == Player.BLACK) {
			return Integer.MAX_VALUE;
		} else if (board.isComplete() && board.getWinner() == Player.WHITE) {
			return Integer.MIN_VALUE;
		} else {
			return blackScoreBoard(board);
		}
	}

	
	private int blackScoreBoard(Board board) {
		Map<Player, Integer> currentM = board.getPlayerSquareCounts();
		int blackCount = currentM.get(Player.BLACK);
		int whiteCount = currentM.get(Player.WHITE);
		int score = blackCount - whiteCount;

		int whiteScore = 0;
		int blackScore = 0;
		Map<Square, Player> PlayerSqr = board.getSquareOwners();

		Set<Square> AllSqr = PlayerSqr.keySet();
		for (Square sqr : AllSqr) {
			if (PlayerSqr.get(sqr) == Player.BLACK) {
				blackScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			} else {
				whiteScore += scoreBoard[sqr.getRow()][sqr.getColumn()];
			}
		}

		if (blackCount + whiteCount > 50) {
			return blackScore - whiteScore + score;
		}

		return blackScore - whiteScore - score;
	}

}


class Node {

	Square square;
	double score;

	public Node(Square square, double score) {
		this.square = square;
		this.score = score;
	}
}


class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(strategy.Node o1, strategy.Node o2) {
		if (o1.score < o2.score) {
			return 1;
		} else if (o1.score > o2.score) {
			return -1;
		}
		return 0;
	}

}