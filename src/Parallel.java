import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

// Implementation of parallel algorithm to solve sudoku board
public class Parallel {

  // Solves the sudoku board using parallel techniques
  public static void solveBoard(Board board, int nThreads) {
    // set possible values for each cell in the grid
    board.setPossibleValuesForGrid();
    // true if a value has been set in the grid, valse otherwise
    AtomicBoolean valueSet = new AtomicBoolean(false);
    do {
      // call elimantion; if the method returns and a value
      // has been set in the grid, redo elimination
      elimination(board, valueSet, nThreads);
      if (valueSet.get()) {
        continue;
      }
      // call loneRangers; if the method returns and a value
      // has been set in the grid, restart from elimination
      loneRangers(board, valueSet, nThreads);
      if (valueSet.get()) {
        continue;
      }
    } while (valueSet.get()); // loop while a value has been set in the grid

    // use backtracking if parallel methods didn't solve the board
    Backtrack.solveBoard(board);
  }

  // Execute elimination tasks in parallel
  private static void elimination(Board board, AtomicBoolean valueSet, int nThreads) {
    ExecutorService pool = Executors.newFixedThreadPool(nThreads);
    valueSet.set(false);
    // execute elimination task on each mini-grid
    for (int cornerRow = 0; cornerRow < 9; cornerRow += 3) {
      for (int cornerCol = 0; cornerCol < 9; cornerCol += 3) {
        EliminationTask elemTask = new EliminationTask(board, cornerRow, cornerCol, valueSet);
        pool.execute(elemTask);
      }
    }
    pool.shutdown();
    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      // do nothing
    }
  }

  // Execute loneRangers tasks in parallel
  private static void loneRangers(Board board, AtomicBoolean valueSet, int nThreads) {
    ExecutorService pool = Executors.newFixedThreadPool(nThreads);
    valueSet.set(false);
    // execute elimination task on each mini-grid
    for (int cornerRow = 0; cornerRow < 9; cornerRow += 3) {
      for (int cornerCol = 0; cornerCol < 9; cornerCol += 3) {
        // determine whether toe check rows or columns
        boolean[] checks = getMiniGridChecks(cornerRow, cornerCol);
        boolean checkRows = checks[0];
        boolean checkCols = checks[1];
        LoneRangersTask LRTask =
            new LoneRangersTask(board, cornerRow, cornerCol, checkRows, checkCols, valueSet);
        pool.execute(LRTask);
      }
    }
    pool.shutdown();
    try {
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      // do nothing
    }
  }

  /*
   * Determine whether to check rows or columns
   * based on which mini-grid we are at.
   * Source: https://cse.buffalo.edu/faculty/miller/Courses/CSE633/Sankar-Spring-2014-CSE633.pdf Slide 11
   */
  private static boolean[] getMiniGridChecks(int cornerRow, int cornerCol) {
    boolean checkRows = false;
    boolean checkCols = false;
    if ((cornerRow == 0 && cornerCol == 0)
        || (cornerRow == 3 && cornerCol == 3)
        || (cornerRow == 6 && cornerCol == 3)) {
      checkRows = true;
    } else if ((cornerRow == 3 && cornerCol == 0)
        || (cornerRow == 0 && cornerCol == 3)
        || (cornerRow == 0 && cornerCol == 6)) {
      checkCols = true;
    }
    boolean[] checks = {checkRows, checkCols};
    return checks;
  }

  // Runs parallel algorithm on a given board.
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
    System.out.println("Solved Sudoku Board (Parallel):");
    System.out.println(board);
    System.out.println("Solver Runtime: " + (stop - start) + " ms");
  }
}
