import java.util.concurrent.atomic.AtomicBoolean;

public class LoneRangersTask implements Runnable {
  private Board board;
  private int cornerRow;
  private int cornerCol;
  private boolean checkRows;
  private boolean checkCols;
  private AtomicBoolean valueSet;

  public LoneRangersTask(
      Board board,
      int cornerRow,
      int cornerCol,
      boolean checkRows,
      boolean checkCols,
      boolean checkMiniGrid,
      AtomicBoolean valueSet) {
    this.board = board;
    this.cornerRow = cornerRow;
    this.cornerCol = cornerCol;
    this.checkRows = checkRows;
    this.checkCols = checkCols;
    this.valueSet = valueSet;
  }

  public void run() {
    Cell[][] grid = this.board.getGrid();
    if (checkRows) {
      for (int r = 0; r < 3; r++) {
        int row = this.cornerRow + r;
        if (!this.board.tryLockCell(row, 0)) {
          continue;
        }
        for (int col = 0; col < 9; col++) {}
      }
    }
  }
}
