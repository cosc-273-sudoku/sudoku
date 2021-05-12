import java.util.concurrent.atomic.AtomicBoolean;

public class EliminationTask implements Runnable {
  private int cornerRow;
  private int cornerCol;
  private boolean checkRows; // if false, check columns
  private AtomicBoolean valueChanged;

  public EliminationTask(
      int cornerRow, int cornerCol, boolean checkRows, AtomicBoolean valueChanged) {
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.checkRows = checkRows;
    this.valueChanged = valueChanged;
  }

  // TODO
  public void run() {}
}
