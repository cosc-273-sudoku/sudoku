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
      elimination(board, valueSet, nThreads);
      // call loneRangers; if the method returns and a value
      // has been set in the grid, restart from elimination
      // loneRangers();
      // if (valueSet.get()) {
      //   continue;
      // }
    } while (valueSet.get()); // loop while a value has been set in the grid

    // System.out.println("Before backtrack:");
    // System.out.println(board);

    // use backtracking if parallel methods didn't solve the board
    Backtrack.solveBoard(board);
  }

  /*
   * Execute elimination tasks in parallel and
   * continuing executing while at least one value
   * in the grid has been set
   */
  private static void elimination(Board board, AtomicBoolean valueSet, int nThreads) {
    do {
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
    } while (valueSet.get());
  }

  private static void loneRangers() {
    // do {
    //   execute tasks
    //   if (valueSet.get()) {
    //     return;
    //   }
    // } while (valueSet.get());
  }
}
