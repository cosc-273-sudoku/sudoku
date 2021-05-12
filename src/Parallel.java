import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Parallel {
  public static void solveBoard(Board board) {
    board.setPossibleValuesForGrid();
    AtomicBoolean solved = new AtomicBoolean(false);
    AtomicBoolean valueChanged = new AtomicBoolean(false);
    int nThreads = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(nThreads);
    // TODO
  }
}
