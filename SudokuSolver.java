import java.io.*;

public class SudokuSolver
{
 public static void main (String[] args) throws IOException
   {
   byte[][] data0 = {{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 0, 0}};
   byte[][] data = {{0, 0, 4, 0, 3, 8, 0, 0, 0},{0, 0, 1, 0, 0, 6, 0, 0, 8},{0, 6, 0, 2, 7, 0, 0, 0, 0},{4, 0, 0, 1, 8, 0, 9, 0, 6},{0, 0, 0, 0, 0, 0, 0, 0, 0},{1, 0, 6, 0, 9, 7, 0, 0, 5},{0, 0, 0, 0, 1, 2, 0, 4, 0},{7, 0, 0, 5, 0, 0, 6, 0, 0},{0, 0, 0, 8, 6, 0, 5, 0, 0}};
   byte[][] data2 = {{8, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 3, 6, 0, 0, 0, 0, 0},{0, 7, 0, 0, 9, 0, 2, 0, 0},{0, 5, 0, 0, 0, 7, 0, 0, 0},{0, 0, 0, 0, 4, 5, 7, 0, 0},{0, 0, 0, 1, 0, 0, 0, 3, 0},{0, 0, 1, 0, 0, 0, 0, 6, 8},{0, 0, 8, 5, 0, 0, 0, 1, 0},{0, 9, 0, 0, 0, 0, 4, 0, 0}};
   byte[][] data3 = {{1, 0, 0, 0, 8, 9, 7, 0, 0},{0, 0, 9, 0, 0, 3, 0, 0, 0},{0, 0, 0, 0, 0, 0, 0, 1, 0},{0, 0, 0, 4, 0, 0, 0, 6, 8},{2, 5, 0, 0, 0, 0, 0, 0, 0},{0, 0, 7, 0, 0, 0, 0, 4, 2},{0, 9, 0, 0, 6, 5, 0, 0, 0},{0, 0, 6, 0, 0, 4, 0, 5, 0},{0, 0, 0, 8, 0, 0, 9, 0, 0}};
   byte[][] data4 = {{0, 9, 0, 0, 0, 0, 8, 0, 1},{1, 8, 0, 0, 2, 0, 0, 0, 0},{3, 0, 0, 0, 0, 0, 0, 0, 0},{0, 0, 0, 5, 3, 0, 0, 0, 4},{0, 7, 0, 0, 0, 0, 0, 0, 0},{5, 6, 8, 4, 0, 9, 0, 0, 0},{0, 0, 0, 6, 0, 0, 9, 0, 5},{8, 0, 6, 9, 0, 0, 4, 0, 0},{0, 1, 0, 0, 5, 0, 0, 0, 0}};
   
   SudokuSquare ss;
   ss = new SudokuSquare(data4);
   ss.printSudoku();
   System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
   ss.solveSudoku();
   }
}