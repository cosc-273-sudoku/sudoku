public class Driver {
  public static void main(String args[]) {
    String filename = args[0];
    BoardGenerator generator = new BoardGenerator();
    // generator.generate(filename);
    Board backtrackBoard = generator.readTxt(filename);
    long start;
    long stop;
    System.out.println("Sudoku Board:");
    start = System.nanoTime();
    System.out.println(backtrackBoard);
    stop = System.nanoTime();
    System.out.println("Solved Sudoku Board (Backtrack):");
    Backtrack.solveBoard(backtrackBoard);
    System.out.println("Runtime: " + (stop - start));
    System.out.println(backtrackBoard);
    Board parallelBoard = generator.readTxt(filename);
    start = System.nanoTime();
    Parallel.solveBoard(parallelBoard, 2);
    stop = System.nanoTime();
    System.out.println("Solved Sudoku Board (Parallel):");
    System.out.println("Runtime: " + (stop - start));
    System.out.println(parallelBoard);
    if (backtrackBoard.equals(parallelBoard)) {
      System.out.println("Boards are equal.");
    }
  }
}
