import java.util.Scanner;
import java.io.*;
import java.io.File;

public class Board {
	/*
	 * creates a random sudoku board, creates a txt file, and writes the sudoku
	 * board to that txt file if creating the file is successful
	 */
	public void generate(String filename) {
		Sudoku sudoku = new Sudoku();
		sudoku.fillValues();
		this.createFile(sudoku, filename);
	}

	/*
	 * creates a txt file with the filename and writes the sudoku board into the txt
	 * file with the filename upon successful creation
	 */
	public void createFile(Sudoku sudoku, String filename) {
		try {
			File myObj = new File(filename);
			// checks to see if the file already exists
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getPath());
				this.exportToTxt(sudoku, filename);
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
	public void exportToTxt(Sudoku sudoku, String filename) {
		try {
			PrintWriter pr = new PrintWriter(filename);

			for (int i = 0; i < sudoku.matrix.length; i++) {
				for (int j = 0; j < sudoku.matrix.length; j++) {
					pr.print(sudoku.matrix[i][j] + " ");
				}
				pr.println();
			}
			pr.close();
		}
		// checks if the file does not exist
		catch (FileNotFoundException e) {
			System.out.println("File not found. I'll quit now.");
			System.exit(1);
		}
	}

	// reads from the txt file with the filename and returns a sudoku puzzle
	public Sudoku readTxt(String filename) {
		Sudoku sudoku = new Sudoku();
		try {
			Scanner scanner = new Scanner(new File(filename));
			while (scanner.hasNextInt()) {
				for (int i = 0; i < sudoku.matrix.length; i++) {
					for (int j = 0; j < sudoku.matrix.length; j++) {
						sudoku.matrix[i][j] = scanner.nextInt();
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
		return sudoku;
	}
}
