import java.util.ArrayList;

public class crooks {

    static class myPair {
        private double key;
        private double value;

        public myPair(double a, double b)
        {
            key = a;
            value = b;
        }

        public double key()
        {
            return key;
        }
        public double value()
        {
            return value;
        }
    }

    static class arrayPairs {
        private ArrayList<myPair> array = new ArrayList<myPair>();

        public ArrayList<myPair> array()
        {
            return array;
        }

        // public boolean add(myPair pair)
        // {
        //     return possibilities.add(pair);
        // }
    }

    static class sets {
        private myPair coords;
        private arrayPairs possibilities;

        public sets(myPair cell, arrayPairs choices)
        {
            coords = cell;
            possibilities = choices;
        }

        public myPair coords()
        {
            return coords;
        }

        public arrayPairs possibilities()
        {
            return possibilities;
        }
    }

    static ArrayList<Integer> onBoard(int[][] board, int row, int col) {
        ArrayList<Integer> included = new ArrayList<Integer>();
        
        // Get row
        for (int j = 0; j < 9; j++)
            if (board[row][j] != 0)
                included.add(board[row][j]);

        // Get col
        for (int i = 0; i < 9; i++)
            if (board[i][col] != 0)
                included.add(board[i][col]);

        // Get box
        int rowStart = row - row%3;
        int colStart = col - col%3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + rowStart][j + colStart] != 0)
                    included.add(board[i + rowStart][j + colStart]);
            }
        }
        return included;
    }
}