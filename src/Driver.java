public class Driver {
    public static void main(String args[]) {
        Board generator = new Board();
        String filename = "../boards/" + args[0];
        generator.generate(filename);
        Sudoku p1 = generator.readTxt(filename);
        System.out.println(p1.toString());
        if (Backtrack.solveSudoku(p1))
            System.out.println(p1.toString());
        else
            System.out.println("No solution.");
    }
}
