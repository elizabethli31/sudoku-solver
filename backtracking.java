public class backtracking {

    static boolean finished(int[][] board)
    {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j <9; j++)
                if (board[i][j] == 0)
                    return false;
        return true;
    }

    static class Pair
    {
        int row;
        int col;
        Pair(int a, int b)
        {
            row = a;
            col = b;
        }
    }

    static boolean rowSafe(int[][] board, int row, int num)
    {
        for (int x=0; x<9; x++)
            if (board[row][x] == num)
                return false;
        return true;
    }

    static boolean columnSafe(int[][] board, int col, int num)
    {
        for (int x=0; x<9; x++)
            if (board[x][col] == num)
                return false;
        return true;
    }

    static boolean squareSafe(int[][] board, int row, int col, int num)
    {
        int rowStart = row - row%3;
        int colStart = col - col%3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + rowStart][j + colStart] == num)
                    return false;
            }
        }
        return true;
    }

    static boolean isSafe(int[][] board, int row, int col, int num)
    {
        return (rowSafe(board, row, num) 
        && columnSafe(board, col, num)
        && squareSafe(board, row, col, num));
    }

    static void printBoard(int[][] board)
    {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    static boolean solveSudoku(int[][] board)
    {
        if (finished(board))
            return true;

        Pair position = new Pair(0, 0);
        int row = position.row;
        int col = position.col;


        for (row = 0; row < 9; row++)
        {
            boolean zero = false;
            for (col = 0; col < 9; col++)
            {
                if (board[row][col] == 0) {
                    zero = true;
                    break;
                }
            }
            if (zero)
                break;
        }


        for (int num = 1; num <=9; num++)
        {
            if (isSafe(board, row, col, num))
            {
                board[row][col] = num;
                if (solveSudoku(board))
                {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    public static void main(String[] args)
    {
        int easy[][] = examples.easy();
        int medium[][] = examples.medium();
        int hard[][] = examples.hard();
        int expert[][] = examples.expert();

        if (solveSudoku(easy))
            printBoard(easy);

        if (solveSudoku(medium))
            printBoard(medium);
        
        if (solveSudoku(hard))
            printBoard(hard);

        if (solveSudoku(expert))
            printBoard(expert);
    }
}


