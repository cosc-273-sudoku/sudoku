# Sudoku

## Current Functionality

The program currently contains a baseline/sequential implementation of a sudoku solver and a class for handling sudoku puzzles (as 9x9 2D arrays). The sequential backtracking algorithm fills in numbers at blank locations one by one, checking if recursive calls (for subsequent unfilled locations) lead to a solution or not. If an assignment doesn't lead to a valid solution, it tries the next number for the current empty location, returning false if none of the numbers 1-9 leads to a solution. The class for puzzle objects generates 9x9 sudoku puzzles for testing, allowing for repetition and recording of runtime trials.

To compile and run the code:
```
cd src/
javac *.java
java Driver test_board.txt
```
The generated sudoku board will be in `boards/test_board.txt`. Further, both the generated board and its solution while be printed to standard out.

## Future Functionality

In future versions of the program, we’ll implement our improved solution with partial parallelization, as we described in our proposal. We’ll also add code to track relative runtimes of the baseline/optimized solutions.

## Testing for Correctness and Performance

We will test the correction by seeing if the sudoku board has been solved correctly (no duplicate values in all the row, column, and 3x3 boxes). We will test the performance of the baseline and optimized programs by tracking and comparing runtimes of the baseline and optimized programs.
