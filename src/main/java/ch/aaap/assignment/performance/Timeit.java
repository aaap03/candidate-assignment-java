package ch.aaap.assignment.performance;


import ch.aaap.assignment.Application;

public class Timeit {

  public static void code(Runnable block) {
    long start = System.nanoTime();

    try {
      block.run();
    } finally {
      long end = System.nanoTime();
      System.out.println("Time taken: " + (end - start) / 1.0e9);
    }
  }


  public static void main(String[] args) {

    System.out.print("Parallel mode: ");
    Timeit.code(() -> new Application(true));
    System.out.print("Concurrent mode: ");
    Timeit.code(() -> new Application(false));

  }


}
