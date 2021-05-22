import java.io.*;
import java.util.ArrayList;

public class Results {
  private static String[] boardNames = new String[1];
  public static void main (String[] args) {

    if (args.length != 2) {
      System.out.println("USAGE: java Results [number of trials] [name of file to write results]");
      System.exit(0);
    }

    BoardGenerator generator = new BoardGenerator();
    Board[] seqBoards = readBoards("boards", generator);
    Board[] parBoards = readBoards("boards", generator);
    boardNames = getNames("boards");

    int trials = Integer.valueOf(args[0]);
    Summary[] seqResults = new Summary[seqBoards.length];
    Summary[] parResults = new Summary[seqBoards.length];
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
        Parallel.solveBoard(parBoards[j], 2); //TODO: update nThreads value?
        end = System.nanoTime();
        runtime = (double) (end - start);
        parResults[j].update(i, runtime);

      }

      // Boards have been modified; reset them before next set of trials
      seqBoards = readBoards("boards", generator);
      parBoards = readBoards("boards", generator);

    }
    System.out.println();
    for (int i = 0; i < seqBoards.length; i++) {
      System.out.println("Board: " + boardNames[i]);
      System.out.println("Average Seq. Runtime: " + seqResults[i].getMean() + "; Average Par. Runtime: " + parResults[i].getMean());
      System.out.println();
    }

    try {
      FileWriter writer = new FileWriter(args[1]);
      for (int i = 0; i < seqBoards.length; i++) {
        writer.write("Board: " + boardNames[i] + "\n");
        writer.write("Average Seq. Runtime: " + seqResults[i].getMean() + "; Average Par. Runtime: " + parResults[i].getMean() + "\n" + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Error writing out results");
    }

    System.out.println("Results written to " + args[1]);
  }

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
