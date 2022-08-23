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

    static ArrayList<Integer> get_possibilities(int[][] board, int row, int col)
    {
        ArrayList<Integer> included = onBoard(board, row, col);
        ArrayList<Integer> not_included = new ArrayList<Integer>();

        for (int i = 1; i <= 9; i++) 
        {
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

    static boolean same_column(ArrayList<myPair> coords)
    {
        for (int i = 1; i < coords.size(); i++)
        {
            if (coords.get(i-1).value() != coords.get(i).value())
                return false;
        }
        return true;
    }

    static boolean same_row(ArrayList<myPair> coords)
    {
        for (int i = 1; i < coords.size(); i++)
        {
            if (coords.get(i-1).key() != coords.get(i).key())
                return false;
        }
        return true;
    }

    static boolean same_box(ArrayList<myPair> coords)
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

    static Map<myPair, ArrayList<Integer>> get_board(int[][] board)
    {
        Map<myPair, ArrayList<Integer>> board_by_coords = new HashMap<>();
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (board[i][j] != 0)
                    continue;
                
                ArrayList<Integer> possibilities = get_possibilities(board, i, j);

                // if (singleton(possibilities)) {
                //     board[i][j] = possibilities.get(0);
                // }
                // else
                // {
                    myPair coords = new myPair(i, j);
                    board_by_coords.put(coords, possibilities);
                
            }
        }

        return board_by_coords;
    }

    static Map<ArrayList<Integer>, ArrayList<myPair>> get_all_sets(Map<myPair, ArrayList<Integer>> board_by_coords)
    {
        Map<ArrayList<Integer>, ArrayList<myPair>> all_possibilities = new HashMap<>();

        for (Map.Entry<myPair, ArrayList<Integer>> cell : board_by_coords.entrySet())
        {
            myPair coords = cell.getKey();
            ArrayList<Integer> possibilities = cell.getValue();

            ArrayList<myPair> values = all_possibilities.get(possibilities);

            if (values != null) {
                values.add(coords);  
            }
            all_possibilities.put(possibilities, values);
        }

        return all_possibilities;
    }

    static Map<ArrayList<Integer>, ArrayList<myPair>> get_preemptive_sets(Map<ArrayList<Integer>, 
                                                                   ArrayList<myPair>> all_possibilities)
    {
        Map<ArrayList<Integer>, ArrayList<myPair>> preemptive_sets = new HashMap<>();

        for (Map.Entry<ArrayList<Integer>, ArrayList<myPair>> possibility : all_possibilities.entrySet()) 
        {
            ArrayList<Integer> nums = possibility.getKey();
            ArrayList<myPair> all_coords = possibility.getValue();

            if (same_box(all_coords) &&
                same_column(all_coords) &&
                same_row(all_coords) &&
                nums.size() == all_coords.size())
                {
                    preemptive_sets.put(nums, all_coords);
                }
        }
        return preemptive_sets;
    }

    static Map<myPair, ArrayList<Integer>> remove_coords(Map<ArrayList<Integer>, ArrayList<myPair>> preemptive, 
                              Map<ArrayList<Integer>, ArrayList<myPair>> all,
                              Map<myPair, ArrayList<Integer>> board_by_coords) 
    {
        for (Map.Entry<ArrayList<Integer>, ArrayList<myPair>> one : preemptive.entrySet())
        {
            ArrayList<Integer> possibilities = one.getKey();
            ArrayList<myPair> preemptive_coords = one.getValue();
            ArrayList<myPair> all_coords = all.get(possibilities);

            ArrayList<myPair> coords_to_update = new ArrayList<myPair>(all_coords);
            coords_to_update.removeAll(preemptive_coords);


            for (int i = 0; i < coords_to_update.size(); i++)
            {
                myPair coord = coords_to_update.get(i);
                ArrayList<Integer> new_possibilities = board_by_coords.get(coord);
                new_possibilities.removeAll(possibilities);

                board_by_coords.put(coord, new_possibilities);
            }
        }

        return board_by_coords;
    }

    static boolean update_board(int[][] board, Map<myPair, ArrayList<Integer>> board_by_coords)
    {
        boolean change = false;

        for (Map.Entry<myPair, ArrayList<Integer>> cell : board_by_coords.entrySet())
        {
            myPair coord = cell.getKey();
            int row = coord.key();
            int col = coord.value();
            ArrayList<Integer> possibilities = cell.getValue();

            if (singleton(possibilities))
            {
                board[row][col] = possibilities.get(0);
                change = true;
                board_by_coords.remove(coord);
            }
        }
        return change;
    }

    static boolean crooksSolve(int[][]board)
    {
        Map<myPair, ArrayList<Integer>> board_by_coords = get_board(board);

        while(update_board(board, board_by_coords))
        {
            Map<ArrayList<Integer>, ArrayList<myPair>> all_possibilities = get_all_sets(board_by_coords);
            Map<ArrayList<Integer>, ArrayList<myPair>> preemptive_sets = get_preemptive_sets(all_possibilities);
            board_by_coords = remove_coords(preemptive_sets, all_possibilities, board_by_coords);
        }

        return backtracking.solveSudoku(board);
    }
}