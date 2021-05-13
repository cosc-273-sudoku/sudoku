import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Parallel {
  public static void solveBoard(Board board) {
    board.setPossibleValuesForGrid();
    AtomicBoolean valueSet = new AtomicBoolean(false);
    int nThreads = Runtime.getRuntime().availableProcessors();
    do {
      elimination(board, valueSet, nThreads);
      // loneRangers();
      // if (valueSet.get()) {
      //   continue;
      // }
    } while (valueSet.get());
    // System.out.println("Before backtrack:");
    // System.out.println(board);
    Backtrack.solveBoard(board);
  }

  private static void elimination(Board board, AtomicBoolean valueSet, int nThreads) {
    do {
      ExecutorService pool = Executors.newFixedThreadPool(nThreads);
      valueSet.set(false);
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
