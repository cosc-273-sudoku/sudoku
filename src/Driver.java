public class Driver {
  public static void main(String args[]) {
    String filename = "../boards/" + args[0];
    BoardGenerator generator = new BoardGenerator();
    // generator.generate(filename);
    Board board = generator.readTxt(filename);
    System.out.println("Sudoku Board:");
    System.out.println(board.toString());
    board.setPossibleValuesForGrid();
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        System.out.println(
            row + ", " + col + ": " + board.getBoard()[row][col].getPossibleValues());
      }
    }
    System.out.println("Solved Sudoku Board:");
    if (Backtrack.solveBoard(board)) System.out.println(board.toString());
    else System.out.println("No solution.");
  }
}
