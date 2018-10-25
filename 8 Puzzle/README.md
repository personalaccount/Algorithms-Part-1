# Programming Assignment 4: 8 Puzzle

> Please navigate to the official [assignment 4 URL](http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html) for details.

Write a program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm.

**The problem.** The 8-puzzle problem is a puzzle invented and popularized by Noyes Palmer Chapman in the 1870s. It is played on a 3-by-3 grid with 8 square blocks labeled 1 through 8 and a blank square. Your goal is to rearrange the blocks so that they are in order, using as few moves as possible. You are permitted to slide blocks horizontally or vertically into the blank square.

|initial |     |1 left  |     | 2 up   |     | 5 left |     | goal  |
|------- |---- |------- |---- |------- |---- |------- |---- |-------|
|*  1  3 |     |1  *  3 |     |1  2  3 |     |1  2  3 |     |1  2  3|
|4  2  5 |  => |4  2  5 |  => |4  *  5 |  => |4  5  * |  => |4  5  6|
|7  8  6 |     |7  8  6 |     |7  8  6 |     |7  8  6 |     |7  8  *|

 **Best-first search.** Now, we describe a solution to the problem that illustrates a general artificial intelligence methodology known as the A* search algorithm. We define a search node of the game to be a board, the number of moves made to reach the board, and the predecessor search node. First, insert the initial search node (the initial board, 0 moves, and a null predecessor search node) into a priority queue. Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all neighboring search nodes (those that can be reached in one move from the dequeued search node). Repeat this procedure until the search node dequeued corresponds to a goal board. The success of this approach hinges on the choice of priority function for a search node. We consider two priority functions:

 * *Hamming priority function.* The number of blocks in the wrong position, plus the number of moves made so far to get to the search node. Intuitively, a search node with a small number of blocks in the wrong position is close to the goal, and we prefer a search node that have been reached using a small number of moves.
 * *Manhattan priority function.* The sum of the Manhattan distances (sum of the vertical and horizontal distance) from the blocks to their goal positions, plus the number of moves made so far to get to the search node.
