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

        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 3; j++)
            {
                if (board[i + rowStart][j + colStart] != 0)
                    included.add(board[i + rowStart][j + colStart]);
            }
        }
        return included;
    }

    static ArrayList<Integer> getPossibilities(int[][] board, int row, int col)
    {
        ArrayList<Integer> included = onBoard(board, row, col);
        ArrayList<Integer> notIncluded = new ArrayList<Integer>();

        for (int i = 1; i <= 9; i++) 
        {
            if (included.contains(i) == true)
                notIncluded.add(i);
        }

        return notIncluded;
    }

    static boolean singleton(ArrayList<Integer> notIncluded)
    {
        if (notIncluded.size() == 1)
            return true;
        else
            return false;
    }

    static boolean sameColumn(ArrayList<myPair> coords)
    {
        for (int i = 1; i < coords.size(); i++)
        {
            if (coords.get(i-1).value() != coords.get(i).value())
                return false;
        }
        return true;
    }

    static boolean sameRow(ArrayList<myPair> coords)
    {
        for (int i = 1; i < coords.size(); i++)
        {
            if (coords.get(i-1).key() != coords.get(i).key())
                return false;
        }
        return true;
    }

    static boolean sameBox(ArrayList<myPair> coords)
    {
        int row = coords.get(0).key();
        int col = coords.get(0).value();

        int rowStart = row - row%3;
        int colStart = col - col%3;

        for (int i = 1; i < coords.size(); i++)
        {
            if (coords.get(i).key() < rowStart &&
                coords.get(i).key() > rowStart+3 &&
                coords.get(i).value() < colStart &&
                coords.get(i).value() > colStart+3) {
                    return false;
                }
        }
        return true;
    }

    static Map<myPair, ArrayList<Integer>> getBoard(int[][] board)
    {
        Map<myPair, ArrayList<Integer>> boardByCoords = new HashMap<>();
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (board[i][j] != 0)
                    continue;
                
                ArrayList<Integer> possibilities = getPossibilities(board, i, j);

                myPair coords = new myPair(i, j);
                boardByCoords.put(coords, possibilities);
            }
        }

        return boardByCoords;
    }

    static Map<ArrayList<Integer>, ArrayList<myPair>> getAllSets(Map<myPair, ArrayList<Integer>> boardByCoords)
    {
        Map<ArrayList<Integer>, ArrayList<myPair>> allPossibilities = new HashMap<>();

        for (Map.Entry<myPair, ArrayList<Integer>> cell : boardByCoords.entrySet())
        {
            myPair coords = cell.getKey();
            ArrayList<Integer> possibilities = cell.getValue();

            ArrayList<myPair> values = allPossibilities.get(possibilities);

            if (values != null) {
                values.add(coords);  
            }
            allPossibilities.put(possibilities, values);
        }

        return allPossibilities;
    }

    static Map<ArrayList<Integer>, ArrayList<myPair>> getPreemptiveSets(Map<ArrayList<Integer>, 
                                                                   ArrayList<myPair>> allPossibilities)
    {
        Map<ArrayList<Integer>, ArrayList<myPair>> preemptiveSets = new HashMap<>();

        for (Map.Entry<ArrayList<Integer>, ArrayList<myPair>> possibility : allPossibilities.entrySet()) 
        {
            ArrayList<Integer> nums = possibility.getKey();
            ArrayList<myPair> allCoords = possibility.getValue();

            if (sameBox(allCoords) &&
                sameColumn(allCoords) &&
                sameRow(allCoords) &&
                nums.size() == allCoords.size())
                {
                    preemptiveSets.put(nums, allCoords);
                }
        }
        return preemptiveSets;
    }

    static Map<myPair, ArrayList<Integer>> removeCoords(Map<ArrayList<Integer>, ArrayList<myPair>> preemptive, 
                              Map<ArrayList<Integer>, ArrayList<myPair>> all,
                              Map<myPair, ArrayList<Integer>> boardByCoords) 
    {
        for (Map.Entry<ArrayList<Integer>, ArrayList<myPair>> one : preemptive.entrySet())
        {
            ArrayList<Integer> possibilities = one.getKey();
            ArrayList<myPair> preemptiveCoords = one.getValue();
            ArrayList<myPair> allCoords = all.get(possibilities);

            ArrayList<myPair> coordsToUpdate = new ArrayList<myPair>(allCoords);
            coordsToUpdate.removeAll(preemptiveCoords);


            for (int i = 0; i < coordsToUpdate.size(); i++)
            {
                myPair coord = coordsToUpdate.get(i);
                ArrayList<Integer> newPossibilities = boardByCoords.get(coord);
                newPossibilities.removeAll(possibilities);

                boardByCoords.put(coord, newPossibilities);
            }
        }

        return boardByCoords;
    }

    static boolean updateBoard(int[][] board, Map<myPair, ArrayList<Integer>> boardByCoords)
    {
        boolean change = false;

        for (Map.Entry<myPair, ArrayList<Integer>> cell : boardByCoords.entrySet())
        {
            myPair coord = cell.getKey();
            int row = coord.key();
            int col = coord.value();
            ArrayList<Integer> possibilities = cell.getValue();

            if (singleton(possibilities))
            {
                board[row][col] = possibilities.get(0);
                change = true;
                boardByCoords.remove(coord);
            }
        }
        return change;
    }

    static boolean crooksSolve(int[][]board)
    {
        Map<myPair, ArrayList<Integer>> boardByCoords = getBoard(board);

        while(updateBoard(board, boardByCoords))
        {
            Map<ArrayList<Integer>, ArrayList<myPair>> allPossibilities = getAllSets(boardByCoords);
            Map<ArrayList<Integer>, ArrayList<myPair>> preemptiveSets = getPreemptiveSets(allPossibilities);
            boardByCoords = removeCoords(preemptiveSets, allPossibilities, boardByCoords);
        }

        return backtracking.solveSudoku(board);
    }
}