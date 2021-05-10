public class Driver {
  public static void main(String args[]) {
    Board generator = new Board();
    String filename = "../boards/" + args[0];
    generator.generate(filename);
    Sudoku p1 = generator.readTxt(filename);
    System.out.println("Sudoku Board:");
    System.out.println(p1.toString());
    System.out.println("Solved Sudoku Board:");
    if (Backtrack.solveSudoku(p1)) System.out.println(p1.toString());
    else System.out.println("No solution.");
  }
}
