public class Backtrack {
  /*
   * Backtracking-7 algorithm: Function isValid() that checks if a given matrix is a valid (filled)
   * sudoku grid. Recursive function that takes the grid and current position for the value we are
   * trying to fill in - Base cases: - If the position is at the end of the board grid, check if
   * the grid is valid. If so, return true. - When position is at the last column, move to next row
   * - if current position is blank/unassigned, fill it in with 1-9 and recur for all 9 cases with
   * the position of the next element. If the recursive call returns true, break the loop and return
   * true. - if the current index is assigned, then call the recursive function with position of
   * next element
   *
   * Algorithm used from
   * https://see.stanford.edu/materials/icspacs106b/H19-RecBacktrackExamples.pdf
   */

  /*
   * Checks if filling the @param num in at location @param row, @param col makes the board invalid
   * (unsolvable)
   */
  private static boolean isValid(Board board, int row, int col, int num) {
    // Check that number is not already present in the row or column
    for (int j = 0; j < board.getLength(); j++) {
      if (board.getValue(row, j) == num || board.getValue(j, col) == num) return false;
    }
    // Check that number is not already present in 3x3 grid
    int gLen = (int) Math.sqrt(board.getLength());
    int gRow = row - row % gLen;
    int gCol = col - col % gLen;
    for (int r = gRow; r < gRow + gLen; r++) {
      for (int c = gCol; c < gCol + gLen; c++) {
        if (board.getValue(r, c) == num) return false;
      }
    }
    return true; // no rules broke
  }

  /*
   * Uses backtracking-7 algorithm to solve a partially filled @param board and attempts to
   * assign values at all blank positions for a valid solution if possible
   */
  public static boolean solveBoard(Board board) {
    int row = -1;
    int col = -1;
    boolean isFilled = true;

    for (int r = 0; r < board.getLength(); r++) {
      for (int c = 0; c < board.getLength(); c++) {
        if (board.getValue(r, c) == 0) {
          row = r;
          col = c;
          isFilled = false; // There are still unfilled values in board
          break;
        }
      }
      if (!isFilled) break; // Unfilled values means we continue to backtracking
    }
    if (isFilled) return true; // fully filled board --> solution found

    // Backtrack --> assign a number from 1-9 --> if valid, recursively call, until
    // a full solution is found or no solutions remain
    for (int num = 1; num <= board.getLength(); num++) {
      if (isValid(
          board, row, col, num)) { // placing num at row,col is valid (will be further backtracked)
        board.setValue(row, col, num);
        if (solveBoard(board)) {
          return true;
        } else {
          board.setValue(row, col, 0);
        }
      }
    }
    return false;
  }

  // Runs backtracking algorithm on a given board.
  // The first argument is the path to the board file
  public static void main(String[] args) {
    String filename = args[0];
    BoardGenerator generator = new BoardGenerator();
    Board board = generator.readTxt(filename);
    System.out.println("Sudoku Board:");
    System.out.println(board);
    long start = System.nanoTime();
    Backtrack.solveBoard(board);
    long stop = System.nanoTime();
    System.out.println("Solved Sudoku Board (Backtrack):");
    System.out.println(board);
    System.out.println("Solver Runtime: " + (stop - start) + " ms");
  }
}
