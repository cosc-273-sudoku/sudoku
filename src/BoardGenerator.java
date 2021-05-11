import java.io.*;
import java.io.File;
import java.util.Scanner;

public class BoardGenerator {
  /*
   * creates a random sudoku board, creates a txt file, and writes the sudoku
   * board to that txt file if creating the file is successful
   */
  public void generate(String filename) {
    Board board = new Board();
    board.fillValues();
    this.createFile(board, filename);
  }

  /*
   * creates a txt file with the filename and writes the sudoku board into the txt
   * file with the filename upon successful creation
   */
  public void createFile(Board board, String filename) {
    try {
      File file = new File(filename);
      // checks to see if the file already exists
      if (file.createNewFile()) {
        System.out.println("File created: " + file.getPath());
        this.exportToTxt(board, filename);
      } else {
        System.out.println("The file already exists. I'll quit now");
        System.exit(1);
      }
    }
    // checks if an error occurred
    catch (IOException e) {
      System.out.println("Something went wrong. I'll quit now.");
      System.exit(1);
    }
  }

  // writes the sudoku board into the txt file with the filename
  public void exportToTxt(Board board, String filename) {
    try {
      PrintWriter writer = new PrintWriter(filename);

      for (int i = 0; i < board.getLength(); i++) {
        for (int j = 0; j < board.getLength(); j++) {
          writer.print(board.getValue(i, j) + " ");
        }
        writer.println();
      }
      writer.close();
    }
    // checks if the file does not exist
    catch (FileNotFoundException e) {
      System.out.println("File not found. I'll quit now.");
      System.exit(1);
    }
  }

  // reads from the txt file with the filename and returns a sudoku puzzle
  public Board readTxt(String filename) {
    Board board = new Board();
    try {
      Scanner scanner = new Scanner(new File(filename));
      while (scanner.hasNextInt()) {
        for (int i = 0; i < board.getLength(); i++) {
          for (int j = 0; j < board.getLength(); j++) {
            board.setValue(i, j, scanner.nextInt());
          }
        }
      }
      scanner.close();
      System.out.println("Successful read from " + filename + ".");
    }
    // checks if file does not exist
    catch (FileNotFoundException e) {
      System.out.println("File not found. I'll quit now.");
      System.exit(1);
    }
    // checks if an error occurred
    catch (Exception e) {
      System.out.println("Something went wrong. I'll quit now.");
      System.exit(1);
    }
    return board;
  }
}
