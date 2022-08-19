import java.util.*;

public class crooks {

    static class myPair 
    {
        int key;
        int value;

        myPair(int a, int b)
        {
            key = a;
            value = b;
        }

        public int key()
        {
            return key;
        }
        public int value()
        {
            return value;
        }
    }

    static class arrayPairs 
    {
        private ArrayList<myPair> array = new ArrayList<myPair>();

        public ArrayList<myPair> array()
        {
            return array;
        }

        public boolean add(myPair pair)
        {
            return array.add(pair);
        }
    }

    static ArrayList<Integer> onBoard(int[][] board, int row, int col) 
    {
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

    static ArrayList<Integer> get_possibilities(int[][] board, int row, int col)
    {
        ArrayList<Integer> included = onBoard(board, row, col);
        ArrayList<Integer> not_included = new ArrayList<Integer>();

        for (int i = 1; i <= 9; i++) {
            if (included.contains(i) == true)
                not_included.add(i);
        }

        return not_included;
    }

    static boolean singleton(ArrayList<Integer> not_included)
    {
        if (not_included.size() == 1)
            return true;
        else
            return false;
    }

    static Map<ArrayList<Integer>, arrayPairs> get_preemptive_sets(int[][] board)
    {
        Map<ArrayList<Integer>, arrayPairs> all_possibilities = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0)
                    continue;

                ArrayList<Integer> possibilities = get_possibilities(board, i, j);

                if (singleton(possibilities)) {
                    board[i][j] = possibilities.get(0);
                }
                else
                {
                    myPair coords = new myPair(i, j);
                    arrayPairs values = all_possibilities.get(possibilities);

                    if (values != null) {
                        values.add(coords);  
                    }
                    all_possibilities.put(possibilities, values);
                }
            }
        }
        // filter to find preemptive sets


    }
}