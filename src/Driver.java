public class Driver {
  public static void main(String args[]) {
    String filename = "../boards/" + args[0];
    BoardGenerator generator = new BoardGenerator();
    // generator.generate(filename);
    Board backtrackBoard = generator.readTxt(filename);
    System.out.println("Sudoku Board:");
    System.out.println(backtrackBoard);
    System.out.println("Possible Values:");
    backtrackBoard.setPossibleValuesForGrid();
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 9; c++) {
        System.out.println(
            r + ", " + c + ": " + backtrackBoard.getGrid()[r][c].getPossibleValues());
      }
    }
    System.out.println("Solved Sudoku Board (Backtrack):");
    Backtrack.solveBoard(backtrackBoard);
    Board parallelBoard = generator.readTxt(filename);
    System.out.println("Solved Sudoku Board (Parallel):");
    Parallel.solveBoard(parallelBoard);
    System.out.println(parallelBoard);
    if (backtrackBoard.equals(parallelBoard)) {
      System.out.println("Boards are equal.");
    }
  }
}
