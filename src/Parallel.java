import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Parallel {
  /*
   * Solves the sudoku board using parallel techniques
   */
  public static void solveBoard(Board board) {
    // set possible values for each cell in the grid
    board.setPossibleValuesForGrid();
    // true if a value has been set in the grid, valse otherwise
    AtomicBoolean valueSet = new AtomicBoolean(false);
    int nThreads = Runtime.getRuntime().availableProcessors();
    do {
      // call elimantion; if the method returns and a value
      // has been set in the grid, redo elimination
      elimination(board, valueSet, nThreads);
      System.out.println("After elimination:");
      System.out.println(board);
      if (valueSet.get()) {
        continue;
      }
      // call loneRangers; if the method returns and a value
      // has been set in the grid, restart from elimination
      loneRangers(board, valueSet, nThreads);
      System.out.println("After loneRangers:");
      System.out.println(board);
      if (valueSet.get()) {
        continue;
      }
    } while (valueSet.get()); // loop while a value has been set in the grid

    // use backtracking if parallel methods didn't solve the board
    Backtrack.solveBoard(board);
  }

  /*
   * Execute elimination tasks in parallel
   */
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

  /*
   * Execute loneRangers tasks in parallel
   */
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
   * based on which mini-grid we are at
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
}
