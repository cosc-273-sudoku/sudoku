public class SetPossibleValuesForCellTask implements Runnable {
  private Board board;
  private int row;
  private int col;

  public SetPossibleValuesForCellTask(Board board, int row, int col) {
    this.board = board;
    this.row = row;
    this.col = col;
  }

  public void run() {
    this.board.setPossibleValuesForCell(row, col);
  }
}
