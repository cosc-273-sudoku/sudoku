import java.io.*;
import java.util.ArrayList;

public class Results {
  private static String[] boardNames = new String[1];
  public static void main (String[] args) {

    // Takes in 3 arguments; the number of threads to test on, the number of trials per board, and the name of the output file
    if (args.length != 3) {
      System.out.println("USAGE: java Results [number of threads] [number of trials] [name of file to write results]");
      System.exit(0);
    }
    // Create Board objects for each board file; one for the sequential solver and one for the parallel solver
    BoardGenerator generator = new BoardGenerator();
    Board[] seqBoards = readBoards("boards", generator);
    Board[] parBoards = readBoards("boards", generator);
    // Tracks list of boards' names for output purposes
    boardNames = getNames("boards");

    // Gets the number of threads and trials from arguments, generates Summary objects
    // to store the results of each trial.
    int nThreads = Integer.valueOf(args[0]);
    int trials = Integer.valueOf(args[1]);
    Summary[] seqResults = new Summary[seqBoards.length];
    Summary[] parResults = new Summary[seqBoards.length];
    // Initializing Summary objects
    for (int i = 0; i < seqBoards.length; i++) {
      seqResults[i] = new Summary(trials);
      parResults[i] = new Summary(trials);
    }


    long start;
    long end;
    double runtime;
    for (int i = 0; i < trials; i++) {
      for (int j = 0; j < seqBoards.length; j++) {

        // Sequential run
        start = System.nanoTime();
        Backtrack.solveBoard(seqBoards[j]);
        end = System.nanoTime();
        runtime = (double) (end - start);
        seqResults[j].update(i, runtime);

        // Parallel run
        start = System.nanoTime();
        Parallel.solveBoard(parBoards[j], nThreads);
        end = System.nanoTime();
        runtime = (double) (end - start);
        parResults[j].update(i, runtime);

      }

      // Solving boards modifies their contents; we re-read the initial board states to reset them before the next set of trials
      seqBoards = readBoards("boards", generator);
      parBoards = readBoards("boards", generator);

    }
    // Prints results of solving each board in parallel and sequentially to the console
    System.out.println();
    for (int i = 0; i < seqBoards.length; i++) {
      System.out.println("Board: " + boardNames[i]);
      System.out.println("Average Seq. Runtime: " + seqResults[i].getMean() + "; Average Par. Runtime: " + parResults[i].getMean());
      System.out.println();
    }

    // Writes the same information to the output file
    try {
      FileWriter writer = new FileWriter(args[2]);
      for (int i = 0; i < seqBoards.length; i++) {
        writer.write("Board: " + boardNames[i] + "\n");
        writer.write("Average Seq. Runtime: " + seqResults[i].getMean() + "; Average Par. Runtime: " + parResults[i].getMean() + "\n" + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Error writing out results");
    }

    System.out.println("Results written to " + args[2]);
  }

  // Given a directory and a BoardGenerator object, read each file in the directory
  // as a Board, and add it to a list of Boards. Return an array containing all Boards found
  private static Board[] readBoards (String directory, BoardGenerator bg) {
    final File folder = new File("../" + directory);
    if (!folder.isDirectory()) {
      System.out.println("Non-folder directory received");
      return new Board[1];
    }

    ArrayList<Board> boards = new ArrayList<Board>();

    for (final File file : folder.listFiles()) {
      String fileName = file.getName();
      Board temp = bg.readTxt("../" + directory + "/" + fileName);
      boards.add(temp);
    }
    Board[] a = new Board[1];
    return boards.toArray(a);
  }

  // Given a directory, collect a list of file names in the directory
  private static String[] getNames (String directory) {
    final File folder = new File("../" + directory);
    if (!folder.isDirectory()) {
      System.out.println("Non-folder directory received");
      return new String[1];
    }

    ArrayList<String> names = new ArrayList<String>();
    for (final File file : folder.listFiles()) {
      String fileName = file.getName();
      names.add(fileName);
    }

    return names.toArray(boardNames);
  }
}

// Summary class stores an array of runtimes
// for n different trials, and returns the mean
// value when prompted.
class Summary {
  double[] runtimes;

  Summary (int size) {
    this.runtimes = new double[size];
  }

  public void update (int index, double value) {
    runtimes[index] = value;
  }

  public double getMean() {
    double avg = 0;
    for (int i = 0; i < runtimes.length; i++) {
      avg += runtimes[i];
    }
    return (avg / runtimes.length);
  }
}
