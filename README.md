# Reversi/Othello AI
For my final project in CSE 486 (Introduction to Artificial Intelligence), I had to create an AI implementation of 
the famous Reversi/Othello board game. The objective of the project was to create the fastest, most efficient Reversi/Othello
algorithm wherein it could compete against twenty-six other submissions for the top prize. Of those twenty-six submissions, my solution
came in fourth place.

As one of the most challenging projects I've ever had to complete (in or outside of an academic setting), I decided to share it to my GitHub.

## Brief Overview of the Solution
* Incorporates [minimax](https://en.wikipedia.org/wiki/Minimax) with [alpha-beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)
* Utilizes a board layout (2-D array) that contains scoring values based on how good a specific position is
  * I.e., corner pieces are weighed more than any other piece
* Level of play is configurable and differientiated by how deeply the algorithm checks every move
  * Easy: looks 1 move ahead
  * Medium: looks 3 moves ahead
  * Hard: looks 5 moves ahead
* Different beginning/end-game scoring metrics

## Brainstorming Process
1. Get familiar with [Reversi/Othello](https://www.eothello.com/)
2. Research and apply popular strategies to online gameplay
3. Translate experiences from online gameplay into viable solution by creating a weighted scoreboard (which focused on getting to the corners, staying away from "dead zones", forcing the opponent to make a move that minimized their score, and only making moves that maximized the player's score)
4. Consider and implement other strategies such as: checking several moves ahead, recognize specific board layouts (esp. those that lead to an immediate win), etc.

## Advantages
* Views the board differently based on how many pieces have been played
  * At the beginning of the game, optimizes having fewer positions
  * Towards the end of the game, focuses on taking more of the opponent's pieces
* Calculates the score of the board after every turn
  * Calculates a complete board, or one at a standstill, different from an incomplete one
* On average, this solution can resolve 20 games in 10-12 seconds while maintaining a depth of 4
  * Difficulty level: between Medium and Hard

## Disadvantages
* Doesn't win 100% of the time
  * For `n` games, where `n = 20`, this solution wins 18 times or more
  * Allows ties
* No randomization
  * Small amounts of randomization allows for "mutations" and prevents patterns/monotony 
* Set of possible moves aren't ordered or chosen by priority
  * Follows a FIFO structure
